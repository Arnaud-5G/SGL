package sure;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import sure.renderers.Shader;
import sure.renderers.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class Game {
    private float[] vertexArray = {
            // position + z        // color            // uv coords      // texture slot
            100f, 0f, 0f, 1f,      1f, 0f, 0f, 1f,     1f, 0f,           0f, // bottom right 0
            0f, 100f, 0f, 1f,      0f, 1f, 0f, 1f,     0f, 1f,           0f, // top left     1
            100f, 100f, 0f, 1f,    0f, 0f, 1f, 1f,     1f, 1f,           0f, // top right    2
            0f, 0f, 0f, 1f,        0f, 1f, 1f, 1f,     0f, 0f,           0f, // bottom left  3
            200f, 100f, 0f, 1f,      1f, 0f, 0f, 1f,     1f, 0f,           1f, // bottom right 0
            100f, 200f, 0f, 1f,      0f, 1f, 0f, 1f,     0f, 1f,           1f, // top left     1
            200f, 200f, 0f, 1f,    0f, 0f, 1f, 1f,     1f, 1f,           1f, // top right    2
            100f, 100f, 0f, 1f,        0f, 1f, 1f, 1f,     0f, 0f,           1f, // bottom left  3
    };

    // must be counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // top right triangle
            0, 1, 3, // bottom left triangle
            6, 5, 4, // top right triangle
            4, 5, 7, // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    protected Camera camera;
    protected Shader shader;
    protected Texture[] texture = new Texture[8];

    void init() {
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

        int positionSize = 4;
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

        this.start();

        if (camera == null) {
            camera = new Camera(new Vector2f());
        }

        if (shader == null) {
            shader = new Shader("src/main/java/sure/shaders/default.glsl");
        }
        shader.compile();
    }

    public abstract void start();

    void update() {
        // bind
        shader.use();
        shader.uploadMath4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMath4f("uView", camera.getViewMatrix());
        for (int i = 0; i < texture.length; i++) {
            if (texture[i] == null) {
                continue;
            }

            shader.uploadTexture("uTextureSampler" + i, i);
            glActiveTexture(GL_TEXTURE0 + i);
            texture[i].bind();
        }

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        // draw
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
        this.execute();

        // Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        shader.detach();
        for (int i = 0; i < texture.length; i++) {
            if (texture[i] == null) {
                continue;
            }

            texture[0].unbind();
        }
    }

    public abstract void execute();
}
