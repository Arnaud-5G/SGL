package sure.basepackage.components;

import sure.basepackage.camera.Coordinates.Screen;
import sure.basepackage.listeners.MouseListener;

public interface Clickable {
    /**
     * Checks if a screen space position can be contained in this object
     * @param pos - screen space
     * @return boolean
     */
    public boolean contains(Screen pos);

    /**
     * Will be called if the mouse is contained in the object and a mouse button is clicked
     * @param button
     */
    public void clickEvent(MouseListener.MouseButton button);
}
