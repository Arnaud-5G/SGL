package sure.physicspackage.components;

import org.joml.Vector2f;

public interface UsesPhysics extends Colliding {
    /**
     * @return the forces affecting the object
     */
    public Vector2f calculateForces();

    /**
     * Is called every frame to allow the object to interact with the forces affecting it
     * @param force
     * @param normal
     */
    public void moveObject(Vector2f force, Vector2f normal);
}