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
    private Matrix4f dirTransform3to2, dirTransform2to3;

    /**
     * Creates a container with the given vertices.
     *
     * @param vertex1 The first vertex.
     * @param vertex2 The second vertex.
     * @param vertex3 The third vertex.
     */
    public Container(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3) {
        setVertices(vertex1, vertex2, vertex3);
    }

    /**
     * set the vertices of the container.
     *
     * @param vertex1 The first vertex.
     * @param vertex2 The second vertex.
     * @param vertex3 The third vertex.
     * @return the container.
     */
    public Container setVertices(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3) {
        vertices[0] = vertex1;
        vertices[1] = vertex2;
        vertices[2] = vertex3;
        normal = vertex2.sub(vertex1, new Vector3f()).cross(vertex3.sub(vertex1, new Vector3f()));
        normal.normalize();
        xDir = vertex2.sub(vertex1, new Vector3f());
        xDir.normalize();
        yDir = new Vector3f(normal).cross(xDir, new Vector3f());
        yDir.normalize();
        transform2to3 = new Matrix4f().setColumn(0, new Vector4f(xDir, 0)).setColumn(1, new Vector4f(yDir, 0)).setColumn(2, new Vector4f(normal, 0)).setColumn(3, new Vector4f(vertex1, 1));
        transform3to2 = new Matrix4f(transform2to3).invert();
        dirTransform2to3 = new Matrix4f().setColumn(0, new Vector4f(xDir, 0)).setColumn(1, new Vector4f(yDir, 0)).setColumn(2, new Vector4f(normal, 0));
        dirTransform3to2 = new Matrix4f(dirTransform2to3).invert();
        return this;
    }

    /**
     * get the vertices of the container.
     *
     * @return the vertices.
     */
    public Vector3f[] vertices() {
        return vertices;
    }

    /**
     * get the nearby container at the edge of the given vertex.
     *
     * @param vertex The given vertex.
     * @return the nearby container.
     */
    public Container nearby(Vector3f vertex) {
        for (int i = 0; i < 3; i++) {
            if (vertex.equals(vertices[i])) {
                return nearby[i];
            }
        }
        return null;
    }

    /**
     * set the nearby container at the given index.
     *
     * @param index     The index.
     *                  <p>0: The container at the edge of the first vertex and the second vertex.</p>
     *                  <p>1: The container at the edge of the second vertex and the third vertex.</p>
     *                  <p>2: The container at the edge of the third vertex and the first vertex.</p>
     * @param container The nearby container.
     * @return true if the nearby container is set successfully, false otherwise.
     * <p>false may mean that the given container is not at the edge of the container.</p>
     */
    public boolean setNearby(int index, Container container) {
        for (int i = 0; i < 3; i++) {
            boolean flag = true;
            for (int j = 0; j < 3; j++) {
                if (container.vertex(i).equals(vertices[j])) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                nearbyOpposite[index] = container.vertex(i);
                nearby[index] = container;
                return true;
            }
        }
        return false;
    }

    /**
     * set the nearby container at the given index.
     *
     * @param index     The index.
     *                  <p>0: The container at the edge of the first vertex and the second vertex.</p>
     *                  <p>1: The container at the edge of the second vertex and the third vertex.</p>
     *                  <p>2: The container at the edge of the third vertex and the first vertex.</p>
     * @param container The nearby container.
     * @param opposite The opposite vertex of the nearby container.
     * @return true if the nearby container is set successfully, false otherwise.
     * <p>false may mean that the given container is not at the edge of the container.</p>
     */
    public boolean setNearby(int index, Container container, Vector3f opposite){
        nearbyOpposite[index] = opposite;
        nearby[index] = container;
        return true;
    }

    /**
     * set the nearby container.
     *
     * @param container The nearby container.
     * @return true if the nearby container is set successfully, false otherwise.
     * <p>false may mean that the given container is not at the edge of the container.</p>
     */
    public boolean setNearby(Container container) {
        boolean result = false;
        for (int i = 0; i < 3; i++) {
            boolean[] flag = new boolean[]{false, false};
            int notEqualIndex = -1;
            for (int j = 0; j < 3; j++) {
                if (container.vertex(j).equals(vertices[i])) {
                    flag[0] = true;
                } else if (container.vertex(j).equals(vertices[(i + 1) % 3])) {
                    flag[1] = true;
                } else {
                    notEqualIndex = j;
                }
            }
            if(flag[0] && flag[1]) {
                nearbyOpposite[i] = container.vertex(notEqualIndex);
                nearby[i] = container;
                result = true;
            }
        }
        return result;
    }

    /**
     * get the nearby container at the given index.
     *
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
     *
     * @return the vertex.
     */
    public Vector3f vertex(int index) {
        return vertices[index];
    }

    /**
     * get the normal of the container.
     *
     * @return the normal.
     */
    public Vector3f normal() {
        return normal;
    }

    /**
     * get the absolute position of the given position in the container.
     * <p>Use the direct method.</p>
     *
     * @param posInContainer The position in the container.
     * @return the absolute position.
     */
    public Vector3f absPositionDirect(Vector2f posInContainer) {
        Vector3f pos = new Vector3f(vertices[0]).add(xDir.mul(posInContainer.x, new Vector3f())).add(yDir.mul(posInContainer.y, new Vector3f()));
        return pos;
    }

    /**
     * get the absolute position of the given position in the container.
     * <p>Use the transform method.</p>
     *
     * @param posInContainer The position in the container.
     * @return the absolute position.
     */
    public Vector3f absPositionTransform(Vector2f posInContainer) {
        Vector3f temp = new Vector3f(posInContainer, 0);
        return transform2to3.transformProject(temp);
    }

    /**
     * get the absolute direction of the given direction in the container.
     * @param dirInContainer The direction in the container.
     * @return the absolute direction.
     */
    public Vector3f absDir(Vector2f dirInContainer){
        Vector3f temp = new Vector3f(dirInContainer, 0);
        return dirTransform2to3.transformProject(temp);
    }

    /**
     * get the relative direction of the given direction in the container.
     * @param dirInWorld The direction in the world.
     * @return the relative direction.
     */
    public Vector2f relDir(Vector3f dirInWorld){
        Vector3f temp = dirInWorld.mulProject(dirTransform3to2, new Vector3f());
        return new Vector2f(temp.x, temp.y);
    }

    /**
     * get the relative position of the given position in the container.
     *
     * @param posInWorld The position in the world.
     * @return the relative position.
     */
    public Vector2f relPosition(Vector3f posInWorld) {
        Vector3f temp = posInWorld.mulProject(transform3to2, new Vector3f());
        return new Vector2f(temp.x, temp.y);
    }

    /**
     * check if the given position is in the container.
     *
     * @param posInContainer The position in the world.
     * @return true if the position is in the container, false otherwise.
     */
    public boolean contains(Vector2f posInContainer) {
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
     *
     * @param posInContainer       The position in the container.
     * @param next                 The next position in the container.
     * @param containerIndex       The index of the container.
     * @param posInContainerDelta  where the position in the container delta is written to.
     * @param nextInContainerDelta where the next position in the container delta is written to.
     * @return true if the position is in the container, false otherwise.
     */
    public boolean containsDelta(Vector2f posInContainer, Vector2f next, int[] containerIndex, Vector2f posInContainerDelta, Vector2f nextInContainerDelta, Vector2f intersectionPoint) {
        boolean result = contains(next);
        if (result) {
            if (posInContainerDelta != null && posInContainerDelta != posInContainer) {
                posInContainerDelta.set(posInContainer);
            }
            if (nextInContainerDelta != null && nextInContainerDelta != next) {
                nextInContainerDelta.set(next);
            }
            return true;
        } else {
            int index = outSideIndex(posInContainer, next);
            if (index == -1) {
                return true;
            }
            intersect(new Vector2f[]{posInContainer, next}, new Vector2f[]{relPosition(vertices[index]), relPosition(vertices[(index + 1) % 3])}, intersectionPoint);
            if (containerIndex != null) {
                containerIndex[0] = index;
            }
            if (nearby[index] == null) {
                return false;
            }
            transformPosition(new Vector2f[]{posInContainer, next}, index, new Vector2f[]{posInContainerDelta, nextInContainerDelta});
            return false;
        }
    }

    /**
     * transform the given position in this container to the given index container.
     * @param posInContainer The position in this container.
     * @param index The index of the container in the nearby containers.
     * @param dest where the result is written to. If null, then write to posInContainer.
     * @return the result.
     */
    public Vector2f[] transformPosition(Vector2f[] posInContainer, int index, Vector2f[] dest){
        Container delta = nearby[index];
        Vector3f lineVertex1 = new Vector3f(vertex(index));
        Vector3f lineVertex2 = vertex((index + 1) % 3);
        Vector3f opposite1 = vertex((index + 2) % 3);
        Vector3f opposite2 = nearbyOpposite[index];
        Vector3f line = lineVertex2.sub(lineVertex1, new Vector3f());
        Vector3f dir1 = opposite1.sub(lineVertex1, new Vector3f());
        Vector3f dir2 = opposite2.sub(lineVertex1, new Vector3f());
        dir1.cross(line).cross(line).negate();
        dir2.cross(line).cross(line).negate();
        float angle = (float) (Math.PI - dir1.angleSigned(dir2, line));
        Matrix4f transform = new Matrix4f().translate(lineVertex1).rotate(-angle, line.normalize()).translate(lineVertex1.negate());
        if(dest == null){
            for (Vector2f vector2f : posInContainer) {
                Vector3f temp = absPositionTransform(vector2f);
                temp = transform.transformProject(temp);
                temp = delta.transform3to2.transformProject(temp);
                vector2f.set(temp.x, temp.y);
            }
            return posInContainer;
        }
        for(int i = 0; i < posInContainer.length; i++){
            Vector3f temp = absPositionTransform(posInContainer[i]);
            temp = transform.transformProject(temp);
            temp = delta.transform3to2.transformProject(temp);
            if(dest[i] == null)
                dest[i] = new Vector2f(temp.x, temp.y);
            else
                dest[i].set(temp.x, temp.y);
        }
        return dest;
    }

    /**
     * transform the given direction in this container to the given index container.
     * @param dir The direction in this container.
     * @param index The index of the container in the nearby containers.
     * @param dest where the result is written to. If null, then write to dir.
     * @return the result.
     */
    public Vector2f transformDir(Vector2f dir, int index, Vector2f dest){
        Container delta = nearby[index];
        Vector3f temp = dirTransform2to3.transformProject(new Vector3f(dir, 0));
        System.out.println(temp);
        Vector3f lineVertex1 = new Vector3f(vertex(index));
        Vector3f lineVertex2 = vertex((index + 1) % 3);
        Vector3f opposite1 = vertex((index + 2) % 3);
        Vector3f opposite2 = nearbyOpposite[index];
        Vector3f line = lineVertex2.sub(lineVertex1, new Vector3f());
        Vector3f dir1 = opposite1.sub(lineVertex1, new Vector3f());
        Vector3f dir2 = opposite2.sub(lineVertex1, new Vector3f());
        dir1.cross(line).cross(line).negate();
        dir2.cross(line).cross(line).negate();
        float angle = (float) (Math.PI - dir1.angleSigned(dir2, line));
        Matrix4f transform = new Matrix4f().rotate(-angle, line.normalize());
        temp = transform.transformProject(temp);
        temp = delta.dirTransform3to2.transformProject(temp);
        if(dest != null) {
            dest.set(temp.x, temp.y);
            return dest;
        }
        dir.set(temp.x, temp.y);
        return dir;
    }

    /**
     * check if two lines intersect.
     *
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
        if (cross1 * cross2 < 0) {
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
     * check if two lines intersect.
     *
     * @param line1             The first line.
     *                          <p>line1[0]: The start point of the first line.</p>
     *                          <p>line1[1]: The end point of the first line.</p>
     * @param line2             The second line.
     *                          <p>line2[0]: The start point of the second line.</p>
     *                          <p>line2[1]: The end point of the second line.</p>
     * @param intersectionPoint where the intersection point is written to.
     * @return true if the two lines intersect, false otherwise.
     */
    public static boolean intersect(Vector2f[] line1, Vector2f[] line2, Vector2f intersectionPoint) {
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
        if(cross3 * cross4 > 0){
            return false;
        }
        if (intersectionPoint != null) {
            float cross5 = delta1.x * delta2.y - delta1.y * delta2.x;
            cross5 = Math.abs(cross5);
            cross3 = Math.abs(cross3);
            float t = cross3 / cross5;
            intersectionPoint.set(line1[0].add(delta1.mul(t), new Vector2f()));
        }
        return true;
    }

    /**
     * check if two lines intersect.
     *
     * @param line1             The first line.
     *                          <p>line1[0]: The start point of the first line.</p>
     *                          <p>line1[1]: The end point of the first line.</p>
     * @param line2             The second line.
     *                          <p>line2[0]: The start point of the second line.</p>
     *                          <p>line2[1]: The end point of the second line.</p>
     * @param t                 where the intersection point's relative position on the first line is written to.
     * @return true if the two lines intersect, false otherwise.
     */
    public static boolean intersect(Vector2f[] line1, Vector2f[] line2, float[] t){
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
        if(cross3 * cross4 > 0){
            return false;
        }
        float cross5 = delta1.x * delta2.y - delta1.y * delta2.x;
        cross5 = Math.abs(cross5);
        cross3 = Math.abs(cross3);
        t[0] = cross3 / cross5;
        return true;
    }

    /**
     * get the index of the container side that the pos go out of.
     * @param pos The position.
     * @param next The next position.
     * @return the index of the container side that the pos go out of.
     */
    public int outSideIndex(Vector2f pos, Vector2f next){
        int index = -1;
        float[] t = new float[1];
        float[] t1 = new float[1];
        for(int i = 0; i < 3; i++){
            if(intersect(new Vector2f[]{pos, next}, new Vector2f[]{relPosition(vertices[i]), relPosition(vertices[(i + 1) % 3])}, t)){
                if(t[0] >= t1[0]){
                    index = i;
                    t1[0] = t[0];
                }
            }
        }
        return index;
    }

    /**
     * test for container.
     *
     * @param args the arguments.
     */
    public static void main(String[] args) {
        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(0, 0, 1);
        vertices[1] = new Vector3f(1, 0, 1);
        vertices[2] = new Vector3f(0, 1, 1);
        vertices[3] = new Vector3f(0, 0, 2);
        Container container1 = new Container(vertices[0], vertices[1], vertices[2]);
        Container container2 = new Container(vertices[1], vertices[2], vertices[3]);
        container1.setNearby(container2);
        container2.setNearby(container1);

        Vector2f posInContainer = new Vector2f(0.5f, 0.1f);

        Vector2f posInContainer1 = new Vector2f(0.2f, 0.2f);
        Vector2f next1 = new Vector2f(0.6f, 0.6f);
        Vector2f posInContainerDelta1 = new Vector2f();
        Vector2f nextInContainerDelta1 = new Vector2f();
        Vector2f intersectionPoint = new Vector2f();

        Vector2f[] line1 = new Vector2f[]{new Vector2f(0, 0), new Vector2f(1, 1)};
        Vector2f[] line2 = new Vector2f[]{new Vector2f(0, 1), new Vector2f(1, 0)};

        //====TEST FOR CONTAINER'S RELATIVE POSITION AND ABSOLUTE POSITION DIRECT AND ABSOLUTE POSITION TRANSFORM AND CONTAINS====//
        System.out.println("====test for container::relPosition and container::absPositionDirect and container::absPositionTransform and container::contains====".toUpperCase());
        System.out.print("relPosition:\n\t");
        System.out.println(container1.relPosition(new Vector3f(2, 1, 0)));
        System.out.print("contains:\n\t");
        System.out.println(container1.contains(posInContainer));
        System.out.print("absPositionDirect:\n\t");
        System.out.println(container1.absPositionDirect(posInContainer));
        System.out.print("absPositionTransform:\n\t");
        System.out.println(container1.absPositionTransform(posInContainer));
        System.out.println();


        //====TEST FOR CONTAINER'S CONTAINS DELTA====//
        System.out.println("====test for container::containsDelta====".toUpperCase());
        System.out.print("containsDelta:\n\t");
        System.out.println(container1.containsDelta(posInContainer1, next1, null, posInContainerDelta1, nextInContainerDelta1, intersectionPoint));
        System.out.print("\tposInContainerDelta:\n\t\t");
        System.out.println(posInContainerDelta1);
        System.out.print("\tnextInContainerDelta:\n\t\t");
        System.out.println(nextInContainerDelta1);
        System.out.println();
        System.out.println("abs position:\n\t"+container2.absPositionTransform(posInContainerDelta1)+"\n\t"+container2.absPositionTransform(nextInContainerDelta1));

        //====TEST FOR CONTAINER'S INTERSECT====//
        System.out.println("====test for container::intersect====".toUpperCase());
        System.out.println(intersect(line1, line2, intersectionPoint));
        System.out.print("\tintersectionPoint:\n\t\t");
        System.out.println(intersectionPoint);
        System.out.println();

        //====TEST FOR CONTAINER'S INTERSECT FLOAT VERSION====//
        System.out.println("====test for container::intersect float version====".toUpperCase());
        float[] t = new float[1];
        System.out.println(intersect(line1, line2, t));
        System.out.print("\tt:\n\t\t");
        System.out.println(t[0]);

        //====TEST FOR CONTAINER'S OUTSIDE INDEX====//
        System.out.println("====test for container::outSideIndex====".toUpperCase());
        System.out.println("index: \n\t"+container1.outSideIndex(posInContainer1, next1)+"\n");

        //====TEST FOR TRANSFORM POSITION====//
        System.out.println("====test for container::transformPosition====".toUpperCase());
        container1.transformPosition(new Vector2f[]{posInContainer1, next1}, 1, new Vector2f[]{posInContainerDelta1, nextInContainerDelta1});
        System.out.println("\tposInContainerDelta:\n\t\t"+posInContainer1);
        System.out.println("\tnextInContainerDelta:\n\t\t"+next1);
        System.out.print("\tposInContainerDelta:\n\t\t");
        System.out.println(posInContainerDelta1);
        System.out.print("\tnextInContainerDelta:\n\t\t");
        System.out.println(nextInContainerDelta1);
        System.out.println();

        //====TEST FOR TRANSFORM DIR====//
        System.out.println("====test for container::transformDir====".toUpperCase());
        System.out.println("\tdir in delta:\n\t\t"+container1.transformDir(new Vector2f(2, 1), 1, null));
    }
}
