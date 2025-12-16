package sure.objects;

import org.joml.Vector2f;
import org.joml.Vector3f;
import sure.components.Clickable;
import sure.listeners.MouseListener;
import sure.listeners.MouseListener.*;
import sure.utils.SureMath;

import java.awt.geom.Point2D;

public class Button extends Rectangle implements Clickable {
    public Button(float x, float y, float height, float width, int textureSlot) {
        super(x, y, height, width, 1, textureSlot);
    }

    @Override
    public boolean contains(Vector3f pos) {
        return SureMath.contains(new Vector2f(pos), this);
    }

    @Override
    public void clickEvent(MouseButton button) {
        if (button == MouseListener.MouseButton.LEFT) {
            textureSlot = 1;
        }
    }
}
