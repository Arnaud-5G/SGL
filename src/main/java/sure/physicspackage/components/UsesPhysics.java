package sure.physicspackage.components;

import org.joml.Vector2f;

public interface UsesPhysics extends Colliding {
    public Vector2f calculateForces();

    public void moveObject(Vector2f force, Vector2f normal);
}