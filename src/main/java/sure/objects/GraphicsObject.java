package sure.objects;

import sure.renderers.Texture;
import sure.renderers.VertexRenderer;
import sure.utils.Color;

public abstract class GraphicsObject {
    public static final int NUMBER_OF_ATTRIBUTES = 10;

    // attributes
    public float z;
    public Texture texture;
    public Color color;

    protected int numOfVertices;
    protected float[][] vertexPos;
    protected float[][] vertexColor;
    protected float[][] vertexUV;

    public GraphicsObject(float zIndex, Texture texture, int numOfVertices, Color color) {
        this.z = zIndex;
        this.texture = texture;
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

    public void updateGraphics() {
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
        int textureID = texture != null ? texture.getTextureID() : -1;
        for (int i = 0; i < numOfVertices; i++) {
            float[] scaledUV = vertexUV[i];
            if (texture != null) {
                float uRange = texture.getUvcoords()[2][0]-texture.getUvcoords()[0][0];
                float vRange = texture.getUvcoords()[2][1]-texture.getUvcoords()[0][1];
                scaledUV[0] *= uRange;
                scaledUV[1] *= vRange;
                scaledUV[0] += texture.getUvcoords()[0][0];
                scaledUV[1] += texture.getUvcoords()[0][1];
            }
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 0] = vertexPos[i][0];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 1] = vertexPos[i][1];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 2] = z;
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 3] = vertexColor[i][0];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 4] = vertexColor[i][1];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 5] = vertexColor[i][2];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 6] = vertexColor[i][3];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 7] = scaledUV[0];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 8] = scaledUV[1];
            miniVAO[i * NUMBER_OF_ATTRIBUTES + 9] = (float) textureID;
        }

        return miniVAO;
    }

    public float[][] getPoses() {
        return vertexPos;
    }
}
