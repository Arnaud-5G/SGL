package golfgame;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.joml.Vector2f;
import org.joml.Vector3f;

import sure.basepackage.Game;
import sure.basepackage.Window;
import sure.basepackage.components.Updating;
import sure.basepackage.listeners.KeyListener;
import sure.basepackage.listeners.KeyListener.KeyState;
import sure.basepackage.objects.Circle;
import sure.basepackage.objects.Rectangle;
import sure.basepackage.utils.Color;
import sure.basepackage.utils.SureMath;
import sure.basepackage.utils.Time;

public class GolfGame extends Game {
    GolfBall golfBall;
    Floor floor1;
    Floor floor2;
    int a = 0;
    Rectangle cursor;
    Test test;
    float angle = 0;

    @Override
    public void load() {}

    @Override
    public void start() {
        golfBall = new GolfBall(300, 500, 20, 20, 2, null);
        golfBall.color = Color.RED;
        floor1 = new Floor(300, 50, 20, 500, 1, null);
        floor1.color = Color.BLUE;
        floor1.withAngle(10);
        floor2 = new Floor(550, 25, 20, 100, 1, null);
        floor2.color = Color.BLUE;
        cursor = new Rectangle(0, 0, 10, 10, 0, null);
        cursor.color = Color.BLUE;
        test = new Test(290, 100, 20, 20, 2, null);
        test.color = Color.GREEN;
    }

    @Override
    public void execute() {
        Vector3f pos = camera.screenToWorld(new Vector2f(((float) Window.get().getActualWidth())/2, ((float) Window.get().getActualHeight())/2));
        cursor.x = pos.x;
        cursor.y = pos.y;

        if (KeyListener.getKeyState(GLFW_KEY_SPACE) == KeyState.DOWN) {
            angle += 0.1f;
        }

        floor1.withAngle(angle);
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
