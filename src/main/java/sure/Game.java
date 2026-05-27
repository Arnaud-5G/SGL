package sure;

import kotlin.Pair;
import sure.renderers.Sprites.SpriteSheet;
import sure.standardcomponents.Clickable;
import sure.standardcomponents.Updating;
import sure.listeners.MouseListener;
import static sure.listeners.MouseListener.*;

import sure.renderers.Shader;
import sure.renderers.VertexRenderer;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL20.*;

public abstract class Game {
    protected Camera camera;
    protected Shader shader;
    protected SpriteSheet[] textures = new SpriteSheet[31]; // max number of textures supported by opengl
    private ArrayList<Pair<Class, Consumer>> components = new ArrayList<>();

    final void init() {
        VertexRenderer.start();

        // add standard components
        addComponent(Clickable.class, this::handleClickables);
        addComponent(Updating.class, this::handleUpdatings);

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

    final void update() {
        VertexRenderer.bind();
        // bind
        shader.use();
        shader.uploadMath4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMath4f("uView", camera.getViewMatrix());
        for (int i = 0; i < textures.length; i++) {
            if (textures[i] == null) {
                continue;
            }

            shader.uploadTexture("uTextureSampler" + i, i);
            glActiveTexture(GL_TEXTURE0 + i);
            textures[i].bind();
        }

        // draw
        handleComponents();
        this.execute();
        VertexRenderer.render();

        // Unbind
        VertexRenderer.unbind();
        shader.detach();
        for (SpriteSheet texture : textures) {
            if (texture == null) {
                continue;
            }

            texture.unbind();
        }
    }

    private void handleComponents() {
        for (Pair<Class, Consumer> component : components) {
            component.getSecond().
                    accept(VertexRenderer.getGraphicsObjects(component.getFirst()).toArray());
        }
    }

    /**
     * This method is used to add component scripts to interfaces.
     * @apiNote This process is not reversible mid-execution.
     * @param componentInterface - an interface
     * @param consumer - the script to be run every frame on the selected objects
     *                 (This will need to cast the accepted objects into the desired type)
     */
    final <T> void addComponent(Class<T> componentInterface, Consumer<T[]> consumer) {
        components.add(new Pair<>(componentInterface, consumer));
    }

    private void handleUpdatings(Updating... objects) {
        for (Updating updating : objects) {
            updating.update();
        }
    }

    private void handleClickables(Clickable... objects) {
        for (Clickable clickable : objects) {
            if (!(clickable.contains(camera.screenToWorld(MouseListener.getMousePos())))) {
                continue;
            }

            if (MouseListener.mouseButtonDown(MouseButton.LEFT)) {
                clickable.clickEvent(MouseButton.LEFT);
            }

            if (MouseListener.mouseButtonDown(MouseButton.RIGHT)) {
                clickable.clickEvent(MouseButton.RIGHT);
            }
        }
    }

    public abstract void execute();
}
