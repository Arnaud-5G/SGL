import org.joml.Vector2f;
import org.joml.Vector3f;
import sure.Camera;
import sure.Game;
import sure.listeners.KeyListener;
import sure.listeners.MouseListener;
import sure.objects.Button;
import sure.objects.Rectangle;
import sure.renderers.Shader;
import sure.renderers.Texture;
import sure.utils.Assets;
import sure.utils.Time;

import static org.lwjgl.glfw.GLFW.*;

public class MyGame extends Game {
    Button object2;

    @Override
    public void load() {
        shader = Assets.getShader("src/main/java/sure/shaders/default.glsl");
        Assets.getShader("src/main/java/sure/shaders/blackAndWhite.glsl");
        texture[0] = Assets.getTexture("assets/Test Image1.png");
        texture[1] = Assets.getTexture("assets/Test Image2.png");

        object2 = new Button(400, 400, 200, 200, 0);
    }

    @Override
    public void start() {

    }

    @Override
    public void execute() {
        shader.uploadFloat("uTime", Time.getScaledTime());

//        object2.x = camera.screenToWorld(MouseListener.getMousePos()).x;
//        object2.y = camera.screenToWorld(MouseListener.getMousePos()).y;
    }
}
