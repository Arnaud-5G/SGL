package sure.physicspackage.components;

import sure.basepackage.components.HandleComponents;
import sure.basepackage.objects.GraphicsObject;

public class HandlePhysicsComponents extends HandleComponents {
    @Override
    public void initializeComponents() {
        addComponent(Colliding.class, this::handleColliding);
        addComponent(UsesPhysics.class, this::handleUsesPhysics);
    }
    
    public void handleColliding(Colliding... object) {
        for (Colliding colliding : object) {
            if (colliding instanceof UsesPhysics || !(colliding instanceof GraphicsObject)) {
                continue;
            }
        }
    }

    public void handleUsesPhysics(UsesPhysics... object) {

    }
}
