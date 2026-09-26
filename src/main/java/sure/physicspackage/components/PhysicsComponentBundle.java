package sure.physicspackage.components;

import org.joml.Vector2f;

import sure.basepackage.components.ComponentBundle;
import sure.basepackage.objects.GraphicsObject;
import sure.basepackage.utils.SureMath;
import sure.basepackage.*;

public class PhysicsComponentBundle extends ComponentBundle {
    public PhysicsComponentBundle() {
        this.add(Colliding.class, PhysicsComponentBundle::handleColliding);
        this.add(UsesPhysics.class, PhysicsComponentBundle::handleUsesPhysics);
    }
    
    public static void handleColliding(Colliding... objects) {
        for (Colliding colliding : objects) {
            if (colliding instanceof UsesPhysics || !(colliding instanceof GraphicsObject)) {
                continue;
            }
        }
    }

    public static void handleUsesPhysics(UsesPhysics... objects) {
        for (UsesPhysics physics : objects) {
            if (!(physics instanceof GraphicsObject)) {
                continue;
            }

            GraphicsObject object = (GraphicsObject) physics;
            // GraphicsObject futureObject = object;
            Vector2f forces = physics.calculateForces();
            Vector2f finalNormal = new Vector2f();

            for (Colliding otherObject : Window.get().getRunningGame().getGameObjects(Colliding.class)) {
                if (otherObject.equals((Colliding) physics)) {
                    continue;
                }

                Vector2f normal = otherObject.getCollisionNormal(physics);
                if (normal.length() == 0) {
                    normal = physics.getCollisionNormal(otherObject);
                }
                System.out.println(normal);
                finalNormal.add(normal);
            }

            SureMath.normalize(finalNormal);

            finalNormal.mul(forces.length());
            forces.add(finalNormal);

            physics.moveObject(forces, finalNormal);
        }
    }
}
