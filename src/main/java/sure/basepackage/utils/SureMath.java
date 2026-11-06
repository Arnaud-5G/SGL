package sure.basepackage.utils;

import java.util.ArrayList;

import org.joml.Vector2f;

import sure.basepackage.objects.GraphicsObject;

public class SureMath {
    protected SureMath() {
    }

    public static class ConvexPolygon {
        protected ConvexPolygon() {
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
                                                                                   * raycasts in the x axis and finds
                                                                                   * the x
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

                    float[] point = segmentsIntersectionPoint(new float[][] { vertices1[i], vertices1[j] },
                            new float[][] { vertices2[k], vertices2[m] });
                    if (point == null) {
                        continue;
                    } else if (point[1] == Float.NaN) {
                        point = point_i;
                    }

                    points.add(new Vector2f(point[0], point[1]));
                }
            }

            return points.toArray(new Vector2f[0]);
        }

        /**
         * @param segment1
         * @param segment2
         * @return the coordinates where the two segments intersect
         * @apiNote The point may be null which indicates that there is no intersection between the two segments. One or both of the axes may be NaN which indicates that the two segments overlap and no or only one axis could be found.
         */
        public static float[] segmentsIntersectionPoint(float[][] segment1, float[][] segment2) {
            float[] point_i = segment1[0];
            float[] point_j = segment1[1];

            float[] point_k = segment2[0];
            float[] point_m = segment2[1];

            // calculate the slope of the segments
            float a1 = (point_i[1] - point_j[1]) / (point_i[0] - point_j[0]);
            float a2 = (point_k[1] - point_m[1]) / (point_k[0] - point_m[0]);

            // if one or both of the segments are vertical
            if ((Float.isInfinite(a1) && isBetween(point_i[0], point_k[0], point_m[0])
                    && isBetween(point_i[1], point_k[1], point_m[1])
                    && isBetween(point_j[1], point_k[1], point_m[1])) &&
                (Float.isInfinite(a2) && isBetween(point_k[0], point_i[0], point_j[0])
                    && isBetween(point_k[1], point_i[1], point_j[1])
                    && isBetween(point_m[1], point_i[1], point_j[1]))) {
                return new float[] {point_i[0], Float.NaN}; // a1 and a2 are infinite and collide
            } else if ((Float.isInfinite(a1) && isBetween(point_i[0], point_k[0], point_m[0])
                    && isBetween(point_i[1], point_k[1], point_m[1])
                    && isBetween(point_j[1], point_k[1], point_m[1]))) {
                float b2 = point_k[1] - a2 * point_k[0];
                return new float[] {point_i[0], a2*point_i[0] + b2}; // a1 is infinite and collides with the other segment
            } else if ((Float.isInfinite(a2) && isBetween(point_k[0], point_i[0], point_j[0])
                    && isBetween(point_k[1], point_i[1], point_j[1])
                    && isBetween(point_m[1], point_i[1], point_j[1]))) {
                float b1 = point_i[1] - a1 * point_i[0];
                return new float[] {point_k[0], a1*point_k[0] + b1}; // a2 is infinite and collides with the other segment
            }

            // calculates the intersection between the y-axis and the segments if they were infinitely long
            float b1 = point_i[1] - a1 * point_i[0];
            float b2 = point_k[1] - a2 * point_k[0];

            // if the segments are on top of each other
            if (a1 == a2 && b1 == b2 && a1 == 0 && 
                isBetween(point_i[0], point_k[0], point_m[0]) || 
                isBetween(point_j[0], point_k[0], point_m[0]) || 
                isBetween(point_k[0], point_i[0], point_j[0]) || 
                isBetween(point_m[0], point_i[0], point_j[0])) {
                return new float[] {Float.NaN, b1}; // the segments are on top of each other but not horizontal
            } else if (a1 == a2 && b1 == b2 && 
                isBetween(point_i[0], point_k[0], point_m[0]) || 
                isBetween(point_j[0], point_k[0], point_m[0]) || 
                isBetween(point_k[0], point_i[0], point_j[0]) || 
                isBetween(point_m[0], point_i[0], point_j[0])) {
                return new float[] {Float.NaN, Float.NaN}; // the segments are on top of each other
            }

            // solves for coordinates
            float x = (b2 - b1) / (a1 - a2);
            float y = (a1 * b2 - b1 * a2) / (a1 - a2);

            if (isBetween(x, point_i[0], point_j[0]) && isBetween(y, point_i[1], point_j[1])
                    && isBetween(x, point_k[0], point_m[0]) && isBetween(y, point_k[1], point_m[1])) {
                    return new float[] { x, y }; // best case
            }

            return null;
        }

        //! may be sketch
        public static boolean intersectsWithPoint(GraphicsObject object, Vector2f point) {
            float[][] vertices = object.getPoses();
            final float margin = 1E-10f;

            for (int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
                float a = (vertices[i][1] - vertices[j][1]) / (vertices[i][0] - vertices[j][0]);
                float b = vertices[i][1] - a * vertices[j][0];

                if (vertices[i][0] == vertices[j][0] && isBetween((point.y - b)/a, point.x - margin, point.x + margin)) {
                    return true;
                }

                if (isBetween(a * point.x + b, point.y - margin, point.y + margin)) {
                    return true;
                }
            }

            return false;
        }

        public static Vector2f getNormal(GraphicsObject object, Vector2f center, Vector2f point) {
            if (!intersectsWithPoint(object, point)) {
                return null;
            }

            float[][] vertices = object.getPoses();
            final float margin = 1E-10f;

            for (int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
                float a = (vertices[i][1] - vertices[j][1]) / (vertices[i][0] - vertices[j][0]);
                float b = vertices[i][1] - a * vertices[j][0];

                if (vertices[i][0] == vertices[j][0] && isBetween((point.y - b)/a, point.x - margin, point.x + margin)
                     || isBetween(a * point.x + b, point.y - margin, point.y + margin)) {
                    float[] middlePoint = new float[] {(vertices[i][0] - vertices[j][0])/2, (vertices[i][1] - vertices[j][1])/2};
                    boolean yShouldBePositive = false;
                    // normal y component should be positive if the middle point's x component
                    // is positive with the origin at the center and if the slope of the segment is positive
                    if (middlePoint[0] >= center.x)
                        yShouldBePositive = !yShouldBePositive;
                    if (a < 0)
                        yShouldBePositive = !yShouldBePositive;
                    Vector2f normal = createSegmentVector(vertices[i], vertices[j]).perpendicular();
                    if (yShouldBePositive != normal.y < 0) {
                        normal = normal.mul(-1);
                    }

                    return normal;
                }
            }

            return null;
        }
    }

    public static class Circle {
        protected Circle() {
        }

        public static boolean contains(Vector2f point, float[] center, float radius) {
            return contains(new float[] {point.x, point.y}, center, radius);
        }

        public static boolean contains(float[] point, float[] center, float radius) {
            return Math.hypot(point[0]-center[0], point[1]-center[1]) <= radius;
        }

        public static boolean intersects() {
            // TODO
            return false;
        }

        public static boolean intersectsWithPoint(Vector2f point, float[] center, float radius) {
            return intersectsWithPoint(new float[] {point.x, point.y}, center, radius);
        }

        public static boolean intersectsWithPoint(float[] point, float[] center, float radius) {
            final float margin = 1E-10f;
            float distance = (float) Math.hypot(point[0]-center[0], point[1]-center[1]);
            return isBetween(distance, radius - margin, radius + margin);
        }

        public static boolean getIntersectionPoints(GraphicsObject circle1, GraphicsObject circle2) {
            // TODO
            return false;
        }

        /**
         * Will return the normal of circle at a given point
         * @param point
         * @param center
         * @param radius
         * @return
         */
        public static Vector2f getNormal(Vector2f point, float[] center, float radius) {
            if (!intersectsWithPoint(point, center, radius)) {
                return null;
            }

            return new Vector2f(point.x-center[0], point.y-center[1]).normalize();
        }
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

    public static Vector2f normalize(Vector2f vector) {
        if (vector.length() == 0)
            return vector;
        return vector.normalize();
    }

    public static float getAngle(Vector2f vector) {
        if (vector.length() == 0)
            return Float.NaN;
        return (float) Math.atan2(vector.y, vector.x);
    }

    public static Vector2f createAngledVector(float angle, float length) {
        return new Vector2f((float) Math.cos(angle)*length, (float) Math.sin(angle)*length);
    }

    public static Vector2f createSegmentVector(float[] point1, float[] point2) {
        return createAngledVector(new Vector2f(point1[0], point1[1]).angle(new Vector2f(point2[0], point2[1])), Vector2f.distance(point1[0], point1[1], point2[0], point2[1]));
    }
}
