package sure.objects;

import sure.renderers.VertexRenderer;

public class GraphicsObject {
    public static final int numberOfAttributes = 10; // 2 + 1 + 4 + 2 + 1 = 10

    // attributes
    public float x;
    public float y;
    public float z;
    public float width;
    public float height;

    protected int numOfVertices;
    protected float[][] vertexPos;
    protected float[][] vertexColor;
    protected float[][] vertexUV;
    public int textureSlot;

    public GraphicsObject(float x, float y) {
        this(x, y, 0);
    }

    public GraphicsObject(float x, float y, float zIndex) {
        this(x, y, zIndex, 100, 100, -1);
    }

    public GraphicsObject(float x, float y, float zIndex, float height, float width, int textureSlot) {
        this.x = x;
        this.y = y;
        this.z = zIndex;
        this.width = width;
        this.height = height;
        this.textureSlot = textureSlot;
        this.numOfVertices = 4; // TODO: change to be more or less
        vertexPos = new float[numOfVertices][2];
        vertexColor = new float[numOfVertices][4];
        vertexUV = new float[numOfVertices][2];

        VertexRenderer.add(this);
    }

    public int numberOfVertices() {
        return numOfVertices;
    }

    public int numberOfElements() {
         return 2; // TODO: change
    }

    public float[][] generateRectanglePoses() {
        return new float[][] {
                {x + width / 2, y - height / 2}, // bottom right
                {x - width / 2, y + height / 2}, // top left
                {x + width / 2, y + height / 2}, // top right
                {x - width / 2, y - height / 2}, // bottom left
        };
    }

    public float[][] generateRectangleColors() {
        return new float[][] {
                {1, 1, 0, 1},
                {1, 1, 0, 1},
                {1, 1, 0, 1},
                {1, 1, 0, 1},
        };
    }

    public float[][] generateRectangleUVs() {
        return new float[][] {
                {1, 0},
                {0, 1},
                {1, 1},
                {0, 0},
        };
    }

    public void update() {
        vertexPos = generateRectanglePoses();
        vertexColor = generateRectangleColors();
        vertexUV = generateRectangleUVs();
    }

    public float[] makePartialVAO() {
        float[] miniVAO = new float[numOfVertices * numberOfAttributes];
        for (int i = 0; i < numOfVertices; i++) {
            miniVAO[i * numberOfAttributes + 0] = vertexPos[i][0];
            miniVAO[i * numberOfAttributes + 1] = vertexPos[i][1];
            miniVAO[i * numberOfAttributes + 2] = z;
            miniVAO[i * numberOfAttributes + 3] = vertexColor[i][0];
            miniVAO[i * numberOfAttributes + 4] = vertexColor[i][1];
            miniVAO[i * numberOfAttributes + 5] = vertexColor[i][2];
            miniVAO[i * numberOfAttributes + 6] = vertexColor[i][3];
            miniVAO[i * numberOfAttributes + 7] = vertexUV[i][0];
            miniVAO[i * numberOfAttributes + 8] = vertexUV[i][1];
            miniVAO[i * numberOfAttributes + 9] = (float) textureSlot;
        }

        return miniVAO;
    }

    public int[] makePartialEBO() {
        int[] miniEBO; // TODO: support different shapes

        miniEBO = new int[]{
                2, 1, 0, // top-right triangle
                0, 1, 3, // bottom-left triangle
        };

        return miniEBO;
    }
}
