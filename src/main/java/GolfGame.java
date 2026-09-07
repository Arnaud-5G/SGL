import sure.basepackage.Game;
import sure.basepackage.objects.Circle;
import sure.basepackage.objects.Rectangle;
import sure.basepackage.utils.Color;

public class GolfGame extends Game {
    Circle golfBall;
    Rectangle floor;

    @Override
    public void load() {

    }

    @Override
    public void start() {
        golfBall = new Circle(300, 300, 20, 100, 1, null);
        golfBall.color = Color.RED;
        floor = new Rectangle(100, 50, 20, 500, 0, null);
        floor.color = Color.BLUE;
    }

    @Override
    public void execute() {

    }
    
}
