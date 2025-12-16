package sure.objects.ui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import sure.components.Clickable;
import sure.components.Updating;
import sure.listeners.MouseListener;
import sure.objects.Circle;
import sure.objects.Rectangle;
import sure.utils.Color;
import sure.utils.SureMath;

public class Slider {
    private final Thumb thumb;
    private final Rectangle track;
    private final float min;
    private final float max;

    public static class Thumb extends Circle implements Clickable, Updating {
        final float minx;
        final float length;
        boolean isDragging = false;

        public Thumb(float x, float y, float radius, int numOfVertices, float zIndex, int textureSlot, float length) {
            super(x, y, radius, numOfVertices, zIndex, textureSlot);
            this.minx = x;
            this.length = length;
        }

        @Override
        public boolean contains(Vector3f pos) {
            return SureMath.contains(new Vector2f(pos), this);
        }

        @Override
        public void clickEvent(MouseListener.MouseButton button) {
            x = Math.clamp(MouseListener.getMouseGamePos().x, minx, minx+length);
            isDragging = true;
        }

        @Override
        public void update() {
            isDragging &= MouseListener.isDragging();
            if (isDragging) {
                x = Math.clamp(MouseListener.getMouseGamePos().x, minx, minx+length);
            }
        }

        /**
         * @return [0, 1]
         */
        public float getValue() {
            return (x - minx) / length;
        }

        /**
         * @param value [0, 1]
         */
        public void setValue(float value) {
            x = length * value + minx;
        }
    }

    public Slider(float x, float y, float radius, float length, float min, float max) {
        thumb = new Thumb(x - length / 2, y, radius, 100, 100, -1, length);
        track = new Rectangle(x, y, radius, length, 99, -1);
        track.color = new Color(0, 0, 0.5f);
        this.min = min;
        this.max = max;
    }

    public float getValue() {
        return thumb.getValue() * (max - min) + min;
    }

    public void setValue(float value) {
        thumb.setValue((value - min) / (max - min));
    }
}
