package sure.basepackage.objects.ui;

import sure.basepackage.components.Updating;
import sure.basepackage.utils.Time;

public class FPS extends TextBox implements Updating {
    float timer = 0.1f;

    public FPS(float zIndex) {
        super(25, 500, zIndex); // TODO: lock fps to top-left when window size can be converted to world space
    }

    @Override
    public void update() {
        timer += Time.deltaTime();
        if (timer >= 0.1) {
            this.set((int) Time.FPS() + " FPS\n" + Time.deltaTime()*1000 + " ms");
            timer = 0;
        }
    }
}
