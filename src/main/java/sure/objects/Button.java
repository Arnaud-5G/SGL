package sure.objects;

import org.joml.Vector3f;
import sure.components.Clickable;
import sure.listeners.MouseListener;
import sure.listeners.MouseListener.*;

public class Button extends GraphicsObject implements Clickable {
    public Button(float x, float y) {
        super(x, y);
    }

    @Override
    public boolean contains(Vector3f pos) {
        if (pos.y - height < 0 || pos.y - height > height) {
            return false;
        }

        if (pos.x - width < 0 || pos.x - width > width) {
            return false;
        }

        return true;
    }

    @Override
    public void clickEvent(MouseButton button) {
        if (button == MouseListener.MouseButton.LEFT) {
            textureSlot = 1;
        }
    }
}
