package sure.utils;

import org.joml.Vector2f;
import sure.objects.GraphicsObject;

public class SureMath {
    private SureMath() {}

    public static boolean contains(Vector2f point, GraphicsObject object) {
        float[][] vertices = object.getPoses();
        int numVertices = vertices.length;
        float x = point.x();
        float y = point.y();

        boolean inside = false;

        // segment from j to i (j being the smallest index)
        for (int i = 0, j = numVertices - 1; i < numVertices; j = i++) {
            float xi = vertices[i][0];
            float yi = vertices[i][1];
            float xj = vertices[j][0];
            float yj = vertices[j][1];

            boolean intersect = ((yi > y) != (yj > y)) // check if point is within exclusive y range of vi and vj
                    && (x <= (((xj - xi) * (y - yi) / (yj - yi + 1e-10)) + xi /* raycasts in the x axis and finds the x value along the segment */)); //TODO: find better way to not divide by 0

            if (y == yi && x <= xi) { // if raycast would perfectly touch a vertex checks only once per point
                intersect = true;
            }

            if (intersect) {
                inside = !inside;
            }
        }

        return inside;
    }
}
