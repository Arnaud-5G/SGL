package sure.basepackage.objects;

import sure.basepackage.renderers.Texture;
import sure.basepackage.utils.Color;

public abstract class RotatableObject extends GraphicsObject {
    public float x;
    public float y;

    protected float angle;

    public RotatableObject(float x, float y, float zIndex, Texture texture, int numOfVertices, Color color) {
        super(zIndex, texture, numOfVertices, color);
        this.x = x;
        this.y = y;

        this.angle = 0;
    }

    /**
     * @param angle in degrees
     */
    public void withAngle(float angle) {
        this.angle = angle;
    }

    /**
     * Calculates the position of a point at a certain angle along a centered circle with a specified radius
     * @param angle
     * @param radius
     * @return
     */
    protected float[] getPosAtAngle(double angle, float radius) {
        return new float[] {(float)(Math.sin(angle) * radius) + x, (float)(Math.cos(angle) * radius) + y};
    }
}
