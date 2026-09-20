package sure.physicspackage;

import org.joml.Vector2f;

public class Gravity {
    public final static Vector2f DEFAULT_GRAVITY = new Vector2f(0f, -9.8f);
    protected static Vector2f gravity = DEFAULT_GRAVITY;

    public static void setGravity(Vector2f newGravity) {
        gravity = newGravity;
    }

    public static Vector2f getGravity() {
        return new Vector2f(gravity.x, gravity.y);
    }
}
