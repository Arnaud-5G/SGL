package sure.basepackage.components;

import sure.basepackage.camera.Coordinates.Screen;
import sure.basepackage.listeners.MouseListener;

public interface Clickable {
    public boolean contains(Screen pos);
    public void clickEvent(MouseListener.MouseButton button);
}
