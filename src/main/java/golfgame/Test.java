package golfgame;

import org.joml.Vector2f;

import sure.basepackage.objects.Circle;
import sure.basepackage.renderers.Texture;
import sure.basepackage.utils.SureMath;
import sure.physicspackage.components.Colliding;

public class Test extends Circle implements Colliding {

    public Test(float x, float y, float radius, int numOfVertices, float zIndex, Texture texture) {
        super(x, y, radius, numOfVertices, zIndex, texture);
    }

    @Override
    public Vector2f getCollisionNormal(Colliding object) {
        Vector2f normal = new Vector2f();
        for (float[] point : this.getPoses()) {
            if (object.contains(point)) {
                Vector2f subNormal = new Vector2f(point[0] - x, point[1] - y);
                normal.add(subNormal);
            }
        }

        return SureMath.normalize(normal);
    }

    @Override
    public boolean contains(float[] point) {
        return Math.hypot(point[0]-x, point[1]-y) <= radius;
    }
    
}
