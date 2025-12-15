package sure.objects;

import sure.renderers.VertexRenderer;
import sure.utils.Color;

import java.awt.geom.Point2D;

public abstract class GraphicsObject {
    public static final int NUMBER_OF_ATTRIBUTES = 10;

    // attributes
    public float z;
    public int textureSlot;
    public Color color;

    protected int numOfVertices;
    protected float[][] vertexPos;
    protected float[][] vertexColor;
    protected float[][] vertexUV;

    public GraphicsObject(float zIndex, int textureSlot, int numOfVertices, Color color) {
        this.z = zIndex;
        this.textureSlot = textureSlot;
        this.numOfVertices = numOfVertices;
        this.color = color;
        vertexPos = new float[numOfVertices][2];
        vertexColor = new float[numOfVertices][4];
        vertexUV = new float[numOfVertices][2];

        VertexRenderer.add(this);
    }

    public abstract int numberOfElements();
    public abstract float[][] generatePoses();
    public abstract float[][] generateUVs();
    public abstract int[] makePartialEBO();

    public int numberOfVertices() {
        return numOfVertices;
    }

    public void update() {
        vertexPos = generatePoses();
        vertexColor = generateColors();
        vertexUV = generateUVs();
    }

    public float[][] generateColors() {
        float[][] colorArray = new float[numOfVertices][4];

        for (int i = 0; i < numOfVertices; i++) {
            colorArray[i][0] = color.red;
            colorArray[i][1] = color.green;
            colorArray[i][2] = color.blue;
            colorArray[i][3] = color.alpha;
        }

        return colorArray;
    }

    public float[] makePartialVAO() {
        float[] miniVAO = new float[numOfVertices * NUMBER_OF_ATTRIBUTES];
        for (int i = 0; i < numOfVertices; i++) {
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 0] = vertexPos[i][0];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 1] = vertexPos[i][1];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 2] = z;
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 3] = vertexColor[i][0];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 4] = vertexColor[i][1];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 5] = vertexColor[i][2];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 6] = vertexColor[i][3];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 7] = vertexUV[i][0];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 8] = vertexUV[i][1];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 9] = (float) textureSlot;
        }

        return miniVAO;
    }

    public float[][] getPoses() {
        return vertexPos;
    }
}
