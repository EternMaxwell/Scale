package core.game;

import org.joml.*;
import org.joml.Math;

public class Container {
    private final Vector3f[] vertices = new Vector3f[3];
    private Vector3f normal;
    private Vector3f xDir;
    private Vector3f yDir;
    private final Container[] nearby = new Container[3];
    private final Vector3f[] nearbyOpposite = new Vector3f[3];
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
        yDir = new Vector3f(normal).cross(xDir, new Vector3f()).negate();
        yDir.normalize();
        transform3to2 = new Matrix4f();
        transform3to2.lookAt(vertex1, vertex1.add(normal, new Vector3f()), yDir);
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
     * @param container The nearby container.
     * @return true if the nearby container is set successfully, false otherwise.
     * <p>false may mean that the given container is not at the edge of the container.</p>
     */
    public boolean setNearby(int index, Container container) {
        nearby[index] = container;
        for (int i = 0; i < 3; i++) {
            boolean flag = true;
            for(int j = 0; j < 3; j++){
                if (container.vertex(i).equals(vertices[j])) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                nearbyOpposite[index] = container.vertex(i);
                return true;
            }
        }
        return false;
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

    /**
     * get the relative position of the given position in the container.
     * @param posInWorld The position in the world.
     * @return the relative position.
     */
    public Vector2f relPosition(Vector3f posInWorld){
        Vector3f temp = posInWorld.mulProject(transform3to2, new Vector3f());
        return new Vector2f(temp.x, temp.y);
    }

    /**
     * check if the given position is in the container.
     * @param posInContainer The position in the world.
     * @return true if the position is in the container, false otherwise.
     */
    public boolean contains(Vector2f posInContainer){
        Vector2f relPos1 = relPosition(vertices[1]);
        Vector2f relPos2 = relPosition(vertices[2]);
        Vector2f rel = relPos2.sub(relPos1, new Vector2f());
        float cross1 = relPos1.x * posInContainer.y - relPos1.y * posInContainer.x;
        float cross2 = relPos2.x * posInContainer.y - relPos2.y * posInContainer.x;
        posInContainer.sub(relPos1, relPos2);
        float cross3 = rel.x * relPos2.y - rel.y * relPos2.x;
        return cross1 >= 0 && cross2 <= 0 && cross3 >= 0;
    }

    //TODO: test for containsDelta
    /**
     * check if the given position is in the container.
     * @param posInContainer The position in the world.
     * @param delta The delta.
     * @param containerIndex The index of the container.
     * @param posInContainerDelta The delta of the position in the container.
     * @return true if the position is in the container, false otherwise.
     */
    public boolean containsDelta(Vector2f posInContainer, Vector2f delta, int[] containerIndex, Vector2f posInContainerDelta){
        Vector2f next = posInContainer.add(delta, new Vector2f());
        boolean result = contains(next);
        if (result){
            posInContainer.set(next);
            return true;
        }else {
            int index = -1;
            if(intersect(new Vector2f[]{posInContainer, next}, new Vector2f[]{relPosition(vertices[0]), relPosition(vertices[1])})) {
                index = 0;
            }else if (intersect(new Vector2f[]{posInContainer, next}, new Vector2f[]{relPosition(vertices[1]), relPosition(vertices[2])})) {
                index = 1;
            }else if (intersect(new Vector2f[]{posInContainer, next}, new Vector2f[]{relPosition(vertices[2]), relPosition(vertices[0])})) {
                index = 2;
            }
            if(index == -1){
                return true;
            }
            if(nearby[index] == null){
                return false;
            }
            if(containerIndex != null){
                containerIndex[0] = index;
            }
            Container container = nearby[index];
            Vector3f lineVertex1 = vertex(index);
            Vector3f lineVertex2 = vertex((index + 1) % 3);
            Vector3f opposite1 = vertex((index + 2) % 3);
            Vector3f opposite2 = nearbyOpposite[index];
            Vector3f line = lineVertex2.sub(lineVertex1, new Vector3f());
            Vector3f dir1 = opposite1.sub(lineVertex1, new Vector3f());
            Vector3f dir2 = opposite2.sub(lineVertex1, new Vector3f());
            float angle = dir1.angleSigned(dir2, line);
            Matrix4f transform = new Matrix4f().translate(lineVertex1).rotate(-angle, line).translate(lineVertex1.negate());
            if(posInContainerDelta != null){
                posInContainerDelta.set(container.relPosition(transform.transformProject(absPositionTransform(posInContainer))));
            }
            return false;
        }
    }

    /**
     * check if two lines intersect.
     * @param line1 The first line.
     *              <p>line1[0]: The start point of the first line.</p>
     *              <p>line1[1]: The end point of the first line.</p>
     * @param line2 The second line.
     *              <p>line2[0]: The start point of the second line.</p>
     *              <p>line2[1]: The end point of the second line.</p>
     * @return true if the two lines intersect, false otherwise.
     */
    private static boolean intersect(Vector2f[] line1, Vector2f[] line2) {
        Vector2f delta1 = line1[1].sub(line1[0], new Vector2f());
        Vector2f rel1 = line2[0].sub(line1[0], new Vector2f());
        Vector2f rel2 = line2[1].sub(line1[0], new Vector2f());
        float cross1 = delta1.x * rel1.y - delta1.y * rel1.x;
        float cross2 = delta1.x * rel2.y - delta1.y * rel2.x;
        if (cross1 * cross2 > 0) {
            return false;
        }
        Vector2f delta2 = line2[1].sub(line2[0], new Vector2f());
        Vector2f rel3 = line1[0].sub(line2[0], new Vector2f());
        Vector2f rel4 = line1[1].sub(line2[0], new Vector2f());
        float cross3 = delta2.x * rel3.y - delta2.y * rel3.x;
        float cross4 = delta2.x * rel4.y - delta2.y * rel4.x;
        return !(cross3 * cross4 > 0);
    }

    /**
     * test for container.
     * @param args the arguments.
     */
    public static void main(String[] args) {
        System.out.println("test for container::relPosition and container::absPositionDirect and container::absPositionTransform and container::contains");
        Container container = new Container(new Vector3f(1, 0, 0), new Vector3f(2, 1, 0), new Vector3f(0, 1, 0));
        System.out.println(new Vector2f(container.transform3to2.transformProject(new Vector3f(2,1,0)).x,
                                        container.transform3to2.transformProject(new Vector3f(2,1,0)).y));
        System.out.println(container.relPosition(new Vector3f(2,1,0)));
        Vector2f posInContainer = new Vector2f(Math.sqrt(2),0);
        System.out.println(container.contains(posInContainer));
        System.out.println(container.absPositionDirect(posInContainer));
        System.out.println(container.absPositionTransform(posInContainer));
    }
}
