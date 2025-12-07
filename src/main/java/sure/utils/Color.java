package sure.utils;

public class Color {
    public float red;
    public float green;
    public float blue;
    public float alpha;

    public Color(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    public Color(float r, float g, float b, float a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }
}
