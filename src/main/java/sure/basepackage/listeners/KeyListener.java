package sure.basepackage.listeners;

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
        // fills the array to all UP
        for (int i = 0; i < keys.length; i++) {
            keys[i] = KeyState.UP;
        }
    }

    private KeyListener() {}

    /**
     * Updates the state of keys
     */
    public static void updateListener() {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == KeyState.PRESSED) {
                keys[i] = KeyState.DOWN;
            } else if (keys[i] == KeyState.RELEASED) {
                keys[i] = KeyState.UP;
            }
        }
    }

    /**
     * Callback function called each time a key state is modified.
     * Should only be called by internal game logic.
     * @param window   the window that received the event as an id
     * @param key      the keyboard key that was pressed or released
     * @param scancode the platform-specific scancode of the key
     * @param action   the key action. One of:<br><table><tr><td>{@link KeyState#PRESSED PRESS}</td><td>{@link KeyState#RELEASED RELEASE}</td><td>{@link KeyState#DOWN DOWN}</td><td>{@link KeyState#UP UP}</td></tr></table>
     * @param mods     bitfield describing which modifiers keys were held down (ctrl, shift, etc.)
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == -1) {
            System.out.println("How the hell?");
            return;
        }

        if (action == GLFW_PRESS) {
            keys[key] = KeyState.PRESSED;
        } else if (action == GLFW_RELEASE) {
            keys[key] = KeyState.RELEASED;
        }
    }

    /**
     * @param key
     * @return the state of the key
     * @throws ArrayIndexOutOfBoundsException if the key is invalid
     */
    public static KeyState getKeyState(int key) {
        if (key >= keys.length)
            throw new ArrayIndexOutOfBoundsException("key: " + key + " is not supported by this library");
        return keys[key];
    }

    /**
     * @return the full list of all keys that are down this frame
     */
    public static boolean[] getDownKeys() {
        boolean[] down = new boolean[keys.length];
        for (int i = 0; i < keys.length; i++) {
            down[i] = keys[i] == KeyState.DOWN || keys[i] == KeyState.PRESSED;
        }

        return down;
    }

    /**
     * @return the full list of all keys that were up last frame and down this frame
     */
    public static boolean[] getPressedKeys() {
        boolean[] pressed = new boolean[keys.length];
        for (int i = 0; i < keys.length; i++) {
            pressed[i] = keys[i] == KeyState.PRESSED;
        }

        return pressed;
    }
}
