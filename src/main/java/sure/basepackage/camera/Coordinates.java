package sure.basepackage.camera;

import org.joml.Vector2f;

import sure.basepackage.Window;

public abstract class Coordinates {
    public float x;
    public float y;

    public Vector2f toVector() {
        return new Vector2f(x, y);
    }

    public abstract World toWorld();
    public abstract Screen toScreen();

    /**
     * Screen space coordinates from -1 to 1 in both axes.
     */
    public static class Screen extends Coordinates {
        public Screen() {
            this(0, 0);
        }

        public Screen(Screen coords) {
            this(coords.x, coords.y);
        }

        public Screen(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public static Screen coords(Screen coords) {
            return new Screen(coords);
        }

        public static Screen coords(float x, float y) {
            return new Screen(x, y);
        }

        public static Screen origin() {
            return new Screen();
        }

        public static Screen fromWindowSpace(float x, float y) {
            return new Screen(x / Window.get().getActualWidth()*2 - 1, (y / Window.get().getActualHeight()*2 - 1)*-1);
        }

        @Override
        public World toWorld() {
            float tempX = (this.x + 1) / 2f;
            float tempY = (this.y + 1) / 2f;
            return Window.get().getRunningGame().getCamera().screenToWorld(tempX, tempY);
        }

        @Override
        public Screen toScreen() {
            return new Screen(x, y);
        }
    }

    /**
     * World space coordinates do not have bounds.
     */
    public static class World extends Coordinates {
        public World() {
            this(0, 0);
        }

        public World(World coords) {
            this(coords.x, coords.y);
        }

        public World(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public static World coords(World coords) {
            return new World(coords);
        }

        public static World coords(float x, float y) {
            return new World(x, y);
        }

        public static World origin() {
            return new World();
        }

        @Override
        public World toWorld() {
            return new World(x, y);
        }

        // TODO: implement
        @Override 
        public Screen toScreen() {
            return null;
        }
    }
}
