package sure.objects.ui;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import sure.listeners.KeyListener;
import sure.listeners.MouseListener;
import sure.objects.Rectangle;
import sure.renderers.Sprites.SpriteSheet;
import sure.standardcomponents.Clickable;
import sure.standardcomponents.UsesFocus;
import sure.utils.Assets;
import sure.utils.Color;

public class TextField extends TextBox implements Clickable, UsesFocus {
    final Rectangle cursor;
    int cursorIndex = 0;
    int lastFieldSize = 0;
    final float CHAR_TO_CURSOR_WIDTH = 1f/10f;

    public TextField(float x, float y, float zIndex) {
        this(Assets.getDefaultFont(), x, y, zIndex);
    }

    public TextField(SpriteSheet font, float x, float y, float zIndex) {
        super(font, x, y, zIndex);
        cursor = new Rectangle(x, y, HEIGHT*scale, WIDTH*scale*CHAR_TO_CURSOR_WIDTH, zIndex+1, null);
        cursor.color = Color.TRANSPARENT;
    }

    @Override
    public void scale(float scale) {
        super.scale(scale);
        cursor.height = HEIGHT*scale;
        cursor.width = WIDTH*scale*CHAR_TO_CURSOR_WIDTH;
    }

    public void moveCursor(int index) {
        cursorIndex = Math.clamp(index, 0, characters.size());

        if (!characters.isEmpty() && cursorIndex > 0) {
            cursor.x = characters.get(cursorIndex-1).x+WIDTH*scale/2;
            cursor.y = characters.get(cursorIndex-1).y;
        } else {
            cursor.x = x + WIDTH*scale*CHAR_TO_CURSOR_WIDTH/2;
            cursor.y = y + WIDTH*scale/2;
        }

        lastFieldSize = characters.size();
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
    public boolean shouldBeFocused() {
        boolean temp = shouldFocus;
        shouldFocus = false;
        if(temp) { // TODO: remove sketchyness later
            moveCursor(characters.size());
        }
        return temp;
    }

    private boolean shouldRemoveFocus = false;
    @Override
    public boolean shouldNotBeFocused() {
        boolean temp = shouldRemoveFocus;
        shouldRemoveFocus = false;
        cursor.color = Color.TRANSPARENT;
        return temp;
    }


    @Override
    public void isFocused() {
        cursor.color = Color.BLACK;

        for(int i = 0; i < KeyListener.getPressedKeys().length; i++) {
            if(!KeyListener.getPressedKeys()[i]) continue;

            switch (i) {
                case GLFW_KEY_BACKSPACE -> {
                    backspace(cursorIndex);
                    continue;
                }
                case GLFW_KEY_DELETE -> {
                    delete();
                    continue;
                }
                case GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT -> {
                    continue;
                }
                case GLFW_KEY_TAB -> {
                    tab();
                    continue;
                }
                case GLFW_KEY_ENTER -> {
                    newline(cursorIndex);
                    continue;
                }
                case GLFW_KEY_ESCAPE -> {
                    shouldRemoveFocus = true;
                    continue;
                }
                case GLFW_KEY_RIGHT -> {
                    moveCursor(cursorIndex + 1);
                    continue;
                }
                case GLFW_KEY_LEFT -> {
                    moveCursor(cursorIndex - 1);
                    continue;
                }
                case GLFW_KEY_LEFT_CONTROL, GLFW_KEY_RIGHT_CONTROL, GLFW_KEY_CAPS_LOCK, GLFW_KEY_LEFT_ALT, GLFW_KEY_RIGHT_ALT, GLFW_KEY_HOME, GLFW_KEY_PAGE_DOWN, GLFW_KEY_PAGE_UP, GLFW_KEY_END, GLFW_KEY_DOWN, GLFW_KEY_UP, GLFW_KEY_F1, GLFW_KEY_F2, GLFW_KEY_F3, GLFW_KEY_F4, GLFW_KEY_F5, GLFW_KEY_F6, GLFW_KEY_F7, GLFW_KEY_F8, GLFW_KEY_F9, GLFW_KEY_F10, GLFW_KEY_F11, GLFW_KEY_F12, GLFW_KEY_F13, GLFW_KEY_F14, GLFW_KEY_F15, GLFW_KEY_F16, GLFW_KEY_F17, GLFW_KEY_F18, GLFW_KEY_F19, GLFW_KEY_F20, GLFW_KEY_F21, GLFW_KEY_F22, GLFW_KEY_F23, GLFW_KEY_F24 -> {
                    continue;
                }
            }

            moveCursor(cursorIndex + (characters.size()) - lastFieldSize);
            write((char) i, cursorIndex);
        }

        moveCursor(cursorIndex + (characters.size()) - lastFieldSize);
    }
}
