import sure.Game;
import sure.listeners.KeyListener;
import sure.renderers.Shader;
import sure.renderers.Texture;
import sure.utils.Time;

import static org.lwjgl.glfw.GLFW.*;

public class MyGame extends Game {
    @Override
    public void start() {
        shader = new Shader("src/main/java/sure/shaders/default.glsl");
        texture[0] = new Texture("assets/Test Image1.png");
        texture[1] = new Texture("assets/Test Image2.png");
    }

    @Override
    public void execute() {
        shader.uploadFloat("uTime", Time.getScaledTime());
        if(KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.position.x -= Time.deltaTime() * 50f;
        } else if(KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.position.x += Time.deltaTime() * 50f;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            camera.position.y -= Time.deltaTime() * 50f;
        } else if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.position.y += Time.deltaTime() * 50f;
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            Time.scale = 0.5f;
        } else {
            Time.scale = 1f;
        }
    }
}
