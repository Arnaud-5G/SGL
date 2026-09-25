package sure.basepackage.objects.ui;

import sure.basepackage.objects.GameObject;
import sure.basepackage.renderers.Texture;
import sure.basepackage.camera.Coordinates.Screen;
import sure.basepackage.components.Clickable;
import sure.basepackage.components.Updating;
import sure.basepackage.listeners.MouseListener;
import sure.basepackage.listeners.MouseListener.MouseButton;
import sure.basepackage.objects.Circle;
import sure.basepackage.objects.Rectangle;
import sure.basepackage.utils.Color;
import sure.basepackage.utils.SureMath;

public class Slider extends GameObject {
    private final Thumb thumb;
    private final Rectangle track;
    private final float min;
    private final float max;

    protected static class Thumb extends Circle implements Clickable, Updating {
        final float minx;
        final float length;
        boolean isDragging = false;

        public Thumb(float x, float y, float radius, int numOfVertices, float zIndex, Texture texture, float length) {
            super(x, y, radius, numOfVertices, zIndex, texture);
            this.minx = x;
            this.length = length;
        }

        @Override
        public boolean contains(Screen pos) {
            return SureMath.ConvexPolygon.contains(pos.toWorld().toVector(), this);
        }

        @Override
        public void clickEvent(MouseButton button) {
            x = Math.clamp(MouseListener.getMousePos().toWorld().x, minx, minx+length);
            isDragging = true;
        }

        @Override
        public void update() {
            isDragging &= MouseListener.isDragging();
            if (isDragging) {
                x = Math.clamp(MouseListener.getMousePos().toWorld().x, minx, minx+length);
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
        thumb = new Thumb(x - length / 2, y, radius, 100, 100, null, length);
        track = new Rectangle(x, y, radius, length, 99, null);
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
