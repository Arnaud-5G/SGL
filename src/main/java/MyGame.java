import sure.Game;
import sure.objects.Rectangle;
import sure.objects.ui.Slider;
import sure.renderers.VertexRenderer;
import sure.utils.Assets;
import sure.utils.Time;

public class MyGame extends Game {
    Slider sliderV;
    Slider sliderS;
    Slider sliderR;
    Rectangle rectangle;

    @Override
    public void load() {
        shader = Assets.getShader("src/main/java/sure/shaders/default.glsl");
        Assets.getShader("src/main/java/sure/shaders/blackAndWhite.glsl");
        this.use(Assets.getSprite("assets/Test Image1.png"));
        this.use(Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16));
        this.use(Assets.getSprite("assets/Test Image2.png"));
    }

    @Override
    public void start() {
        sliderV = new Slider(1100, 600, 30, 200, 3, 100);
        sliderV.setValue(100);
        sliderS = new Slider(1100, 500, 30, 200, -100, 200);
        sliderS.setValue(20);
        sliderR = new Slider(1100, 400, 30, 200, 0, 5);
        sliderR.setValue(0);
        rectangle = new Rectangle(300, 300, 50, 100, 1, Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16).get(1));
        new Rectangle(100, 100, 50, 50, 0, Assets.getSprite("assets/Test Image1.png").get());
    }

    @Override
    public void execute() {
        shader.uploadFloat("uTime", Time.getScaledTime());
        VertexRenderer.remove(rectangle);
        rectangle = new Rectangle(300, 300, sliderS.getValue(), (int) sliderV.getValue(), 0, Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16).get((int) sliderR.getValue()));
    }
}
