package sure.listeners;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    public enum KeyState {
        PRESSED,
        DOWN,
        RELEASED,
        UP,
    }
    private static KeyState[] keys = new KeyState[350];

    static {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = KeyState.UP;
        }
    }

    private KeyListener() {}

    public static void updateListener() {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == KeyState.PRESSED) {
                keys[i] = KeyState.DOWN;
            } else if (keys[i] == KeyState.RELEASED) {
                keys[i] = KeyState.UP;
            }
        }
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            keys[key] = KeyState.PRESSED;
        } else if (action == GLFW_RELEASE) {
            keys[key] = KeyState.RELEASED;
        }
    }

    public static KeyState getKeyState(int key) {
        if (key >= keys.length)
            throw new ArrayIndexOutOfBoundsException("key: " + key + " is not supported by this library");
        return keys[key];
    }

    public static boolean[] getDownKeys() {
        boolean[] down = new boolean[keys.length];
        for (int i = 0; i < keys.length; i++) {
            down[i] = keys[i] == KeyState.DOWN || keys[i] == KeyState.PRESSED;
        }

        return down;
    }

    public static boolean[] getPressedKeys() {
        boolean[] pressed = new boolean[keys.length];
        for (int i = 0; i < keys.length; i++) {
            pressed[i] = keys[i] == KeyState.PRESSED;
        }

        return pressed;
    }
}
