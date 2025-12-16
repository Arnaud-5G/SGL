package sure.listeners;

import org.joml.Vector2f;
import org.joml.Vector3f;
import sure.Camera;
import sure.Window;

import java.util.Optional;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;
    private double posX, lastX, posY, lastY;

    private final boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private Camera currentGameCamera;

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

    public static void attachCamera(Camera camera) {
        MouseListener.get().currentGameCamera = camera;
    }

    public static Vector2f getMousePos() {
        return new Vector2f((float) get().posX, (float) get().posY);
    }

    public static Vector3f getMouseGamePos() {
        return MouseListener.get().currentGameCamera.screenToWorld(getMousePos());
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

    public static boolean mouseButtonDown(MouseButton button) {
        return mouseButtonDown(button.getIndex());
    }

    public static boolean mouseButtonDown(int button) {
        if(button > get().mouseButtonPressed.length - 1)
            throw new ArrayIndexOutOfBoundsException("button: " + button + " is not supported by this library");
        return get().mouseButtonPressed[button];
    }
}
