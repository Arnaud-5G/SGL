package sure.objects;

import sure.utils.Color;

public class Rectangle extends GraphicsObject {
    public float x;
    public float y;
    public float width;
    public float height;

    public Rectangle(float x, float y, float height, float width, float zIndex, int textureSlot) {
        super(zIndex, textureSlot, 4, new Color(1, 1, 0, 1));

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public int numberOfElements() {
        return 2;
    }

    @Override
    public float[][] generatePoses() {
        return new float[][] {
                {x + width / 2, y - height / 2}, // bottom right
                {x - width / 2, y + height / 2}, // top left
                {x + width / 2, y + height / 2}, // top right
                {x - width / 2, y - height / 2}, // bottom left
        };
    }

    @Override
    public float[][] generateUVs() {
        return new float[][] {
                {1, 0},
                {0, 1},
                {1, 1},
                {0, 0},
        };
    }

    @Override
    public int[] makePartialEBO() {
        int[] miniEBO;

        miniEBO = new int[]{
                2, 1, 0, // top-right triangle
                0, 1, 3, // bottom-left triangle
        };

        return miniEBO;
    }
}
