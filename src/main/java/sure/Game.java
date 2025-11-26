package sure;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import sure.renderers.Shader;
import sure.renderers.Texture;
import sure.renderers.VertexRenderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class Game {
    private float[] vertexArray = {
            // position + z        // color            // uv coords      // texture slot
            100f, 0f, 0f,      1f, 0f, 0f, 1f,     1f, 0f,           0f, // bottom right 0
            0f, 100f, 0f,      0f, 1f, 0f, 1f,     0f, 1f,           0f, // top left     1
            100f, 100f, 0f,    0f, 0f, 1f, 1f,     1f, 1f,           0f, // top right    2
            0f, 0f, 0f,        0f, 1f, 1f, 1f,     0f, 0f,           0f, // bottom left  3
    };

    // must be counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // top right triangle
            0, 1, 3, // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    protected Camera camera;
    protected Shader shader;
    protected Texture[] texture = new Texture[31]; // max number of textures supported by opengl

    void init() {
        VertexRenderer.start();

        this.load();

        // force load required objects
        if (camera == null) {
            camera = new Camera(new Vector2f());
        }

        if (shader == null) {
            shader = new Shader("src/main/java/sure/shaders/default.glsl");
        }
        shader.compile();

        this.start();
    }

    public abstract void load();

    public abstract void start();

    void update() {
        VertexRenderer.bind();
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

        // draw
        VertexRenderer.render();
        this.execute();

        // Unbind
        VertexRenderer.unbind();
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
