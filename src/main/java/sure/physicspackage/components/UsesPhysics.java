package sure.physicspackage.components;

import org.joml.Vector2f;

public interface UsesPhysics extends Colliding {
    public Vector2f calculateForces();
}