package golfgame;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import sure.basepackage.listeners.KeyListener.KeyState;

import org.joml.Vector2f;
import org.joml.Vector3f;

import sure.basepackage.Game;
import sure.basepackage.Window;
import sure.basepackage.components.Updating;
import sure.basepackage.objects.Circle;
import sure.basepackage.objects.Rectangle;
import sure.basepackage.utils.Color;
import sure.basepackage.utils.SureMath;
import sure.basepackage.utils.Time;

public class GolfGame extends Game {
    GolfBall golfBall;
    Rectangle floor;
    int a = 0;
    Rectangle test;

    @Override
    public void load() {}

    @Override
    public void start() {
        golfBall = new GolfBall(300, 50, 20, 20, 2, null);
        golfBall.color = Color.RED;
        floor = new Rectangle(100, 50, 20, 500, 1, null);
        floor.color = Color.BLUE;
        new Rectangle(100, 100, 20, 5, 1, null);
        test = new Rectangle(0, 0, 10, 10, 0, null);
        test.color = Color.BLUE;
    }

    @Override
    public void execute() {
        Vector3f pos = camera.screenToWorld(new Vector2f(((float) Window.get().getActualWidth())/2, ((float) Window.get().getActualHeight())/2));
        test.x = pos.x;
        test.y = pos.y;
        test();
        System.out.println(Window.get().getActualHeight() + " (y) -> " + pos.y);
        System.out.println(Window.get().getActualWidth() + " (x) -> " + pos.x);
    }

    public void test() {
        Vector2f[] points = SureMath.getIntersectionPoints(golfBall, floor);
        if (points.length > 0) {
            new LifeTime(points[0].x, points[0].y).withLifeTime(0.5f).color = Color.WHITE;
        }
        for (Vector2f point : points) {
            new LifeTime(point.x, point.y).withLifeTime(0.2f);
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
