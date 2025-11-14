package sure.scenes;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import sure.Camera;
import sure.renderers.Shader;
import sure.renderers.Texture;
import sure.utils.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {
    private float[] vertexArray = {
        // position      // color            // uv coords
        50f, 0f, 0f,     1f, 0f, 0f, 1f,     1f, 0f,// bottom right 0
        0f, 50f, 0f,     0f, 1f, 0f, 1f,     0f, 1f,// top left     1
        50f, 50f, 0f,    0f, 0f, 1f, 1f,     1f, 1f,// top right    2
        0f, 0f, 0f,      0f, 1f, 1f, 1f,     0f, 0f,// bottom left  3
    };

    // must be counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // top right triangle
            0, 1, 3, // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader shader;
    private Texture texture;

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        camera = new Camera(new Vector2f());
        shader = new Shader("src/main/java/sure/shaders/default.glsl");
        shader.compile();
        texture = new Texture("assets/Test Image1.png");

        // generate vao, vbo and ebo
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize)*Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update() {
        camera.position.x -= Time.deltaTime() * 50f;

        // bind
        shader.use();
        shader.uploadMath4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMath4f("uView", camera.getViewMatrix());
        shader.uploadFloat("uTime", Time.getTime());
        shader.uploadTexture("uTextureSampler", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // draw
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        shader.detach();
    }
}
