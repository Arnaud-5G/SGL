package sure.objects;

import org.joml.Vector3f;
import sure.components.Clickable;
import sure.listeners.MouseListener;
import sure.listeners.MouseListener.*;

public class Button extends Rectangle implements Clickable {
    public Button(float x, float y, float height, float width, int textureSlot) {
        super(x, y, height, width, 1, textureSlot);
    }

    @Override
    public boolean contains(Vector3f pos) {
        if (pos.y - y > height/2 || pos.y - y < -(height/2)) {
            return false;
        }

        if (pos.x - x > width/2 || pos.x - x < -(width/2)) {
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
