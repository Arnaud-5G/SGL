package sure.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;
    private double posX, lastX, posY, lastY;

    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        scrollX = 0;
        scrollY = 0;
        posX = 0;
        lastX = 0;
        posY = 0;
        lastY = 0;

        isDragging = false;
    }

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }

        return instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().posX = xPos;
        get().posY = yPos;
        get().isDragging = get().mouseButtonPressed[0] ||
                           get().mouseButtonPressed[1] ||
                           get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= get().mouseButtonPressed.length) {
            return;
        }

        if(action == GLFW_PRESS) {
            get().mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE) {
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }

    public static float getX() {
        return (float)get().posX;
    }

    public static float getY() {
        return (float)get().posY;
    }

    public static float getDx() {
        return (float)(get().lastX - get().posX);
    }

    public static float getDy() {
        return (float)(get().lastY - get().posY);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if(button < get().mouseButtonPressed.length)
            throw new ArrayIndexOutOfBoundsException("button: " + button + " is not supported by this library");
        return get().mouseButtonPressed[button];
    }
}
