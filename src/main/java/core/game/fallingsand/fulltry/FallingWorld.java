package core.game.fallingsand.fulltry;

import core.game.fallingsand.FallingSandWorld;
import core.game.fallingsand.Grid;
import core.game.fallingsand.fulltry.ui.UIManager;
import core.render.EasyRender;
import core.render.Window;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class FallingWorld extends FallingSandWorld {
    public Grid grid;
    double mspt = 0;
    double gridStepTime = 0;
    double updateTimeRate = 0.15;
    Window window;
    UIManager uiManager;
    @Override
    public void init(Window window) {
        FallingData.startup();
        FallingData.inputTool = new InputTool(window);
        FallingData.window = window;
        grid = new FallingGrid();
        this.window = window;
        FallingData.world = this;
        uiManager = new UIManager();
        FallingData.uiManager = uiManager;
    }

    @Override
    public void input() {
        FallingData.inputTool.input();
        uiManager.handleInput();
    }

    @Override
    public void update() {
        double start = glfwGetTime();
        gridStepTime = gridStepTime * (1 - updateTimeRate) + updateTimeRate * grid.step();
        double end = glfwGetTime();
        mspt = mspt * (1 - updateTimeRate) + updateTimeRate * (end - start) * 1000;
        uiManager.update();
    }

    @Override
    public void render(EasyRender render) {

        int startH = window.height();
        int x = window.width();


        render.text.setProjectionMatrix(new Matrix4f().ortho(0, render.window.width(), 0, render.window.height(), -1, 1));
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "grid step time: " + String.format("%.2f",gridStepTime) + "ms",
                new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "mspt: " + String.format("%.2f",mspt) + "ms",
                new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "chunkNum: " + FallingData.chunkNum, new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "enableChunkUpdate: " + FallingData.enableChunkUpdate,
                new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "scale: " + String.format("%.4f",FallingData.scale),
                new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "currentPage: " + String.format("%20s",FallingData.uiManager.currentName),
                new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "pause: " + FallingData.pause,
                new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "mouse pos: " + "(" + String.format("%3.3f",FallingData.inputTool.mousePosX()) + ", " +
                        String.format("%3.3f",FallingData.inputTool.mousePosY()) + ")" ,
                new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
                "mouse pos last: " + "(" + String.format("%3.3f",FallingData.inputTool.mousePosLast()[0]) + ", " +
                        String.format("%3.3f",FallingData.inputTool.mousePosLast()[1]) + ")",
                new Font("Arial", Font.PLAIN, 12))[1];
        startH -= render.text.drawTextRT(x,startH,0,1,1,1,0.8f,
        "scroll: " + String.format("%3.3f %3.3f",FallingData.inputTool.mouseScrollX(),FallingData.inputTool.mouseScrollY()),
                new Font("Arial", Font.PLAIN, 12))[1];
        grid.render(render);
        uiManager.render(render);
    }

    @Override
    public void cleanup() {}
}
