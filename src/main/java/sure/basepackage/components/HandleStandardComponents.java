package sure.basepackage.components;

import sure.basepackage.listeners.*;
import sure.basepackage.listeners.MouseListener.MouseButton;
import sure.basepackage.Window;

public class HandleStandardComponents extends HandleComponents {
    @Override 
    public void initializeComponents() {
        addComponent(Clickable.class, this::handleClickables);
        addComponent(Updating.class, this::handleUpdatings);
        addComponent(UsesFocus.class, this::handleFocus);
    }

    private void handleUpdatings(Updating... objects) {
        for (Updating updating : objects) {
            updating.update();
        }
    }

    private void handleClickables(Clickable... objects) {
        if (!MouseListener.mouseButtonDown(MouseButton.LEFT) && !MouseListener.mouseButtonDown(MouseButton.RIGHT)) {
            return;
        }

        for (Clickable clickable : objects) {
            if (!(clickable.contains(Window.get().getRunningGame().getCamera().screenToWorld(MouseListener.getMousePos())))) {
                continue;
            }

            if (MouseListener.mouseButtonDown(MouseButton.LEFT)) {
                clickable.clickEvent(MouseButton.LEFT);
            }

            if (MouseListener.mouseButtonDown(MouseButton.RIGHT)) {
                clickable.clickEvent(MouseButton.RIGHT);
            }
        }
    }

    private UsesFocus focusedObject;
    private void handleFocus(UsesFocus... objects) {
        for (UsesFocus usesFocus : objects) {
            if (usesFocus.shouldBeFocused() == true) {
                focusedObject = usesFocus;
            }

            if (usesFocus.shouldNotBeFocused() == true && focusedObject != null && focusedObject.equals(usesFocus)) {
                focusedObject = null;
            }
        }

        if (focusedObject != null) {
            focusedObject.isFocused();
        }
    }
}
