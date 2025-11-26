package sure;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import sure.listeners.KeyListener;
import sure.listeners.MouseListener;
import sure.utils.Time;

import java.awt.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window window;

    private int width, height;
    private String title;

    private long glfwWindow;

    private Game game;

    private Window() {
        width = 1920;
        height = 1080;
        title = "SGL Test";
    }

    public static Window get() {
        if (window == null) {
            window = new Window();
        }

        return window;
    }

    /**
     * @return the window's intended width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the window's actual width in pixels
     */
    public int getActualWidth() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetFramebufferSize(glfwWindow, width, height);
        return width[0];
    }

    /**
     * @return the window's intended height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the window's actual height in pixels
     */
    public int getActualHeight() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetFramebufferSize(glfwWindow, width, height);
        return height[0];
    }

    public void run(Game game) {
        this.game = game;

        init();
        loop();

        close();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // setup listeners
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1); // v-sync
        glfwShowWindow(glfwWindow);

        GL.createCapabilities(); // enable opengl

        game.init();
    }

    public void loop() {
        while(!glfwWindowShouldClose(glfwWindow)) {
            Time.timePassedCall();

            glfwPollEvents();

            glEnable(GL_DEPTH_TEST);
            glClearColor(1f, 1f, 1f, 1f); // set color buffer to white
            glClear(GL_COLOR_BUFFER_BIT); // set screen to color buffer

            game.update();

            glClear(GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }

    public void close() {
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
