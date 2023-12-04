package core.game;

import core.render.EasyRender;
import org.joml.*;
import org.joml.Math;

import static org.lwjgl.glfw.GLFW.*;

public class Test {
    private Container[] containers;
    private Point point;

    Vector3f[] vertices;
    Vector2f dir;

    Matrix4f view = new Matrix4f();

    Matrix4f rotateY = new Matrix4f();
    Matrix4f rotateX = new Matrix4f();
    float distance = 10;

    public Test() {
        containers = new Container[4];
    }

    public void init() {
        vertices = new Vector3f[4];
        vertices[0] = new Vector3f(0, 0, 0);
        vertices[1] = new Vector3f(1, 0, 0);
        vertices[2] = new Vector3f(0, 1, 0);
        vertices[3] = new Vector3f(1, 1, 1);
        containers[0] = new Container(vertices[0], vertices[1], vertices[2]);
        containers[1] = new Container(vertices[1], vertices[2], vertices[3]);
        containers[2] = new Container(vertices[2], vertices[3], vertices[0]);
        containers[3] = new Container(vertices[3], vertices[0], vertices[1]);
        containers[0].setNearby(containers[1]);
        containers[0].setNearby(containers[2]);
        containers[0].setNearby(containers[3]);
        containers[1].setNearby(containers[0]);
        containers[1].setNearby(containers[2]);
        containers[1].setNearby(containers[3]);
        containers[2].setNearby(containers[0]);
        containers[2].setNearby(containers[1]);
        containers[2].setNearby(containers[3]);
        containers[3].setNearby(containers[0]);
        containers[3].setNearby(containers[1]);
        containers[3].setNearby(containers[2]);
        point = new Point(containers[0], new Vector2f(0.1f, 0.1f));
        dir = new Vector2f(0.012f, 0.01f);

        view.lookAtLH(new Vector3f(0, 0, -distance), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
    }

    public void update() {
        System.out.println(point.clampMove(dir, 20, null));
        System.out.println(point.pos()+" "+point.absPosition());
    }

    public void render(EasyRender renderer) {
        if(renderer != null){
            if(glfwGetKey(renderer.window().id(), GLFW_KEY_A) == GLFW_PRESS)
                rotateY.rotate(Math.toRadians(-1), 0, 1,0);
            if(glfwGetKey(renderer.window().id(), GLFW_KEY_D) == GLFW_PRESS)
                rotateY.rotate(Math.toRadians(1), 0, 1,0);
            if(glfwGetKey(renderer.window().id(), GLFW_KEY_W) == GLFW_PRESS)
                rotateX.rotate(Math.toRadians(-1), 1, 0,0);
            if(glfwGetKey(renderer.window().id(), GLFW_KEY_S) == GLFW_PRESS)
                rotateX.rotate(Math.toRadians(1), 1, 0,0);
            if(glfwGetKey(renderer.window().id(), GLFW_KEY_SPACE) == GLFW_PRESS){
                point.setContainer(containers[0]);
                point.setPos(new Vector2f(0.1f, 0.1f));
                Random random = new Random();
                dir.x = random.nextFloat() * 0.02f - 0.01f;
                dir.y = random.nextFloat() * 0.02f - 0.01f;
            }
            if(glfwGetMouseButton(renderer.window().id(), GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS){
                distance += 0.1f;
                view.setLookAtLH(new Vector3f(0, 0, -distance), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
            }
            if(glfwGetMouseButton(renderer.window().id(), GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS){
                distance -= 0.1f;
                view.setLookAtLH(new Vector3f(0, 0, -distance), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
            }

            Matrix4f mat = new Matrix4f().perspectiveLH(Math.toRadians(45f), 1, 1, Float.POSITIVE_INFINITY).mul(view).mul(rotateX).mul(rotateY);

            renderer.line.setViewMatrix(mat);
            renderer.point.setViewMatrix(mat);
            renderer.triangle.setViewMatrix(mat);

            renderer.line.drawLine3D(0,0,0,1000,0,0,1,0,0,1);
            renderer.line.drawLine3D(0,0,0,0,1000,0,0,1,0,1);
            renderer.line.drawLine3D(0,0,0,0,0,1000,0,0,1,1);

            renderer.line.drawLine3D(vertices[0].x, vertices[0].y, vertices[0].z, vertices[1].x, vertices[1].y, vertices[1].z, 1, 1, 0, 0.1f);
            renderer.line.drawLine3D(vertices[1].x, vertices[1].y, vertices[1].z, vertices[2].x, vertices[2].y, vertices[2].z, 1, 1, 0, 0.1f);
            renderer.line.drawLine3D(vertices[2].x, vertices[2].y, vertices[2].z, vertices[3].x, vertices[3].y, vertices[3].z, 1, 1, 0, 0.1f);
            renderer.line.drawLine3D(vertices[3].x, vertices[3].y, vertices[3].z, vertices[0].x, vertices[0].y, vertices[0].z, 1, 1, 0, 0.1f);
            renderer.line.drawLine3D(vertices[0].x, vertices[0].y, vertices[0].z, vertices[2].x, vertices[2].y, vertices[2].z, 1, 1, 0, 0.1f);
            renderer.line.drawLine3D(vertices[1].x, vertices[1].y, vertices[1].z, vertices[3].x, vertices[3].y, vertices[3].z, 1, 1, 0, 0.1f);

            renderer.triangle.drawTriangle3D(point.container().vertex(0).x, point.container().vertex(0).y, point.container().vertex(0).z,
                    point.container().vertex(1).x, point.container().vertex(1).y, point.container().vertex(1).z,
                    point.container().vertex(2).x, point.container().vertex(2).y, point.container().vertex(2).z,
                    1, 0, 0, 0.1f);

            Vector3f absPos = point.absPosition();
            renderer.point.drawPoint(absPos.x, absPos.y, absPos.z, 0, 1, 1, 1);
        }
    }
}
