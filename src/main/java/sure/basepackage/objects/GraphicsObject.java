package sure.basepackage.objects;

import sure.basepackage.renderers.Texture;
import sure.basepackage.renderers.VertexRenderer;
import sure.basepackage.utils.Color;

public abstract class GraphicsObject extends GameObject implements Cloneable {
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

    /**
     * @return the number of triangles that should be rendered
     */
    public abstract int numberOfElements();

    /**
     * @return {@code float[x][2]} that contains all vertices coordinates in world space 
     */
    public abstract float[][] generatePoses();

    /**
     * @return {@code float[x][2]} that contains all vertices uv coordinates that go from 0-1
     */
    public abstract float[][] generateUVs();

    /**
     * Ex.: {@code new int[]{
                2, 1, 3, // top-right triangle
                3, 1, 0, // bottom-left triangle
        };}
     * @return {@code int[x]} that contains the order of triangles that should be rendered by indicating the vertices that make them up
     */
    public abstract int[] makePartialEBO();

    public int numberOfVertices() {
        return numOfVertices;
    }

    /**
     * This function should only be called by internal game logic
     */
    public void updateGraphics() {
        vertexPos = generatePoses();
        vertexColor = generateColors();
        vertexUV = generateUVs();
    }

    /**
     * Generates a color for each vertex
     * @return {@code float[x][4]} containing an rgba value for each vertex
     */
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

    /**
     * @return the attribute array to be fed to opengl
     */
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

    @Override
    public void delete() {
        VertexRenderer.remove(this);
        super.delete();
    }
}
