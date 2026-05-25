package sure.renderers;

public class Texture {
    private int textureID;
    private float[][] uvcoords = new float[4][2];

    public Texture(int textureID, float[][] uvcoords) {
        this.textureID = textureID;
        this.uvcoords = uvcoords;
    }

    public int getTextureID() {
        return textureID;
    }

    public float[][] getUvcoords() {
        return uvcoords;
    }
}
