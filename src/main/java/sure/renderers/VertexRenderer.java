package sure.renderers;

import org.lwjgl.BufferUtils;
import sure.objects.GraphicsObject;
import sure.objects.Rectangle;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexRenderer {
    private static ArrayList<GraphicsObject> objects = new ArrayList<GraphicsObject>();
    private static int totalVertices = 0;
    private static int totalElements = 0;

    private static int vaoID, vboID, eboID;

    private VertexRenderer() {}

    public static void start() {
        // generate vao, vbo and ebo
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer emptyFloatBuffer = BufferUtils.createFloatBuffer(0);
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, emptyFloatBuffer, GL_DYNAMIC_DRAW);

        IntBuffer emptyIntBuffer = BufferUtils.createIntBuffer(0);
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, emptyIntBuffer, GL_DYNAMIC_DRAW);

        // enable all attributes
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int textureSlotSize = 1;
        int vertexSizeBytes = (positionSize + colorSize + uvSize + textureSlotSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, textureSlotSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize + uvSize) * Float.BYTES);
        glEnableVertexAttribArray(3);
    }

    public static void add(GraphicsObject object) {
        boolean success = objects.add(object);
        if (success) {
            totalVertices += object.numberOfVertices();
            totalElements += object.numberOfElements();
        }
    }

    public static void remove(GraphicsObject object) {
        boolean success = objects.remove(object);
        if (success) {
            totalVertices -= object.numberOfVertices();
            totalElements -= object.numberOfElements();
        }
    }

    public static void render() {
        if (totalVertices == 0 || totalElements == 0) {
            return;
        }

        GraphicsObject[] objectArr = objects.toArray(new GraphicsObject[0]);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(totalVertices * GraphicsObject.NUMBER_OF_ATTRIBUTES);
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(totalElements * 3);

        int vertexOffset = 0;
        for (GraphicsObject graphicsObject : objectArr) {
            graphicsObject.update();
            vertexBuffer.put(graphicsObject.makePartialVAO());

            int[] localEBO = graphicsObject.makePartialEBO();
            for (int i = 0; i < localEBO.length; i++) {
                elementBuffer.put(localEBO[i] + vertexOffset);
            }

            vertexOffset += graphicsObject.numberOfVertices();
        }

        vertexBuffer.flip();
        elementBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_DYNAMIC_DRAW);

        glDrawElements(GL_TRIANGLES, totalElements * 3, GL_UNSIGNED_INT, 0);
    }

    public static void bind() {
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
    }

    public static void unbind() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static ArrayList<GraphicsObject> getGraphicsObjects() {
        return objects;
    }

    public static <T> ArrayList<T> getGraphicsObjects(Class<T> extend) {
        ArrayList<T> graphicsObjects = new ArrayList<>();
        for (GraphicsObject object : objects) {
            if (extend.isAssignableFrom(object.getClass())) {
                graphicsObjects.add((T) object);
            }
        }

        return graphicsObjects;
    }
}
