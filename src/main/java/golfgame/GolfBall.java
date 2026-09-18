package golfgame;

import sure.physicspackage.components.UsesPhysics;

import org.joml.Vector2f;

import sure.basepackage.objects.Circle;
import sure.basepackage.objects.GraphicsObject;
import sure.basepackage.renderers.Texture;
import sure.physicspackage.Gravity;

public class GolfBall extends Circle implements UsesPhysics {
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
}
