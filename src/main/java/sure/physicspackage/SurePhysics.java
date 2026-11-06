package sure.physicspackage;

import org.joml.Vector2f;

import sure.basepackage.objects.GraphicsObject;
import sure.basepackage.utils.SureMath;
import sure.basepackage.utils.Time;
import sure.physicspackage.components.Colliding;

public class SurePhysics extends SureMath {
    private SurePhysics() {}

    public static class ConvexPolygon extends SureMath.ConvexPolygon {
        // TODO: collisions rectangle -> rectangle do not work
        // TODO: add extra push when inside an object to not make it stick inside
        public static Vector2f getCollisionNormal(Colliding object, float[] centroid, Colliding colliding) {
            Vector2f normal = new Vector2f();
            if (!(colliding instanceof GraphicsObject) || !(object instanceof GraphicsObject)) {
                return new Vector2f();
            }

            GraphicsObject collidingObject = (GraphicsObject) colliding;
            GraphicsObject objectGraphics = (GraphicsObject) object;

            for (int i = 0; i < objectGraphics.getPoses().length; i++) {
                float[] point = objectGraphics.getPoses()[i];
                if (colliding.contains(point)) {
                    Vector2f subNormal;

                    float[][] segment1 = new float[][] {
                            objectGraphics.getPoses()[i - 1 < 0 ? objectGraphics.getPoses().length - 1 : i - 1], point };
                    float[][] segment2 = new float[][] { point,
                            objectGraphics.getPoses()[i + 1 >= objectGraphics.getPoses().length ? 0 : i + 1] };

                    float[][] intersectionPoints = new float[2][2];
                    int m = 0;

                    // check for where the segments touch the object
                    for (int k = 0, j = collidingObject.getPoses().length - 1; k < collidingObject.getPoses().length; j = k++) {
                        intersectionPoints[m] = segmentsIntersectionPoint((m == 0 ? segment1 : segment2),
                                new float[][] { collidingObject.getPoses()[j], collidingObject.getPoses()[k] });
                        System.out.println(intersectionPoints[m]);
                        if (intersectionPoints[m] != null) {
                            if (m >= 1) {
                                break;
                            }
                            m++;
                        }
                    }

                    float deltaX = 0;
                    float deltaY = 0;
                    if (intersectionPoints[0] != null && intersectionPoints[1] != null) {
                        deltaX = intersectionPoints[0][0] - intersectionPoints[1][0];
                        deltaY = intersectionPoints[0][1] - intersectionPoints[1][1];
                    }

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
    }

    public static class Circle extends SureMath.Circle {
        public static Vector2f getCollisionNormal(GraphicsObject circle, float[] center, Colliding object) {
            Vector2f normal = new Vector2f();
            for (float[] point : circle.getPoses()) {
                if (object.contains(point)) {
                    Vector2f subNormal = new Vector2f(center[0] - point[0], center[1] - point[1]);
                    normal.add(subNormal);
                }
            }

            return SureMath.normalize(normal);
        }

        public static double calculateAngularAccel(GraphicsObject circle, float[] center, double momentOfInertia, Colliding object) {
            // TODO

            return 0;
        }
    }

    
    // TODO: add a way to get the amount of speed lost
    public static Vector2f getSpeedAfterCollision(Vector2f speed, Vector2f normal, float bouncePercent) {
        if (normal.length() == 0 || speed.length() == 0) {
            return new Vector2f(speed);
        }

        float normalAngle = getAngle(normal);
        float speedAngle = getAngle(speed);
        float deltaAngle = 0 - normalAngle;

        // turn the speed vector so that the normal is along an axis
        Vector2f projectedSpeed = new Vector2f((float) Math.cos(speedAngle + deltaAngle)*speed.length(), (float) Math.sin(speedAngle + deltaAngle)*speed.length());

        if (projectedSpeed.x < 0) {
            projectedSpeed.x = 0;
        }

        float projectedAngle = getAngle(projectedSpeed);
        Vector2f newSpeed = createAngledVector(projectedAngle - deltaAngle, projectedSpeed.length());
        newSpeed.add(new Vector2f(normal).normalize().mul((speed.length() - newSpeed.length()) * bouncePercent));
        
        return newSpeed;
    }

    // TODO: fix problem when object is stationary
    public static float calculateImpactStrength(Vector2f speed, Vector2f normal) {
        if (normal.length() == 0 || speed.length() == 0) {
            return 0;
        }

        float normalAngle = getAngle(normal);
        float speedAngle = getAngle(speed);
        float deltaAngle = 0 - normalAngle;

        // turn the speed vector so that the normal is along an axis
        Vector2f projectedSpeed = new Vector2f((float) Math.cos(speedAngle + deltaAngle)*speed.length(), (float) Math.sin(speedAngle + deltaAngle)*speed.length());

        if (projectedSpeed.x < 0) {
            projectedSpeed.x = 0;
        }
        
        return speed.length() - projectedSpeed.length();
    }

    public static Vector2f applyFriction(Vector2f force, Vector2f speed, Vector2f normal, float frictionPercent) {
        if (normal.length() == 0 || speed.length() == 0) {
            return new Vector2f(force);
        }
        
        float frictionMagnitude = normal.length() * frictionPercent;
        Vector2f friction = createAngledVector(-getAngle(speed), -frictionMagnitude);
        
        return force.add(friction);
    }

    public static Vector2f calculateSpeed(Vector2f force, Vector2f originalSpeed, Vector2f normal, float bouncePercent, float frictionPercent) {
        Vector2f speed = getSpeedAfterCollision(originalSpeed, normal, bouncePercent);
        force = applyFriction(force, speed, normal, frictionPercent);
        speed.x += force.x * Time.scaledDeltaTime();
        speed.y += force.y * Time.scaledDeltaTime();
        return speed;
    }
}
