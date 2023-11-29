package core.game;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Point {
    private Container container;
    private Container lastContainer;
    private Vector2f pos;
    private Vector2f last;

    /**
     * Creates a point with the given container.
     *
     * @param container The container.
     */
    public Point(Container container) {
        this.container = container;
        pos = new Vector2f();
    }

    /**
     * Creates a point with the given container and position.
     *
     * @param container The container.
     * @param pos       The position.
     */
    public Point(Container container, Vector2f pos) {
        this.container = container;
        this.pos = pos;
    }

    /**
     * get the container of the point.
     *
     * @return the container.
     */
    public Container container() {
        return container;
    }

    /**
     * get the position of the point.
     *
     * @return the position.
     */
    public Vector2f pos() {
        return pos;
    }

    /**
     * get the container of the last position.
     *
     * @return the container of the last position.
     */
    public Container containerLast() {
        return lastContainer;
    }

    /**
     * get the last position.
     *
     * @return the last position.
     */
    public Vector2f positionLast() {
        return last;
    }

    /**
     * set the position of the point.
     *
     * @param pos The new position.
     */
    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    /**
     * set the position of the point.
     *
     * @param x The x coordinate of the new position.
     * @param y The y coordinate of the new position.
     */
    public void setPos(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

    /**
     * set the container of the point.
     *
     * @param container The new container.
     */
    public void setContainer(Container container) {
        this.container = container;
    }

    /**
     * move the point by the given delta.
     * <p>It will loop until the next position is in a container or it goes to where their is no container.</p>
     *
     * @param delta The delta.
     * @return true if the entity moved, false if the next position is not in any container.
     * <p>But it will still move to the last iterated container.</p>
     */
    public boolean move(Vector2f delta) {
        lastContainer = container;
        last = pos;
        Vector2f newPos = pos.add(delta, new Vector2f());
        int[] deltaContainerIndex = new int[]{-1};
        while (!container.containsDelta(pos, newPos, deltaContainerIndex, pos, newPos, null)) {
            if (deltaContainerIndex[0] == -1 || container.nearby(deltaContainerIndex[0]) == null) {
                pos = newPos;
                return false;
            }
            container.transformDir(delta, deltaContainerIndex[0], null);
            container = container.nearby(deltaContainerIndex[0]);
        }
        pos = newPos;
        return true;
    }

    /**
     * move the point by the given delta.
     * <p>This method will loop no more than the given maxIterations.</p>
     *
     * @param delta         The delta.
     * @param maxIterations The maximum number of iterations.
     * @return true if the entity moved, false if the next position is not in any container after the iterations.
     * <p>But it will still move to the last iterated container.</p>
     */
    public boolean move(Vector2f delta, int maxIterations) {
        lastContainer = container;
        last = pos;
        Vector2f newPos = pos.add(delta, new Vector2f());
        int[] deltaContainerIndex = new int[]{-1};
        for (int i = 0; i < maxIterations; i++) {
            if (!container.containsDelta(pos, newPos, deltaContainerIndex, pos, newPos, null)) {
                if (deltaContainerIndex[0] == -1 || container.nearby(deltaContainerIndex[0]) == null) {
                    pos = newPos;
                    return false;
                }
                container.transformDir(delta, deltaContainerIndex[0], null);
                container = container.nearby(deltaContainerIndex[0]);
            } else {
                pos = newPos;
                return true;
            }
        }
        pos = newPos;
        return true;
    }

    /**
     * move the point by the given delta.
     * <p>It will loop no more than the given maxIterations.</p>
     * <p>Then clamp the position to the last container.</p>
     *
     * @param delta         The delta.
     * @param maxIterations The maximum number of iterations.
     * @return true if the position was clamped, false if the next position is valid.
     */
    public boolean clampMove(Vector2f delta, int maxIterations) {
        lastContainer = container;
        last = pos;
        Vector2f newPos = pos.add(delta, new Vector2f());
        Vector2f clamped = new Vector2f();
        int[] deltaContainerIndex = new int[]{-1};
        for (int i = 0; i < maxIterations; i++) {
            if (!container.containsDelta(pos, newPos, deltaContainerIndex, pos, newPos, clamped)) {
                if (deltaContainerIndex[0] == -1 || container.nearby(deltaContainerIndex[0]) == null) {
                    pos = clamped;
                    return true;
                }
                container.transformDir(delta, deltaContainerIndex[0], null);
                container = container.nearby(deltaContainerIndex[0]);
            } else {
                pos = newPos;
                return false;
            }
        }
        pos = clamped;
        return true;
    }

    /**
     * move the point by the given delta.
     * <p>It will loop until the next position is in a container or it goes to where their is no container.</p>
     *
     * @param delta The delta.
     * @return true if the position was clamped, false if the next position is valid.
     */
    public boolean clampMove(Vector2f delta) {
        lastContainer = container;
        last = pos;
        Vector2f newPos = pos.add(delta, new Vector2f());
        Vector2f clamped = new Vector2f();
        int[] deltaContainerIndex = new int[]{-1};
        while (!container.containsDelta(pos, newPos, deltaContainerIndex, pos, newPos, clamped)) {
            if (deltaContainerIndex[0] == -1 || container.nearby(deltaContainerIndex[0]) == null) {
                pos = clamped;
                return true;
            }
            container.transformDir(delta, deltaContainerIndex[0], null);
            container = container.nearby(deltaContainerIndex[0]);
        }
        pos = newPos;
        return false;
    }

    /**
     * get the abs position of the point
     *
     * @return the abs position.
     */
    public Vector3f absPosition() {
        return container.absPositionTransform(pos);
    }

    /**
     * get the last abs position of the point
     *
     * @return the last abs position.
     */
    public Vector3f absPositionLast() {
        return lastContainer.absPositionTransform(last);
    }
}
