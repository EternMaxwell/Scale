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
    ElementPlacement elementPlacement;
    @Override
    public void init(Window window) {
        grid = new EasyGrid(1000, 1000);
        this.window = window;
        viewMatrix.ortho2D(0, 1000, 0, 1000);
        elementPlacement = new ElementPlacement(grid);
    }

    @Override
    public void input() {
        elementPlacement.input(window.id());
    }

    @Override
    public void update() {
        grid.step();
    }

    @Override
    public void render(EasyRender render) {
        render.pixel.setPixelSize(1);
        render.pixel.setViewMatrix(viewMatrix);

        for(int x = 0; x < 1000; x++){
            for(int y = 0; y < 1000; y++){
                if(grid.get(x, y) != null){
                    float[] color = grid.get(x, y).color();
                    render.pixel.drawPixel(x+0.5f,y+0.5f,color[0],color[1],color[2],color[3]);
                }
            }
        }

        elementPlacement.render(render);
    }

    @Override
    public void cleanup() {}
}
