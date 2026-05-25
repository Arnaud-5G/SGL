package sure.objects;

import org.joml.Vector2f;
import org.joml.Vector3f;
import sure.renderers.Texture;
import sure.standardcomponents.Clickable;
import sure.listeners.MouseListener;
import sure.listeners.MouseListener.*;
import sure.utils.Color;
import sure.utils.SureMath;

public class Button extends Rectangle implements Clickable {
    public Button(float x, float y, float height, float width, Texture texture) {
        super(x, y, height, width, 1, texture);
    }

    @Override
    public boolean contains(Vector3f pos) {
        return SureMath.contains(new Vector2f(pos), this);
    }

    @Override
    public void clickEvent(MouseButton button) {
        if (button == MouseListener.MouseButton.LEFT) {
            color = new Color(1f, 1f, 1f, 1f);
        }
    }
}
