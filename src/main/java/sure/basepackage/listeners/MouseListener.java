package sure.basepackage.listeners;
import sure.basepackage.camera.Coordinates.Screen;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static double scrollX, scrollY;
    private static double posX, lastX, posY, lastY;

    private static final boolean[] mouseButtonPressed = new boolean[3];
    private static boolean isDragging;

    public enum MouseButton {
        LEFT(0),
        MIDDLE(1),
        RIGHT(2);

        private final int index;

        MouseButton(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    static {
        // initializes the MouseListener
        scrollX = 0;
        scrollY = 0;
        posX = 0;
        lastX = 0;
        posY = 0;
        lastY = 0;

        isDragging = false;
    }

    public static void updateListener() {
        lastX = posX;
        lastY = posY;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        lastX = posX;
        lastY = posY;
        posX = xPos;
        posY = yPos;
        isDragging = mouseButtonPressed[0] ||
                           mouseButtonPressed[1] ||
                           mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= mouseButtonPressed.length) {
            return;
        }

        if(action == GLFW_PRESS) {
            mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE) {
            mouseButtonPressed[button] = false;
            isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        scrollX = xOffset;
        scrollY = yOffset;
    }

    /**
     * @return the mouse position in screen space
     */
    public static Screen getMousePos() {
        return Screen.fromWindowSpace((float) posX, (float) posY);
    }

    public static float getX() {
        return (float) posX;
    }

    public static float getY() {
        return (float) posY;
    }

    public static float getDx() {
        return (float)(lastX - posX);
    }

    public static float getDy() {
        return (float)(lastY - posY);
    }

    public static float getScrollX() {
        return (float) scrollX;
    }

    public static float getScrollY() {
        return (float) scrollY;
    }

    public static boolean isDragging() {
        return isDragging;
    }

    /**
     * Checks if the specified {@link MouseButton} is down
     * @param button
     * @return boolean
     */
    public static boolean mouseButtonDown(MouseButton button) {
        return mouseButtonDown(button.getIndex());
    }

    /**
     * Checks if the mouse button at the specified index is down
     * @param button
     * @return boolean
     * @throws ArrayIndexOutOfBoundsException if the index is unsupported
     */
    public static boolean mouseButtonDown(int button) {
        if(button > mouseButtonPressed.length - 1)
            throw new ArrayIndexOutOfBoundsException("button: " + button + " is not supported by this library");
        return mouseButtonPressed[button];
    }
}
