import org.joml.Vector2f;
import org.joml.Vector3f;
import sure.Game;
import sure.listeners.KeyListener;
import sure.listeners.MouseListener;
import sure.objects.Button;
import sure.objects.GraphicsObject;
import sure.renderers.Shader;
import sure.renderers.Texture;
import sure.utils.Assets;
import sure.utils.Time;

import static org.lwjgl.glfw.GLFW.*;

public class MyGame extends Game {
    Button object;
    GraphicsObject object2;
    float i =0;

    @Override
    public void load() {
        shader = Assets.getShader("src/main/java/sure/shaders/default.glsl");
        Assets.getShader("src/main/java/sure/shaders/blackAndWhite.glsl");
        texture[0] = Assets.getTexture("assets/Test Image1.png");
        texture[1] = Assets.getTexture("assets/Test Image2.png");

        object2 = new GraphicsObject(400, 400, 0, 200, 100, 0);
        object = new Button(100, 100);
    }

    @Override
    public void start() {

    }

    @Override
    public void execute() {
        shader.uploadFloat("uTime", Time.getScaledTime());
//        if(KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
//            object.x += Time.deltaTime() * 50f;
//        } else if(KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
//            object.x -= Time.deltaTime() * 50f;
//        }
//        if(KeyListener.isKeyPressed(GLFW_KEY_UP)) {
//            object.y += Time.deltaTime() * 50f;
//        } else if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
//            object.y -= Time.deltaTime() * 50f;
//        }

//        if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
//            object.width += Time.deltaTime() * 50f;
//            object.height += Time.deltaTime() * 50f;
//        } else {
//            object.width = 100;
//            object.height = 100;
//        }
    }
}
