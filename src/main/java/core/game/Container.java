package core.game;

import org.joml.*;

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
        normal = vertex2.sub(vertex1, new Vector3f()).cross(vertex3.sub(vertex1, new Vector3f())).negate();
        normal.normalize();
        xDir = vertex2.sub(vertex1, new Vector3f());
        xDir.normalize();
        yDir = new Vector3f(normal).cross(xDir, new Vector3f());
        yDir.normalize();
        transform3to2 = new Matrix4f();
        transform3to2.translate(vertex1).lookAlong(normal, yDir);
        transform3to2.setLookAtLH(vertex1, vertex1.add(normal, new Vector3f()), yDir);
        transform2to3 = new Matrix4f(transform3to2).invert();
    }

    /**
     * set the vertices of the container.
     * @param vertex1 The first vertex.
     * @param vertex2 The second vertex.
     * @param vertex3 The third vertex.
     * @return the container.
     */
    public Container setVertices(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3) {
        vertices[0] = vertex1;
        vertices[1] = vertex2;
        vertices[2] = vertex3;
        normal = vertex2.sub(vertex1, new Vector3f()).cross(vertex3.sub(vertex1, new Vector3f())).negate();
        normal.normalize();
        xDir = vertex2.sub(vertex1, new Vector3f());
        xDir.normalize();
        yDir = new Vector3f(normal).cross(xDir, new Vector3f());
        yDir.normalize();
        transform3to2 = new Matrix4f();
        transform3to2.translate(vertex1).lookAlong(normal, yDir);
        transform3to2.setLookAtLH(vertex1, vertex1.add(normal, new Vector3f()), yDir);
        transform2to3 = new Matrix4f(transform3to2).invert();
        return this;
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
     * <p>Use the direct method.</p>
     * @param posInContainer The position in the container.
     * @return the absolute position.
     */
    public Vector3f absPositionDirect(Vector2f posInContainer){
        Vector3f pos = new Vector3f(vertices[0]).add(xDir.mul(posInContainer.x, new Vector3f())).add(yDir.mul(posInContainer.y, new Vector3f()));
        return pos;
    }

    /**
     * get the absolute position of the given position in the container.
     * <p>Use the transform method.</p>
     * @param posInContainer The position in the container.
     * @return the absolute position.
     */
    public Vector3f absPositionTransform(Vector2f posInContainer){
        Vector3f temp = new Vector3f(posInContainer, 0);
        return transform2to3.transformProject(temp);
    }

    public static void main(String[] args) {
        Container container = new Container(new Vector3f(1, 0, 0), new Vector3f(2, 4, 0), new Vector3f(0, 1, 0));
        System.out.println(new Vector2f(container.transform3to2.transformProject(new Vector3f(2,4,0)).x,
                                        container.transform3to2.transformProject(new Vector3f(2,4,0)).y));
        Vector2f posInContainer = new Vector2f(4.123f,0);
        System.out.println(container.absPositionDirect(posInContainer));
        System.out.println(container.absPositionTransform(posInContainer));
    }
}
