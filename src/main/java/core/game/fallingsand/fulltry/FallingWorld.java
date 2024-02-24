package core.game.fallingsand.fulltry;

import core.game.fallingsand.FallingSandWorld;
import core.game.fallingsand.Grid;
import core.game.fallingsand.easyfallingsand.ChunkAndSleepingBasedGrid;
import core.game.fallingsand.easyfallingsand.ElementPlacement;
import core.render.EasyRender;
import core.render.Window;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class FallingWorld extends FallingSandWorld {
    Grid grid;
    FallingInput input;
    double mspt = 0;
    double gridStepTime = 0;
    double updateTimeRate = 0.15;
    Window window;
    @Override
    public void init(Window window) {
        FallingData.startup();
        grid = new FallingGrid();
        input = new FallingInput(grid);
        this.window = window;
    }

    @Override
    public void input() {
        input.input(window.id());
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

        int startH = 0;

        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "grid step time: " + String.format("%.2f",gridStepTime) + "ms", new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "mspt: " + String.format("%.2f",mspt) + "ms", new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "place-radius: " + input.radius, new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "place-density: " + String.format("%.5f", input.density), new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "place-type: " + input.id, new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "chunkNum: " + FallingData.chunkNum, new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "enableChunkUpdate: " + FallingData.enableChunkUpdate, new Font("Arial", Font.PLAIN, 12));

        grid.render(render);
        input.render(render);
    }

    @Override
    public void cleanup() {}
}
