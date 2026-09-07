package sure.basepackage.renderers;

import sure.basepackage.utils.Time;

public class Animation {
    public static final float TIME_PER_FRAME = 0.2f;
    public final float timePerFrame;
    public final int[] indexes;

    public float startTime;
    public boolean isStarted = false;

    public Animation(int[] indexes) {
        this(indexes, TIME_PER_FRAME);
    }
    
    public Animation(int[] indexes, float timePerFrame) {
        this.indexes = indexes;
        this.timePerFrame = timePerFrame;
    }

    public int getCurrentIndex() {
        if (isStarted) {
            float dt = Time.getScaledTime() - startTime;
            return indexes[(int) Math.floor(dt/TIME_PER_FRAME) % indexes.length];
        }
        return 0;
    }

    public void start() {
        startTime = Time.getScaledTime();
        isStarted = true;
    }

    public void stop() {
        isStarted = false;
    }
}
