package core.game;

import org.joml.Vector2f;

public class Camera {
    private Container container;
    private Vector2f pos;

    /**
     * Creates a camera with the given container.
     * @param container The container.
     */
    public Camera(Container container) {
        this.container = container;
        pos = new Vector2f();
    }

    /**
     * Creates a camera with the given container and position.
     * @param container The container.
     * @param pos The position.
     */
    public Camera(Container container, Vector2f pos) {
        this.container = container;
        this.pos = pos;
    }

    /**
     * get the container of the camera.
     * @return the container.
     */
    public Container container() {
        return container;
    }

    /**
     * get the position of the camera.
     * @return the position.
     */
    public Vector2f pos() {
        return pos;
    }

    /**
     * set the position of the camera.
     * @param pos The new position.
     */
    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    /**
     * set the position of the camera.
     * @param x The x coordinate of the new position.
     * @param y The y coordinate of the new position.
     */
    public void setPos(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

    /**
     * set the container of the camera.
     * @param container The new container.
     */
    public void setContainer(Container container) {
        this.container = container;
    }
}
