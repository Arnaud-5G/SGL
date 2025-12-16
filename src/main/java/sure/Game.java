package sure;

import sure.components.Clickable;
import sure.components.Updating;
import sure.listeners.MouseListener;
import static sure.listeners.MouseListener.*;
import sure.objects.GraphicsObject;
import sure.renderers.Shader;
import sure.renderers.Texture;
import sure.renderers.VertexRenderer;

import org.joml.Vector2f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public abstract class Game {
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

        MouseListener.attachCamera(camera);

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
        handleStandardComponents();
        this.execute();
        VertexRenderer.render();

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

    private void handleStandardComponents() {
        handleClickables();
        handleUpdatings();
    }

    private void handleUpdatings() {
        ArrayList<Updating> objects = VertexRenderer.getGraphicsObjects(Updating.class);
        for (Updating object : objects) {
            object.update();
        }
    }

    private void handleClickables() {
        ArrayList<Clickable> objects = VertexRenderer.getGraphicsObjects(Clickable.class);
        for (Clickable object : objects) {
            if (!(object.contains(camera.screenToWorld(MouseListener.getMousePos())))) {
                continue;
            }

            if (MouseListener.mouseButtonDown(MouseButton.LEFT)) {
                object.clickEvent(MouseButton.LEFT);
            }

            if (MouseListener.mouseButtonDown(MouseButton.RIGHT)) {
                object.clickEvent(MouseButton.RIGHT);
            }
        }
    }

    public abstract void execute();
}
