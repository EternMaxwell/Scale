package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Grid;
import core.render.EasyRender;
import core.render.Window;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;

public class EasyFallingSandWorld extends core.game.fallingsand.FallingSandWorld{

    Grid grid;
    Window window;
    Matrix4f viewMatrix = new Matrix4f();
    @Override
    public void init(Window window) {
        grid = new EasyGrid(100, 100);
        this.window = window;
        viewMatrix.ortho2D(0, 100, 0, 100);
    }

    @Override
    public void input() {
        //if(glfwGetKey(window.id(), GLFW_KEY_SPACE) == GLFW_PRESS)
            grid.set(99,99, new Sand());
    }

    @Override
    public void update() {
        grid.step();
    }

    @Override
    public void render(EasyRender render) {
        render.pixel.setPixelSize(1);
        render.pixel.setViewMatrix(viewMatrix);

        for(int x = 0; x < 100; x++){
            for(int y = 0; y < 100; y++){
                if(grid.get(x, y) != null){
                    float[] color = grid.get(x, y).color();
                    render.pixel.drawPixel(x,y,color[0],color[1],color[2],color[3]);
                }
            }
        }
    }

    @Override
    public void cleanup() {}
}
