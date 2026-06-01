package sure.objects.ui;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import sure.listeners.KeyListener;
import sure.listeners.MouseListener;
import sure.renderers.Sprites.SpriteSheet;
import sure.standardcomponents.Clickable;
import sure.standardcomponents.UsesFocus;

public class TextField extends TextBox implements Clickable, UsesFocus {
    public TextField(float x, float y, float zIndex) {
        super(x, y, zIndex);
    }

    public TextField(SpriteSheet font, float x, float y, float zIndex) {
        super(font, x, y, zIndex);
    }

    @Override
    public boolean contains(Vector3f pos) {
        if  (pos.x < x || pos.x > x + (WIDTH*scale)/2 + characters.size()*WIDTH*scale ||
            (pos.y < y || pos.y > y + (HEIGHT*scale)/2 + HEIGHT*scale)) {
            if (MouseListener.mouseButtonDown(MouseListener.MouseButton.LEFT)) {
                shouldRemoveFocus = true;
            }
            return false;
        }

        return true;
    }

    @Override
    public void clickEvent(MouseListener.MouseButton button) {
        if (button == MouseListener.MouseButton.LEFT) {
            shouldFocus = true;
        }
    }

    private boolean shouldFocus = false;
    @Override
    public boolean setFocus() {
        boolean temp = shouldFocus;
        shouldFocus = false;
        return temp;
    }

    private boolean shouldRemoveFocus = false;
    @Override
    public boolean removeFocus() {
        boolean temp = shouldRemoveFocus;
        shouldRemoveFocus = false;
        return temp;
    }

    @Override
    public void isFocused() {
        for(int i = 0; i < KeyListener.getPressedKeys().length; i++) {
            if(!KeyListener.getPressedKeys()[i]) continue;

            switch (i) {
                case GLFW_KEY_BACKSPACE -> {
                    backspace();
                    continue;
                }
                case GLFW_KEY_DELETE -> {
                    delete();
                    continue;
                }
                case GLFW_KEY_ENTER -> {
                    continue;
                }
                case GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT -> {
                    continue;
                }
                case GLFW_KEY_TAB -> {
                    continue;
                }
                case GLFW_KEY_ESCAPE -> {
                    shouldRemoveFocus = true;
                    continue;
                }
                case GLFW_KEY_LEFT_CONTROL, GLFW_KEY_RIGHT_CONTROL, GLFW_KEY_CAPS_LOCK, GLFW_KEY_LEFT_ALT, GLFW_KEY_RIGHT_ALT, GLFW_KEY_HOME, GLFW_KEY_PAGE_DOWN, GLFW_KEY_PAGE_UP, GLFW_KEY_END, GLFW_KEY_DOWN, GLFW_KEY_UP, GLFW_KEY_LEFT, GLFW_KEY_RIGHT, GLFW_KEY_F1, GLFW_KEY_F2, GLFW_KEY_F3, GLFW_KEY_F4, GLFW_KEY_F5, GLFW_KEY_F6, GLFW_KEY_F7, GLFW_KEY_F8, GLFW_KEY_F9, GLFW_KEY_F10, GLFW_KEY_F11, GLFW_KEY_F12, GLFW_KEY_F13, GLFW_KEY_F14, GLFW_KEY_F15, GLFW_KEY_F16, GLFW_KEY_F17, GLFW_KEY_F18, GLFW_KEY_F19, GLFW_KEY_F20, GLFW_KEY_F21, GLFW_KEY_F22, GLFW_KEY_F23, GLFW_KEY_F24 -> {
                    continue;
                }
            }

            write((char) i);
        }
    }
}
