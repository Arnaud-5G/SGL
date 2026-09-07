package sure.basepackage.objects;

import org.joml.Vector2f;
import org.joml.Vector3f;
import sure.basepackage.renderers.Texture;
import sure.basepackage.standardcomponents.Clickable;
import sure.basepackage.listeners.MouseListener;
import sure.basepackage.listeners.MouseListener.*;
import sure.basepackage.utils.SureMath;

public class Button extends Rectangle implements Clickable {
    Runnable onClick;
    public Button(float x, float y, float height, float width, Texture texture, Runnable onClick) {
        super(x, y, height, width, 1, texture);
        this.onClick = onClick;
    }

    @Override
    public boolean contains(Vector3f pos) {
        return SureMath.contains(new Vector2f(pos), this);
    }

    @Override
    public void clickEvent(MouseButton button) {
        if (button == MouseListener.MouseButton.LEFT) {
            onClick.run();
        }
    }
}
