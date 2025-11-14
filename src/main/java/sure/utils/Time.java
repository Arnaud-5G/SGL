package sure.utils;

public class Time {
    public static long timeStarted = System.nanoTime();
    public static long currentTime = timeStarted;
    public static long lastTime = currentTime;

    private Time(){}

    public static void timePassedCall() {
        lastTime = currentTime;
        currentTime = System.nanoTime();
    }

    /**
     * Will restart the timer so that the {@code getTime()} function will return time from this moment
     */
    public static void resetStartTime() {
        timeStarted = currentTime;
    }

    /**
     * @return time since start in seconds
     */
    public static float getTime() {
        return (currentTime - timeStarted) * 1E-9f;
    }

    /**
     * @return deltaTime in seconds
     */
    public static float deltaTime() {
        return (currentTime - lastTime) * 1E-9f;
    }

    public static float FPS() {
        return 1/deltaTime();
    }
}
