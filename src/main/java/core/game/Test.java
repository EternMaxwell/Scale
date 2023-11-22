package core.game;

import core.render.EasyRender;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Test {
    private Container[] containers;
    private Entity entity;

    public Test() {
        containers = new Container[2];
    }

    public void init() {
        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(-0.5f, -0.5f, 0.0f);
        vertices[1] = new Vector3f(0.5f, -0.5f, 0.0f);
        vertices[2] = new Vector3f(0.5f, 0.5f, 0.0f);
        vertices[3] = new Vector3f(-0.5f, 0.5f, 0.0f);
        containers[0] = new Container(vertices[0], vertices[1], vertices[2]);
        containers[1] = new Container(vertices[1], vertices[2], vertices[3]);
        containers[0].setNearby(containers[1]);
        containers[1].setNearby(containers[0]);
        entity = new Entity(containers[0], new Vector2f(0, 0));
    }

    public void update() {
        if(entity.clampMove(new Vector2f(0.01f, 0.01f)) && entity.container() == containers[1]){
            entity.setContainer(containers[0]);
            entity.setPos(0, 0);
        }
    }

    public void render(EasyRender renderer) {
    }
}
