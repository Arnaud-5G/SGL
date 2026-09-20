package sure.physicspackage.components;

import org.joml.Vector2f;

import sure.basepackage.components.HandleStandardComponents;
import sure.basepackage.objects.GraphicsObject;
import sure.basepackage.*;

public class HandlePhysicsComponents extends HandleStandardComponents {
    @Override
    public void initializeComponents() {
        super.initializeComponents();
        addComponent(Colliding.class, this::handleColliding);
        addComponent(UsesPhysics.class, this::handleUsesPhysics);
    }
    
    public void handleColliding(Colliding... objects) {
        for (Colliding colliding : objects) {
            if (colliding instanceof UsesPhysics || !(colliding instanceof GraphicsObject)) {
                continue;
            }
        }
    }

    public void handleUsesPhysics(UsesPhysics... objects) {
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
                finalNormal.add(normal);
            }

            if (finalNormal.length() != 0) {
                finalNormal.normalize();
            }

            finalNormal.mul(forces.length());
            System.out.println("final normal: " + finalNormal.toString());
            forces.add(finalNormal);

            physics.moveObject(forces);
        }
    }
}
