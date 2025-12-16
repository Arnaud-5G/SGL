package sure.objects;

import org.joml.Vector2f;
import sure.utils.Color;

public class Circle extends GraphicsObject {
    public float x;
    public float y;
    public float radius;

    public Circle(float x, float y, float radius, int numOfVertices, float zIndex, int textureSlot) {
        super(zIndex, textureSlot, (numOfVertices + 1) <= 0 ? 1 : numOfVertices + 1, new Color(1, 1, 0, 1));

        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public int numberOfElements() {
        return numOfVertices;
    }

    @Override
    public float[][] generatePoses() {
        float[][] poses = new float[numOfVertices][2];
        poses[0] = new float[]{x, y};

        double angle = (2 * Math.PI) / (numOfVertices - 1);

        for (int i = 1; i < poses.length; i++) {
            poses[i] = getCirclePos(angle * (i - 1));
        }

        return poses;
    }

    protected float[] getCirclePos(double angle) {
        return new float[] {(float)(Math.sin(angle) * radius) + x, (float)(Math.cos(angle) * radius) + y};
    }

    @Override
    public float[][] generateUVs() { // TODO: remove placeholder
        float[][] uvs = new float[numOfVertices][2];
        for (int i = 0; i < uvs.length; i++) {
            uvs[i][0] = 0;
            uvs[i][1] = 0;
        }
        return uvs;
    }

    @Override
    public int[] makePartialEBO() {
        int[] miniEBO = new int[numOfVertices * 3];
        for (int i = 0; i < miniEBO.length; i += 3) {
            miniEBO[i + 0] = 0;
            if (i/3 + 1 >= numOfVertices) {
                miniEBO[i + 1] = 1;
            } else {
                miniEBO[i + 1] = i/3 + 1;
            }
            miniEBO[i + 2] = i/3;
        }
        return miniEBO;
    }
}
