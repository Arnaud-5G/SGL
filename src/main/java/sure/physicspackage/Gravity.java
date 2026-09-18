package sure.physicspackage;

import org.joml.Vector2f;

public class Gravity {
    public final static Vector2f DEFAULT_GRAVITY = new Vector2f(0f, -9.8f);
    public static Vector2f gravity = DEFAULT_GRAVITY;

    public void setGravity(Vector2f newGravity) {
        gravity = newGravity;
    }
}
