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
    double mspt = 0;
    double gridStepTime = 0;
    double updateTimeRate = 0.15;
    Window window;
    Matrix4f viewMatrix = new Matrix4f();
    Matrix4f modelMatrix = new Matrix4f();
    @Override
    public void init(Window window) {
        grid = new FallingGrid();
        this.window = window;
    }

    @Override
    public void input() {
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
        modelMatrix.setColumn(3, new Vector4f(-(float) FallingData.cameraCentrePos[0],
                -(float) FallingData.cameraCentrePos[1], 0, 1));
        viewMatrix.ortho2D(0, FallingData.scale * FallingData.defaultShowGridWidth * 9/16, 0,
                FallingData.scale * FallingData.defaultShowGridWidth * 9/16);

        render.pixel.setModelMatrix(modelMatrix);
        render.pixel.setViewMatrix(viewMatrix);
        render.pixel.setPixelSize(1);

        int startH = 0;

        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "grid step time: " + String.format("%.2f",gridStepTime) + "ms", new Font("Arial", Font.PLAIN, 12));
        startH += render.text.drawText(5,startH,0,1,1,1,0.8f,
                "mspt: " + String.format("%.2f",mspt) + "ms", new Font("Arial", Font.PLAIN, 12));

        grid.render(render);
    }

    @Override
    public void cleanup() {}
}
