package sure.basepackage.utils;

import java.util.ArrayList;

import org.joml.Vector2f;

import sure.basepackage.objects.GraphicsObject;
import sure.physicspackage.components.Colliding;

public class SureMath {
    private SureMath() {
    }

    /**
     * Returns whether or not a certain value is in between two other values that do
     * not need to be ordered.
     * 
     * @param value the value to compare
     * @param num1  a non ordered float
     * @param num2  a non ordered float
     * @return true/false
     */
    public static boolean isBetween(float value, float num1, float num2) {
        return value == Math.clamp(value, Math.min(num1, num2), Math.max(num1, num2));
    }

    public static boolean intersects(GraphicsObject object1, GraphicsObject object2) {
        return getIntersectionPoints(object1, object2).length > 0;
    }

    public static Vector2f[] getIntersectionPoints(GraphicsObject object1, GraphicsObject object2) {
        float[][] vertices1 = object1.getPoses();
        float[][] vertices2 = object2.getPoses();

        ArrayList<Vector2f> points = new ArrayList<>();

        // segment from j to i (j being the smallest index)
        for (int i = 0, j = vertices1.length - 1; i < vertices1.length; j = i++) {
            for (int k = 0, m = vertices2.length - 1; k < vertices2.length; m = k++) {
                float[] point_i = vertices1[i];
                float[] point_j = vertices1[j];

                float[] point_k = vertices2[k];
                float[] point_m = vertices2[m];

                float[] point = segmentsIntersectionPoint(new float[][] {vertices1[i], vertices1[j]}, new float[][] {vertices2[k], vertices2[m]});
                if (point == null)
                    continue;

                points.add(new Vector2f(point[0], point[1]));
            }
        }

        return points.toArray(new Vector2f[0]);
    }

    public static float[] segmentsIntersectionPoint(float[][] segment1, float[][] segment2) {
        float[] point_i = segment1[0];
        float[] point_j = segment1[1];

        float[] point_k = segment2[0];
        float[] point_m = segment2[1];

        float a1 = (point_i[1] - point_j[1]) / (point_i[0] - point_j[0]);
        float a2 = (point_k[1] - point_m[1]) / (point_k[0] - point_m[0]);
        if ((Float.isInfinite(a1) && isBetween(point_i[0], point_k[0], point_m[0])
                && isBetween(point_i[1], point_k[1], point_m[1]) && isBetween(point_j[1], point_k[1], point_m[1]))
                || (Float.isInfinite(a2) && isBetween(point_k[0], point_i[0], point_j[0])
                        && isBetween(point_k[1], point_i[1], point_j[1])
                        && isBetween(point_m[1], point_i[1], point_j[1]))) {
            return point_i; // TODO: fix
        }

        float b1 = point_i[1] - a1 * point_i[0];
        float b2 = point_k[1] - a2 * point_k[0];

        float x = (b2 - b1) / (a1 - a2);
        float y = (a1 * b2 - b1 * a2) / (a1 - a2);

        if (isBetween(x, point_i[0], point_j[0]) && isBetween(y, point_i[1], point_j[1])
                && isBetween(x, point_k[0], point_m[0]) && isBetween(y, point_k[1], point_m[1])) {
            return new float[] {x, y};
        }

        return null;
    }

    // TODO: add extra push when inside an object to not make it stick inside
    public static Vector2f getCollisionNormal(Colliding object, float[] centroid, Colliding colliding) {
        Vector2f normal = new Vector2f();
        if (!(colliding instanceof GraphicsObject)) {
            return new Vector2f();
        }

        GraphicsObject collidingObject = (GraphicsObject) colliding;
        GraphicsObject objectGraphics = (GraphicsObject) object;

        for (int i = 0; i < objectGraphics.getPoses().length; i++) {
            float[] point = objectGraphics.getPoses()[i];
            if (colliding.contains(point)) {
                Vector2f subNormal;

                float[][] segment1 = new float[][] {objectGraphics.getPoses()[i-1 < 0 ? objectGraphics.getPoses().length-1 : i], point};
                float[][] segment2 = new float[][] {point, objectGraphics.getPoses()[i+1 >= objectGraphics.getPoses().length ? 0 : i]};
                
                float[][] intersectionPoints = new float[2][2];
                int m = 0;

                // check for where the segments touch the object
                for (int k = 0, j = collidingObject.getPoses().length - 1; k < collidingObject.getPoses().length; j = k++) {
                    intersectionPoints[m] = segmentsIntersectionPoint((m == 0 ? segment1 : segment2), new float[][] {collidingObject.getPoses()[j], collidingObject.getPoses()[k]});
                    if (intersectionPoints[m] != null) {
                        if (m >= 1) {
                            break;
                        }
                        m++;
                    }
                }

                float deltaX = intersectionPoints[0][0] - intersectionPoints[1][0];
                float deltaY = intersectionPoints[0][1] - intersectionPoints[1][1];
                
                // take the perpendicular line to the segment
                subNormal = SureMath.normalize(new Vector2f(deltaY, -deltaX));

                // check for normal orientation
                if (subNormal.dot(new Vector2f(point[0] - centroid[0], point[1] - centroid[1])) > 0) {
                    subNormal.mul(-1);
                }
                
                normal.add(subNormal);
            }
        }

        return SureMath.normalize(normal);
    }

    public static boolean contains(Vector2f point, GraphicsObject object) {
        return contains(new float[] { point.x, point.y }, object);
    }

    public static boolean contains(float[] point, GraphicsObject object) {
        float[][] vertices = object.getPoses();
        int numVertices = vertices.length;
        float x = point[0];
        float y = point[1];

        boolean inside = false;

        // segment from j to i (j being the smallest index)
        for (int i = 0, j = numVertices - 1; i < numVertices; j = i++) {
            float xi = vertices[i][0];
            float yi = vertices[i][1];
            float xj = vertices[j][0];
            float yj = vertices[j][1];

            boolean intersect = ((yi > y) != (yj > y)) // check if point is within exclusive y range of vi and vj
                    && (x <= (((xj - xi) * (y - yi) / (yj - yi + 1e-10)) + xi /*
                                                                               * raycasts in the x axis and finds the x
                                                                               * value along the segment
                                                                               */)); // TODO: find better way to not
                                                                                     // divide by 0

            if (y == yi && x <= xi) { // if raycast would perfectly touch a vertex checks only once per point
                intersect = true;
            }

            if (intersect) {
                inside = !inside;
            }
        }

        return inside;
    }

    public static Vector2f normalize(Vector2f vector) {
        if (vector.length() == 0)
            return vector;
        return vector.normalize();
    }
}
