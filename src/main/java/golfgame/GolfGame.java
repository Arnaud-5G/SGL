package golfgame;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.joml.Vector2f;

import sure.basepackage.Game;
import sure.basepackage.camera.Coordinates.Screen;
import sure.basepackage.camera.Coordinates.World;
import sure.basepackage.components.Updating;
import sure.basepackage.listeners.KeyListener;
import sure.basepackage.listeners.KeyListener.KeyState;
import sure.basepackage.listeners.MouseListener;
import sure.basepackage.objects.Button;
import sure.basepackage.objects.Circle;
import sure.basepackage.objects.Rectangle;
import sure.basepackage.objects.ui.FPS;
import sure.basepackage.objects.ui.TextBox;
import sure.basepackage.sound.Sound;
import sure.basepackage.utils.Assets;
import sure.basepackage.utils.Color;
import sure.basepackage.utils.Time;

public class GolfGame extends Game {
    FPS fpsCounter;
    GolfBall golfBall;
    Floor floor1;
    Floor floor2;
    Floor floor3;
    int a = 0;
    Rectangle cursor;
    Test test;
    Button restartButton;
    TextBox coords;

    @Override
    public void load() {
        Assets.getSound("assets/Collision8-Bit.ogg");
        Assets.getSound("assets/EDM.ogg");
    }

    @Override
    public void start() {
        Sound music = Assets.getSound("assets/EDM.ogg", true);
        music.setVolume(0.1f);
        music.play();
        fpsCounter = new FPS(10);
        golfBall = new GolfBall(300, 500, 20, 1000, 2, null);
        golfBall.color = Color.RED;
        floor1 = new Floor(300, 50, 20, 500, 1, null);
        floor1.color = Color.BLUE;
        floor1.withAngle(10);
        floor2 = new Floor(550, 10, 20, 10000, 1, null);
        floor2.color = Color.BLUE;
        floor3 = new Floor(700, 50, 20, 10000, 1, null);
        floor3.color = Color.BLUE;
        floor3.withAngle(-15);
        cursor = new Rectangle(0, 0, 10, 10, 0, null);
        cursor.color = Color.BLUE;
        test = new Test(290, 100, 20, 20, 2, null);
        test.color = Color.GREEN;
        restartButton = new Button(500, 500, 40, 40, null, ()-> {
            golfBall.delete();
            golfBall = new GolfBall(300, 500, 20, 1000, 2, null);
            golfBall.color = Color.RED;
        });
    }

    @Override
    public void execute() {
        Vector2f pos = Screen.origin().toWorld().toVector();
        cursor.x = pos.x;
        cursor.y = pos.y;
        Time.scale(0);

        if (KeyListener.getKeyState(GLFW_KEY_SPACE) == KeyState.DOWN) {
            Time.scale(1);
        }

        int x = (int) MouseListener.getMousePos().x;
        int y = (int) MouseListener.getMousePos().y;

        if (KeyListener.getKeyState(GLFW_KEY_LEFT_SHIFT) == KeyState.DOWN) {
            World mPos = MouseListener.getMousePos().toWorld();
            test.x = mPos.x;
            test.y = mPos.y;
        }
    }

    public class LifeTime extends Circle implements Updating {
        public final float startTime;
        public float maxTime = 0;

        public LifeTime(float x, float y) {
            super(x, y, 4, 15, 10, null);
            this.color = Color.BLACK;
            startTime = Time.getScaledTime();
        }
        
        public LifeTime withLifeTime(float time) {
            maxTime = time;
            return this;
        }

        @Override
        public void update() {
            if (startTime + maxTime >= Time.getScaledTime()) {
                delete();
            }
        }
    }
}
