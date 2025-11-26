package sure;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
        projectionMatrix.ortho(0f, 32f * 40f, 0f, 32f * 21f, 0f, 100f);
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

    public Vector3f screenToWorld(Vector2f screenPosition) {
        screenPosition.y = (Window.get().getActualHeight()) - screenPosition.y;
        Matrix4f invMatrix = new Matrix4f(getProjectionMatrix()).mul(viewMatrix);
        Vector3f pos = invMatrix.unproject(new Vector3f(screenPosition, 0), new int[] { 0, 0, Window.get().getActualWidth(), Window.get().getActualHeight()}, new Vector3f());
        return pos;
    }
}
