package golfgame;

import sure.physicspackage.components.*;

import org.joml.Vector2f;

import sure.basepackage.objects.*;
import sure.basepackage.renderers.Texture;
import sure.basepackage.utils.*;

public class Floor extends Rectangle implements Colliding {

    public Floor(float x, float y, float height, float width, float zIndex, Texture texture) {
        super(x, y, height, width, zIndex, texture);
    }

    @Override
    public Vector2f getCollisionNormal(Colliding object) {
        return SureMath.getCollisionNormal(this, new float[] {x, y}, object);
    }

    @Override
    public boolean contains(float[] point) {
        return SureMath.contains(point, this);
    }
}
