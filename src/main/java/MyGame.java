import sure.Game;
import sure.objects.Circle;
import sure.objects.ui.Slider;
import sure.renderers.VertexRenderer;
import sure.utils.Assets;
import sure.utils.Time;

public class MyGame extends Game {
    Slider sliderV;
    Slider sliderS;
    Slider sliderR;
    Circle circle;

    @Override
    public void load() {
        shader = Assets.getShader("src/main/java/sure/shaders/default.glsl");
        Assets.getShader("src/main/java/sure/shaders/blackAndWhite.glsl");
        texture[0] = Assets.getTexture("assets/Test Image1.png");
        texture[1] = Assets.getTexture("assets/Test Image2.png");
    }

    @Override
    public void start() {
        sliderV = new Slider(1100, 600, 30, 200, 3, 100);
        sliderV.setValue(100);
        sliderS = new Slider(1100, 500, 30, 200, -100, 200);
        sliderS.setValue(20);
        sliderR = new Slider(1100, 400, 30, 200, -360, 360);
        sliderR.setValue(0);
        circle = new Circle(300, 300, 50, 100, 1, 0);
    }

    @Override
    public void execute() {
        shader.uploadFloat("uTime", Time.getScaledTime());
        VertexRenderer.remove(circle);
        circle = new Circle(300, 300, sliderS.getValue(), (int) sliderV.getValue(), 0, 0);
        circle.withAngle(sliderR.getValue());
//      System.out.println(Time.FPS());

//      object2.x = camera.screenToWorld(MouseListener.getMousePos()).x;
//      object2.y = camera.screenToWorld(MouseListener.getMousePos()).y;
    }
}
