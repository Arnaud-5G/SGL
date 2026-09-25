package sure.basepackage.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import sure.basepackage.Window;
import sure.basepackage.camera.Coordinates.Screen;
import sure.basepackage.camera.Coordinates.World;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;
    public float rotation;

    public Camera(Vector2f position) {
        this(position, 0f);
    }

    public Camera(Vector2f position, float rotation) {
        this.position = position;
        this.rotation = rotation;

        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0f, 32f * 40f, 0f, 32f * 21f, -80f, 21f); // z-index [0, 100]
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0f, 0f, -1f);
        Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20f),
                            cameraFront.add(position.x, position.y, 0f, new Vector3f()),
                            cameraUp);
        viewMatrix.rotateZ((float) Math.toRadians(rotation));
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    /**
     * @param screenPositionX 0-1
     * @param screenPositionY 0-1
     * @return
     */
    public World screenToWorld(float screenPositionX, float screenPositionY) {
        screenPositionX *= Window.get().getActualWidth();
        screenPositionY *= Window.get().getActualHeight();
        Matrix4f matrix = new Matrix4f(getProjectionMatrix()).mul(getViewMatrix());
        Vector3f pos = matrix.unproject(new Vector3f(screenPositionX, screenPositionY, 0), new int[] { 0, 0, Window.get().getWidth(), Window.get().getHeight()}, new Vector3f());
        return new World(pos.x, pos.y);
    }
}
