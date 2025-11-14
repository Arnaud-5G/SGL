package sure;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import sure.listeners.KeyListener;
import sure.listeners.MouseListener;
import sure.scenes.*;
import sure.utils.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window window;

    private int width, height;
    private String title;

    private long glfwWindow;

    private static int currentSceneIndex = -1;
    private static Scene currentScene;


    // fun vars
    public float r = 1;
    public float g = 1;
    public float b = 1;

    private Window() {
        width = 1920;
        height = 1080;
        title = "PhysicsSim";
    }

    public static Window get() {
        if (window == null) {
            window = new Window();
        }

        return window;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentSceneIndex = 0;
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentSceneIndex = 1;
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + newScene);
        }
    }

    public void run() {
        System.out.println("Hello LWJGL " + Runtime.version() + "!");

        init();
        loop();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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

        changeScene(0);
    }
    public void loop() {
        while(!glfwWindowShouldClose(glfwWindow)) {
            Time.timePassedCall();

            glfwPollEvents();

            glClearColor(r, g, b, 1f); // set color buffer to red
            glClear(GL_COLOR_BUFFER_BIT); // set screen to color buffer

            currentScene.update();

            glfwSwapBuffers(glfwWindow);
            System.out.println(Time.FPS());
        }
    }
}
