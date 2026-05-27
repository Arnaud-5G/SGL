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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL20.*;

public abstract class Game {
    protected Camera camera;
    protected Shader shader;
    private SpriteSheet[] textures = new SpriteSheet[16];
    private final int[] textureSamplers = new int[textures.length];
    private ArrayList<Pair<Class, Consumer>> components = new ArrayList<>();

    final void init() {
        VertexRenderer.start();

        // add standard components
        addComponent(Clickable.class, this::handleClickables);
        addComponent(Updating.class, this::handleUpdatings);

        for (int i = 0; i < textureSamplers.length; i++) {
            textureSamplers[i] = i;
        }

        // TODO: add default texture to slot 0 of textures[]

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
        shader.uploadIntArray("uTextureSampler", textureSamplers);
        for (int i = 0; i < textures.length; i++) {
            if (textures[i] == null) {
                continue;
            }

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

    /**
     * Will load the given texture at the appropriate id.
     * @param spritesheet
     * @return true when this function has overriden an already existing texture
     */
    public boolean use(SpriteSheet spritesheet) {
        boolean wasTextureUsed = textures[spritesheet.getTextureID()] != null;
        textures[spritesheet.getTextureID()] = spritesheet;
        return wasTextureUsed;
    }

    private void handleComponents() {
        for (Pair<Class, Consumer> component : components) {
            executeComponent(component);
        }
    }

    private <T> void executeComponent(Pair<Class, Consumer> component) {
        Class<T> type = (Class<T>) component.getFirst();
        Consumer<T[]> consumer = (Consumer<T[]>) component.getSecond();

        Object[] rawObjects = VertexRenderer.getGraphicsObjects(type).toArray();

        T[] typedArray = (T[]) Array.newInstance(type, rawObjects.length);
        System.arraycopy(rawObjects, 0, typedArray, 0, rawObjects.length);

        consumer.accept(typedArray);
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
