import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import sure.basepackage.Game;
import sure.basepackage.listeners.KeyListener;
import sure.basepackage.objects.Button;
import sure.basepackage.objects.Rectangle;
import sure.basepackage.objects.ui.Slider;
import sure.basepackage.objects.ui.TextBox;
import sure.basepackage.objects.ui.TextField;
import sure.basepackage.renderers.Animation;
import sure.basepackage.renderers.Animator;
import sure.basepackage.renderers.VertexRenderer;
import sure.basepackage.utils.Assets;
import sure.basepackage.utils.Time;

public class Test extends Game {
    Slider sliderV;
    Slider sliderS;
    Slider sliderR;
    TextField textV;
    TextBox text;
    Rectangle rectangle;
    Button playButton;
    Animator animator;

    @Override
    public void load() {
        this.use(Assets.getShader("src/main/java/sure/basepackage/shaders/default.glsl"));
        this.use(Assets.getSprite("assets/Test Image1.png"));
        this.use(Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16));
        this.use(Assets.getSprite("assets/Test Image2.png"));
        Assets.getSound("assets/Collision8-Bit.ogg");
    }

    @Override
    public void start() {
        playButton = new Button(500, 300, 30, 30, null, ()-> Assets.getSound("assets/Collision8-Bit.ogg").play());
        sliderV = new Slider(1100, 600, 30, 200, 3, 100);
        sliderV.setValue(100);
        sliderS = new Slider(1100, 500, 30, 200, 0, 104);
        sliderS.setValue(20);
        sliderR = new Slider(1100, 400, 30, 200, 0, 5);
        sliderR.setValue(0);
        rectangle = new Rectangle(300, 300, 50, 100, 0, Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16).get(1));
        textV = new TextField(0, 200, 0);
        textV.scale(3);
        textV.set("B\ta");
        text = new TextBox(0, 600, 1);
        text.scale(3);
        animator = new Animator(Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16));
        new Rectangle(100, 100, 50, 50, 0, animator);
        animator.addAnimation(()-> KeyListener.getDownKeys()[GLFW_KEY_W], new Animation(new int[] {0, 1}, 0.3f));
        animator.addAnimation(()-> true, new Animation(new int[] {5}));
    }

    @Override
    public void execute() {
        text.set(textV.getText());
        Assets.getShader("src/main/java/sure/basepackage/shaders/default.glsl").uploadFloat("uTime", Time.getScaledTime());
        VertexRenderer.remove(rectangle);
        text.scale(sliderR.getValue());
        rectangle = new Rectangle(300, 300, sliderS.getValue(), (int) sliderV.getValue(), 0, Assets.getSpriteSheet("assets/Custom SpriteSheet.png", 16, 16).get((int) sliderR.getValue()));
    }
}
