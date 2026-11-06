import golfgame.GolfGame;
import sure.basepackage.Window;
import trexgame.TRexGame;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.run(new GolfGame());
    }
}
