package golfgame;

import sure.physicspackage.components.Colliding;
import sure.physicspackage.components.UsesPhysics;
import sure.basepackage.components.Updating;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Vector2f;

import sure.basepackage.objects.Circle;
import sure.basepackage.renderers.Texture;
import sure.basepackage.utils.SureMath;
import sure.basepackage.utils.Time;
import sure.physicspackage.Gravity;
import sure.basepackage.listeners.KeyListener;
import sure.basepackage.listeners.KeyListener.KeyState;

public class GolfBall extends Circle implements UsesPhysics, Updating {
    public float mass = 40;
    public GolfBall(float x, float y, float radius, int numOfVertices, float zIndex, Texture texture) {
        super(x, y, radius, numOfVertices, zIndex, texture);
    }

    @Override
    public boolean contains(float[] point) {
        return Math.hypot(point[0]-x, point[1]-y) <= radius;
    }

    @Override
    public Vector2f getCollisionNormal(Colliding object) {
        Vector2f normal = new Vector2f();
        for (float[] point : this.getPoses()) {
            if (object.contains(point)) {
                Vector2f subNormal = new Vector2f(x - point[0], y - point[1]);
                normal.add(subNormal);
            }
        }

        return SureMath.normalize(normal);
    }

    @Override
    public Vector2f calculateForces() {
        return Gravity.getGravity().mul(mass);
    }

    @Override
    public void moveObject(Vector2f force) {
        this.x += force.x * Time.scaledDeltaTime();
        System.out.println("force x : " + force.x);
        this.y += force.y * Time.scaledDeltaTime();
        System.out.println("force y : " + force.y);
    }

    @Override
    public void update() {
        this.y += Time.getScaledTime() * (KeyListener.getKeyState(GLFW_KEY_UP) == KeyState.DOWN ? 5 : 0) - (KeyListener.getKeyState(GLFW_KEY_DOWN) == KeyState.DOWN ? 5 : 0);
        this.x += Time.getScaledTime() * (KeyListener.getKeyState(GLFW_KEY_RIGHT) == KeyState.DOWN ? 5 : 0) - (KeyListener.getKeyState(GLFW_KEY_LEFT) == KeyState.DOWN ? 5 : 0);
    }
}
