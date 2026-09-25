package sure.basepackage.components;

import sure.basepackage.listeners.*;
import sure.basepackage.listeners.MouseListener.MouseButton;
import sure.basepackage.Window;

public class StandardComponentBundle extends ComponentBundle {
    public StandardComponentBundle() {
        this.add(Updating.class, StandardComponentBundle::handleUpdatings);
        this.add(Clickable.class, StandardComponentBundle::handleClickables);
        this.add(UsesFocus.class, StandardComponentBundle::handleFocus);
    }

    private static void handleUpdatings(Updating... objects) {
        for (Updating updating : objects) {
            updating.update();
        }
    }

    private static void handleClickables(Clickable... objects) {
        if (!MouseListener.mouseButtonDown(MouseButton.LEFT) && !MouseListener.mouseButtonDown(MouseButton.RIGHT)) {
            return;
        }

        for (Clickable clickable : objects) {
            if (!(clickable.contains(MouseListener.getMousePos()))) {
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

    private static UsesFocus focusedObject;
    private static void handleFocus(UsesFocus... objects) {
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
