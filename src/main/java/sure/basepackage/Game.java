package sure.basepackage;

import sure.basepackage.listeners.KeyListener;
import sure.basepackage.objects.GameObject;
import sure.basepackage.renderers.Sprites.SpriteSheet;
import sure.basepackage.sound.Sound;
import sure.basepackage.listeners.MouseListener;

import sure.basepackage.renderers.Shader;
import sure.basepackage.renderers.VertexRenderer;

import org.joml.Vector2f;
import sure.basepackage.utils.Assets;
import sure.basepackage.camera.Camera;
import sure.basepackage.components.HandleComponents;
import sure.basepackage.components.StandardComponentBundle;
import sure.physicspackage.components.PhysicsComponentBundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;

public abstract class Game {
    protected Camera camera;
    private Shader shader;
    private SpriteSheet[] textures = new SpriteSheet[16];
    private final int[] textureSamplers = new int[textures.length];
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private HandleComponents componentHandler = new HandleComponents();

    final void init() {
        // start logging
        try {
            File file = new File("logs.txt");
            System.setOut(new PrintStream(file));
        } catch (Exception e) {
            System.out.println("Uh the log is not working!");
        }

        VertexRenderer.start();

        this.use(Assets.getSpriteSheet("src/main/java/sure/basepackage/assets/default_font.png", 20, 20));

        // add standard components
        componentHandler.addComponent(new StandardComponentBundle());
        componentHandler.addComponent(new PhysicsComponentBundle());

        for (int i = 0; i < textureSamplers.length; i++) {
            textureSamplers[i] = i;
        }

        // TODO: add default texture to slot 0 of textures[]

        this.load();

        // force load required objects
        if (camera == null) {
            camera = new Camera(new Vector2f());
        }

        if (shader == null) {
            shader = new Shader("src/main/java/sure/basepackage/shaders/default.glsl");
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

        // compute
        componentHandler.executeComponents();
        this.execute();

        // update Listeners
        KeyListener.updateListener();
        MouseListener.updateListener();

        // draw
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

    public abstract void execute();

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

    /**
     * Will load the given shader.
     * @param shader
     * @return true when this function has overriden an already loaded shader
     */
    public boolean use(Shader shader) {
        boolean wasShaderLoaded = this.shader != null;
        this.shader = shader;
        shader.compile();
        return wasShaderLoaded;
    }

    public boolean use(GameObject object) {
        boolean wasObjectPresent = gameObjects.contains(object);
        gameObjects.add(object);
        return wasObjectPresent;
    }

    /**
     * Does nothing but helps to have all of your load() lines the same
     * @param sound
     * @return true
     */
    public boolean use(Sound sound) {
        return true;
    }

    public boolean remove(GameObject object) {
        return gameObjects.remove(object);
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public <T> ArrayList<T> getGameObjects(Class<T> extend) {
        ArrayList<T> gameObjects = new ArrayList<>();
        for (GameObject object : this.gameObjects) {
            if (extend.isAssignableFrom(object.getClass())) {
                gameObjects.add((T) object);
            }
        }

        return gameObjects;
    }

    public Camera getCamera() {
        return camera;
    }
}
