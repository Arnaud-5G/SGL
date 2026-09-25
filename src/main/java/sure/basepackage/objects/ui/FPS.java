package sure.basepackage.objects.ui;

import sure.basepackage.camera.Coordinates.Screen;
import sure.basepackage.components.Updating;
import sure.basepackage.utils.Time;

/**
 * {@link FPS} is an object that will show your games FPS and ms per frame in the top left corner of your screen
 */
public class FPS extends TextBox implements Updating {
    float timer = 0.1f;

    public FPS(float zIndex) {
        super(Screen.coords(-0.9f, 0.8f).toWorld().x, Screen.coords(-0.9f, 0.8f).toWorld().y, zIndex); // TODO: lock fps to top-left when window size can be converted to world space
    }

    @Override
    public void update() {
        timer += Time.deltaTime();
        if (timer >= 0.1) {
            this.set((int) Time.FPS() + " FPS\n" + Time.deltaTime()*1000 + " ms");
            timer = 0;
        }
        x = Screen.coords(-0.9f, 0.8f).toWorld().x;
        y = Screen.coords(-0.9f, 0.8f).toWorld().y;
    }
}
