package sure.basepackage.objects;

import sure.basepackage.renderers.Texture;
import sure.basepackage.utils.Color;

public class Circle extends RotatableObject {
    public float radius;

    public Circle(float x, float y, float radius, int numOfVertices, float zIndex, Texture texture) {
        super(x, y, zIndex, texture, (numOfVertices + 1) <= 0 ? 1 : numOfVertices + 1, new Color(1, 1, 0, 1));

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
            poses[i] = getPosAtAngle(angle * (i - 1) + Math.toRadians(this.angle), radius);
        }

        return poses;
    }

    @Override
    public float[][] generateUVs() {
        float[][] uvs = new float[numOfVertices][2];
        double angle = (2 * Math.PI) / (numOfVertices - 1);

        uvs[0][0] = 0.5f;
        uvs[0][1] = 0.5f;

        for (int i = 1; i < uvs.length; i++) {
            uvs[i][0] = uvs[0][0] + (float)(Math.sin(angle * (i - 1))) / 2;
            uvs[i][1] = uvs[0][1] + (float)(Math.cos(angle * (i - 1))) / 2;
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
