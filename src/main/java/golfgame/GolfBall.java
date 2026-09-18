package golfgame;

import sure.physicspackage.components.UsesPhysics;
import sure.basepackage.components.Updating;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Vector2f;

import sure.basepackage.objects.Circle;
import sure.basepackage.objects.GraphicsObject;
import sure.basepackage.renderers.Texture;
import sure.physicspackage.Gravity;
import sure.basepackage.listeners.KeyListener;
import sure.basepackage.listeners.KeyListener.KeyState;

public class GolfBall extends Circle implements UsesPhysics, Updating {
    public GolfBall(float x, float y, float radius, int numOfVertices, float zIndex, Texture texture) {
        super(x, y, radius, numOfVertices, zIndex, texture);
    }

    public boolean contains(float[] point) {
        return Math.sqrt((double) (Math.pow(point[0]-x, 2) + Math.pow(point[1]-y, 2))) <= radius;
    }

    @Override
    public boolean isCollidingWith(GraphicsObject object) {
        for (float[] v : object.getPoses()) {
            if (contains(v))
                return true;
        }

        return false;
    }

    @Override
    public Vector2f calculateForces() {
        return Gravity.gravity;
    }

    @Override
    public void update() {
        this.y += (KeyListener.getKeyState(GLFW_KEY_UP) == KeyState.DOWN ? 5 : 0) - (KeyListener.getKeyState(GLFW_KEY_DOWN) == KeyState.DOWN ? 5 : 0);
        this.x += (KeyListener.getKeyState(GLFW_KEY_RIGHT) == KeyState.DOWN ? 5 : 0) - (KeyListener.getKeyState(GLFW_KEY_LEFT) == KeyState.DOWN ? 5 : 0);
        System.out.println("aaa");
    }
}
