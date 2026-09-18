package sure.basepackage.components;

import org.joml.Vector3f;
import sure.basepackage.listeners.MouseListener;

public interface Clickable {
    public boolean contains(Vector3f pos);
    public void clickEvent(MouseListener.MouseButton button);
}
