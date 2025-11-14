package sure.utils;

public class Time {
    private static long timeStarted = System.nanoTime();
    private static long currentTime = timeStarted;
    private static long lastTime = currentTime;

    private static long scaledTimePassed = 0;

    public static float scale = 1f;

    private Time(){}

    public static void timePassedCall() {
        lastTime = currentTime;
        currentTime = System.nanoTime();
        scaledTimePassed += (long) ((currentTime - lastTime) * scale);
    }

    /**
     * Will make time go faster or shorter depending on the {@code scale}
     */
    public static void scale(float scale) {
        Time.scale = scale;
    }

    /**
     * Will restart the timer so that the {@code getTime()} function will return time from this moment
     */
    public static void resetStartTime() {
        timeStarted = currentTime;
        scaledTimePassed = 0;
    }

    /**
     * @return time since start in seconds (not scaled)
     */
    public static float getTime() {
        return (timeStarted - currentTime) * 1E-9f;
    }

    /**
     * @return time since start in seconds (scaled)
     */
    public static float getScaledTime() {
        return scaledTimePassed * 1E-9f;
    }

    /**
     * @return deltaTime in seconds (not scaled)
     */
    public static float deltaTime() {
        return (currentTime - lastTime) * 1E-9f;
    }

    /**
     * @return deltaTime in seconds (scaled)
     */
    public static float scaledDeltaTime() {
        return (currentTime - lastTime) * scale * 1E-9f;
    }

    /**
     * @return the fps the game is running in (not scaled)
     */
    public static float FPS() {
        return 1/deltaTime();
    }
}
