package sure.physicspackage.components;

import org.joml.Vector2f;

public interface Colliding {
    /**
     * @param object
     * @return the normal of the collision from this to the object
     */
    public Vector2f getCollisionNormal(Colliding object);

    public boolean contains(float[] point);
}
