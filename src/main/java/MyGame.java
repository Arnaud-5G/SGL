import sure.Game;
import sure.objects.Button;
import sure.objects.Rectangle;
import sure.objects.ui.Slider;
import sure.objects.ui.TextBox;
import sure.objects.ui.TextField;
import sure.renderers.VertexRenderer;
import sure.sound.Sound;
import sure.utils.Assets;
import sure.utils.Time;

public class MyGame extends Game {
    Slider sliderV;
    Slider sliderS;
    Slider sliderR;
    TextField textV;
    Rectangle rectangle;
    Button playButton;

    Sound mySound;


    @Override
    public void load() {
        this.use(Assets.getShader("src/main/java/sure/shaders/default.glsl"));
        this.use(Assets.getSprite("assets/Test Image1.png"));
        this.use(Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16));
        this.use(Assets.getSprite("assets/Test Image2.png"));
    }

    @Override
    public void start() {
        mySound = new Sound("C:\\Users\\20200791\\Desktop\\Coding\\Java\\Game\\SureGameLibrary\\assets\\Collision8-Bit.ogg", false);
        mySound.play();
        playButton = new Button(500, 300, 30, 30, null, ()-> mySound.play());
        sliderV = new Slider(1100, 600, 30, 200, 3, 100);
        sliderV.setValue(100);
        sliderS = new Slider(1100, 500, 30, 200, 0, 104);
        sliderS.setValue(20);
        sliderR = new Slider(1100, 400, 30, 200, 0, 5);
        sliderR.setValue(0);
        rectangle = new Rectangle(300, 300, 50, 100, 1, Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16).get(1));
        textV = new TextField(0, 0, 0);
        textV.scale(5);
        textV.set("B");
        new Rectangle(100, 100, 50, 50, 0, Assets.getSprite("assets/Test Image1.png").get());
    }

    @Override
    public void execute() {
        Assets.getShader("src/main/java/sure/shaders/default.glsl").uploadFloat("uTime", Time.getScaledTime());
        VertexRenderer.remove(rectangle);
        rectangle = new Rectangle(300, 300, sliderS.getValue(), (int) sliderV.getValue(), 0, Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16).get((int) sliderR.getValue()));
//        textV.set(Float.toString(sliderR.getValue()));
    }
}
