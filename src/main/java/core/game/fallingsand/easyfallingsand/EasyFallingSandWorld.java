package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Grid;
import core.game.fallingsand.fulltry.FallingData;
import core.game.fallingsand.fulltry.FallingGrid;
import core.render.EasyRender;
import core.render.Window;
import org.joml.Matrix4f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32.*;

public class EasyFallingSandWorld extends core.game.fallingsand.FallingSandWorld{

    Grid grid;
    Window window;
    double mspt = 0;
    double gridStepTime = 0;
    double updateTimeRate = 0.15;
    Matrix4f viewMatrix = new Matrix4f();
    ElementPlacement elementPlacement;
    @Override
    public void init(Window window) {
        FallingData.startup();
        grid = new ChunkAndSleepingBasedGrid(16, 16);
        grid = new FallingGrid();
        this.window = window;
        viewMatrix.ortho2D(0, 1024, 0, 1024);
        elementPlacement = new ElementPlacement(grid);
    }

    @Override
    public void input() {
        elementPlacement.input(window.id());
    }

    @Override
    public void update() {
        double start = glfwGetTime();
        gridStepTime = gridStepTime * (1 - updateTimeRate) + updateTimeRate * grid.step();
        double end = glfwGetTime();
        mspt = mspt * (1 - updateTimeRate) + updateTimeRate * (end - start) * 1000;
    }

    @Override
    public void render(EasyRender render) {
//        render.pixel.setPixelSize(1);
//        render.pixel.setViewMatrix(viewMatrix);
//
//        for(int x = 0; x < 1024; x++){
//            for(int y = 0; y < 1024; y++){
//                if(grid.get(x, y) != null){
//                    float[] color = grid.get(x, y).color();
//                    if(!grid.get(x,y).freeFall()){
//                        //color = new float[]{color[0]*0.5f, color[1]*0.5f, color[2]*0.5f, color[3]};
//                    }
//                    render.pixel.drawPixel(x+0.5f,y+0.5f,color[0],color[1],color[2],color[3]);
//                }
//            }
//        }

        int startH = 0;

        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "grid step time: " + String.format("%.2f",gridStepTime) + "ms", new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "mspt: " + String.format("%.2f",mspt) + "ms", new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "place-radius: " + elementPlacement.radius, new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "place-density: " + String.format("%.5f", elementPlacement.density), new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "place-type: " + elementPlacement.id, new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "chunkNum: " + FallingData.chunkNum, new Font("Arial", Font.PLAIN, 12));

        render.pixel.flush();

        grid.render(render);
        elementPlacement.render(render);
    }

    @Override
    public void cleanup() {}
}
