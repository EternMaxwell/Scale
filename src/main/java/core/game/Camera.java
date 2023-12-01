package core.game;

import org.joml.Vector2f;

public class Camera {
    private final Point globalPos;

    /**
     * Creates a camera with the given global point.
     * @param globalPos The global point.
     */
    public Camera(Point globalPos) {
        this.globalPos = globalPos;
    }

    /**
     * get the container of the camera.
     * @return the container.
     */
    public Container container() {
        return globalPos.container();
    }

    /**
     * get the position of the camera.
     * @return the position.
     */
    public Vector2f pos() {
        return globalPos.pos();
    }

    /**
     * set the position of the camera.
     * @param pos The new position.
     */
    public void setPos(Vector2f pos) {
        globalPos.setPos(pos);
    }

    /**
     * set the position of the camera.
     * @param x The x coordinate of the new position.
     * @param y The y coordinate of the new position.
     */
    public void setPos(float x, float y) {
        globalPos.setPos(x, y);
    }

    /**
     * set the container of the camera.
     * @param container The new container.
     */
    public void setContainer(Container container) {
        globalPos.setContainer(container);
    }
}
