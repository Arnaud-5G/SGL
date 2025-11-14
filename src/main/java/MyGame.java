import sure.Game;
import sure.renderers.Shader;
import sure.renderers.Texture;
import sure.utils.Time;

public class MyGame extends Game {
    @Override
    public void start() {
        shader = new Shader("src/main/java/sure/shaders/textureSampling.glsl");
        texture = new Texture("assets/Test Image1.png");
    }

    @Override
    public void execute() {
        camera.position.x -= Time.deltaTime() * 50f;
    }
}
