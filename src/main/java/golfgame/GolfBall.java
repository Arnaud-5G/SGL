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
import sure.basepackage.utils.Assets;
import sure.basepackage.utils.SureMath;
import sure.basepackage.utils.Time;
import sure.physicspackage.Gravity;
import sure.basepackage.listeners.KeyListener;
import sure.basepackage.listeners.KeyListener.KeyState;

public class GolfBall extends Circle implements UsesPhysics, Updating {
    public float mass = 40;
    public Vector2f speed = new Vector2f();

    public GolfBall(float x, float y, float radius, int numOfVertices, float zIndex, Texture texture) {
        super(x, y, radius, numOfVertices, zIndex, texture);
    }

    @Override
    public boolean contains(float[] point) {
        return SureMath.Circle.contains(point, new float[] {x, y}, radius);
    }

    @Override
    public Vector2f getCollisionNormal(Colliding object) {
        return SureMath.Circle.getCollisionNormal(this, new float[] {x, y}, object);
    }

    @Override
    public Vector2f calculateForces() {
        return Gravity.getGravity().mul(mass);
    }

    @Override
    public void moveObject(Vector2f force, Vector2f normal) {
        float impact = SureMath.calculateImpactStrength(speed, normal);
        if (impact > 2f && speed.length() != 0) {
            Assets.getSound("assets/Collision8-Bit.ogg").play();
        }
        speed = SureMath.calculateSpeed(force, speed, normal, 0.4f, 0.1f);
    }

    @Override
    public void update() {
        this.y += speed.y * Time.scaledDeltaTime();
        this.x += speed.x * Time.scaledDeltaTime();
    }
}
