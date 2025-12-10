import org.joml.Vector2f;
import org.joml.Vector3f;
import sure.Camera;
import sure.Game;
import sure.listeners.KeyListener;
import sure.listeners.MouseListener;
import sure.objects.Button;
import sure.objects.Circle;
import sure.objects.Rectangle;
import sure.renderers.Shader;
import sure.renderers.Texture;
import sure.utils.Assets;
import sure.utils.Color;
import sure.utils.Time;

import static org.lwjgl.glfw.GLFW.*;

public class MyGame extends Game {
    Circle object3;

    @Override
    public void load() {
        shader = Assets.getShader("src/main/java/sure/shaders/default.glsl");
        Assets.getShader("src/main/java/sure/shaders/blackAndWhite.glsl");
        texture[0] = Assets.getTexture("assets/Test Image1.png");
        texture[1] = Assets.getTexture("assets/Test Image2.png");

        object3 = new Circle(300, 300, 300, 100, 1, -1);
        object3.color = new Color(0, 0, 0, 1);
    }

    @Override
    public void start() {

    }

    @Override
    public void execute() {
        shader.uploadFloat("uTime", Time.getScaledTime());
        System.out.println(Time.FPS());

//        object2.x = camera.screenToWorld(MouseListener.getMousePos()).x;
//        object2.y = camera.screenToWorld(MouseListener.getMousePos()).y;
    }
}
