package core.game;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Container {
    private final Vector3f vertices[] = new Vector3f[3];
    private Vector3f normal;
    private Vector3f xDir;
    private Vector3f yDir;
    private final Container nearby[] = new Container[3];
    private Matrix4f transform3to2, transform2to3;

    /**
     * Creates a container with the given vertices.
     * @param vertex1 The first vertex.
     * @param vertex2 The second vertex.
     * @param vertex3 The third vertex.
     */
    public Container(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3) {
        vertices[0] = vertex1;
        vertices[1] = vertex2;
        vertices[2] = vertex3;
        normal = new Vector3f();
        normal = vertex2.sub(vertex1, new Vector3f()).cross(vertex3.sub(vertex1, new Vector3f()));
        normal.normalize();
        xDir = new Vector3f();
        xDir = vertex2.sub(vertex1, new Vector3f());
        xDir.normalize();
        yDir = new Vector3f();
        yDir = normal.cross(xDir, new Vector3f());
        yDir.normalize();
        transform3to2 = new Matrix4f();
        transform3to2.identity().setLookAt(new Vector3f(vertex1).add(normal), vertex1, yDir);
    }

    /**
     * set the nearby container at the given index.
     * @param index The index.
     *              <p>0: The container at the edge of the first vertex and the second vertex.</p>
     *              <p>1: The container at the edge of the second vertex and the third vertex.</p>
     *              <p>2: The container at the edge of the third vertex and the first vertex.</p>
     */
    public void setNearby(int index, Container container) {
        nearby[index] = container;
    }

    /**
     * get the nearby container at the given index.
     * @param index The index.
     *              <p>0: The container at the edge of the first vertex and the second vertex.</p>
     *              <p>1: The container at the edge of the second vertex and the third vertex.</p>
     *              <p>2: The container at the edge of the third vertex and the first vertex.</p>
     * @return the nearby container.
     */
    public Container nearby(int index) {
        return nearby[index];
    }

    /**
     * get the vertex at the given index.
     * @return the vertex.
     */
    public Vector3f vertex(int index) {
        return vertices[index];
    }

    /**
     * get the normal of the container.
     * @return the normal.
     */
    public Vector3f normal() {
        return normal;
    }

    /**
     * get the absolute position of the given position in the container.
     * @param posInContainer The position in the container.
     * @return the absolute position.
     */
    public Vector3f absPosition(Vector2f posInContainer){
        Vector3f pos = new Vector3f();
        pos = vertices[0].add(xDir.mul(posInContainer.x, new Vector3f())).add(yDir.mul(posInContainer.y, new Vector3f()));
        return pos;
    }
}
