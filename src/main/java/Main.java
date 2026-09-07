import sure.basepackage.Window;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.run(new GolfGame());
    }
}
