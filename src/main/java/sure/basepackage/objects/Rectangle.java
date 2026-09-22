package sure.basepackage.objects;

import sure.basepackage.renderers.Texture;
import sure.basepackage.utils.Color;

public class Rectangle extends RotatableObject {
    public float width;
    public float height;

    public Rectangle(float x, float y, float height, float width, float zIndex, Texture texture) {
        super(x, y, zIndex, texture, 4, new Color(1, 1, 0, 1));

        this.width = width;
        this.height = height;
    }

    @Override
    public int numberOfElements() {
        return 2;
    }

    @Override
    public float[][] generatePoses() {
        float radius = (float) Math.hypot(height/2, width/2);

        return new float[][] {
                getPosAtAngle(Math.atan2(-width/2, -height/2) + Math.toRadians(angle), radius),
                getPosAtAngle(Math.atan2(-width/2, height/2) + Math.toRadians(angle), radius),
                getPosAtAngle(Math.atan2(width/2, height/2) + Math.toRadians(angle), radius),
                getPosAtAngle(Math.atan2(width/2, -height/2) + Math.toRadians(angle), radius)
                // {x - width / 2, y - height / 2}, // bottom left 3
                // {x - width / 2, y + height / 2}, // top left 1
                // {x + width / 2, y + height / 2}, // top right 2
                // {x + width / 2, y - height / 2}, // bottom right 0
        };
    }

    @Override
    public float[][] generateUVs() {
        return new float[][] {
                {0, 0},
                {0, 1},
                {1, 1},
                {1, 0},
        };
    }

    @Override
    public int[] makePartialEBO() {
        int[] miniEBO;

        miniEBO = new int[]{
                2, 1, 3, // top-right triangle
                3, 1, 0, // bottom-left triangle
        };

        return miniEBO;
    }
}
