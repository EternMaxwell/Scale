package core.game.fallingsand.fulltry;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import core.game.fallingsand.fulltry.elements.Elements;
import core.render.EasyRender;
import org.joml.Matrix4f;

import java.util.Objects;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

public class FallingInput {
    public Elements elements;
    public Grid grid;
    public int id = 0;
    private boolean lastTickChunkUpdateKey = false;
    public int radius = 10;
    public float density = 0;

    public boolean putting = false;
    public boolean deleting = false;
    public String elementName = "cave_stone";
    public String action = "put";

    public FallingInput(Grid grid) {
        elements = new Elements();
        this.grid = grid;
    }

    public void input(long window) {
        Random random = new Random();
        if (FallingData.inputTool.isKeyJustPressed(GLFW_KEY_EQUAL)) {
            id = (id + 1) % elements.totalElements();
        }
        if (FallingData.inputTool.isKeyJustPressed(GLFW_KEY_MINUS)) {
            id = (id - 1) % elements.totalElements();
            if (id < 0)
                id += elements.totalElements();
        }
        if (FallingData.inputTool.isKeyPressed(GLFW_KEY_UP)) {
            radius++;
        }
        if (FallingData.inputTool.isKeyPressed(GLFW_KEY_DOWN)) {
            radius--;
            if (radius < 0) {
                radius = 0;
            }
        }
        if (FallingData.inputTool.isKeyPressed(GLFW_KEY_RIGHT)) {
            density += 0.01f;
            if (density > 1) {
                density = 1;
            }
        }
        if (FallingData.inputTool.isKeyPressed(GLFW_KEY_LEFT)) {
            density -= 0.01f;
            if (density < 0) {
                density = 0;
            }
        }
        if(FallingData.inputTool.isKeyPressed(GLFW_KEY_S)){
            FallingData.cameraCentrePos[1] -= 4;
        }
        if(FallingData.inputTool.isKeyPressed(GLFW_KEY_W)){
            FallingData.cameraCentrePos[1] += 4;
        }
        if(FallingData.inputTool.isKeyPressed(GLFW_KEY_A)){
            FallingData.cameraCentrePos[0] -= 4;
        }
        if(FallingData.inputTool.isKeyPressed(GLFW_KEY_D)){
            FallingData.cameraCentrePos[0] += 4;
        }
        if(FallingData.inputTool.isKeyPressed(GLFW_KEY_E)){
            FallingData.scale *= 0.98f;
        }
        if(FallingData.inputTool.isKeyPressed(GLFW_KEY_Q)){
            FallingData.scale *= 1.02f;
        }
        if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_C)){
            if(!lastTickChunkUpdateKey){
                FallingData.enableChunkUpdate = !FallingData.enableChunkUpdate;
            }
            lastTickChunkUpdateKey = true;
        }else{
            lastTickChunkUpdateKey = false;
        }

        int[] xsize = new int[1];
        int[] ysize = new int[1];
        glfwGetWindowSize(window, xsize, ysize);

        int x = (int) ((FallingData.inputTool.mousePosX()/2) * FallingData.defaultShowGridWidth * FallingData.scale / (16 / 9f));
        int y = (int) ((FallingData.inputTool.mousePosY()/2) * FallingData.defaultShowGridWidth * FallingData.scale * 9/16f);
        x -= FallingData.chunkBasePos[0] * FallingData.chunkWidth;
        y -= FallingData.chunkBasePos[1] * FallingData.chunkWidth;
        x += (int) (FallingData.cameraCentrePos[0] + 0.75f);
        y += (int) (FallingData.cameraCentrePos[1] + 0.5f);

        int[] lastMousePos = new int[]{(int) (FallingData.inputTool.mousePosLastX()/2 * FallingData.defaultShowGridWidth * FallingData.scale / (16 / 9f)),
                (int) (FallingData.inputTool.mousePosLastY()/2 * FallingData.defaultShowGridWidth * FallingData.scale * 9/16f)};

        lastMousePos[0] -= FallingData.chunkBasePos[0] * FallingData.chunkWidth;
        lastMousePos[1] -= FallingData.chunkBasePos[1] * FallingData.chunkWidth;
        lastMousePos[0] += (int) (FallingData.cameraCentrePos[0] + 0.75f);
        lastMousePos[1] += (int) (FallingData.cameraCentrePos[1] + 0.5f);

        if(FallingData.inputTool.isMouseJustPressed(GLFW_MOUSE_BUTTON_LEFT) && Objects.equals(action, "put")){
            putting = true;
        }

        if (FallingData.inputTool.isMousePressed(GLFW_MOUSE_BUTTON_LEFT) && Objects.equals(action, "put")) {
            if(putting){
                float length = (float) Math.sqrt((x - lastMousePos[0]) * (x - lastMousePos[0]) + (y - lastMousePos[1]) * (y - lastMousePos[1]));
                if (length != 0) {
                    for (int i = 0; i <= length / radius; i++) {
                        int nx = (int) (lastMousePos[0] + (x - lastMousePos[0]) / length * i * radius);
                        int ny = (int) (lastMousePos[1] + (y - lastMousePos[1]) / length * i * radius);
                        for (int ix = nx - radius; ix < nx + radius; ix++) {
                            for (int iy = ny - radius; iy < ny + radius; iy++) {
                                if (grid.valid(ix, iy)) {
                                    grid.set(ix, iy, null);
                                    if (random.nextFloat() < density)
                                        grid.set(ix, iy, Elements.newInstanceFromName(elementName));
                                }
                            }
                        }
                    }
                } else {
                    for (int ix = x - radius; ix < x + radius; ix++) {
                        for (int iy = y - radius; iy < y + radius; iy++) {
                            if (grid.valid(ix, iy)) {
                                grid.set(ix, iy, null);
                                if (random.nextFloat() < density)
                                    grid.set(ix, iy, Elements.newInstanceFromName(elementName));
                            }
                        }
                    }
                }
            }
        }else
            putting = false;

        if(FallingData.inputTool.isMouseJustPressed(GLFW_MOUSE_BUTTON_LEFT) && Objects.equals(action, "delete")){
            deleting = true;
        }

        if (FallingData.inputTool.isMousePressed(GLFW_MOUSE_BUTTON_LEFT) && Objects.equals(action, "delete")) {
            if(deleting){
                float length = (float) Math.sqrt((x - lastMousePos[0]) * (x - lastMousePos[0]) + (y - lastMousePos[1]) * (y - lastMousePos[1]));
                if (length != 0) {
                    for (int i = 0; i <= length / radius; i++) {
                        int nx = (int) (lastMousePos[0] + (x - lastMousePos[0]) / length * i * radius);
                        int ny = (int) (lastMousePos[1] + (y - lastMousePos[1]) / length * i * radius);
                        for (int ix = nx - radius; ix < nx + radius; ix++) {
                            for (int iy = ny - radius; iy < ny + radius; iy++) {
                                if (grid.valid(ix, iy)) {
                                    grid.set(ix, iy, null);
                                }
                            }
                        }
                    }
                } else {
                    for (int ix = x - radius; ix < x + radius; ix++) {
                        for (int iy = y - radius; iy < y + radius; iy++) {
                            if (grid.valid(ix, iy)) {
                                grid.set(ix, iy, null);
                            }
                        }
                    }
                }
            }
        }else
            deleting = false;

        if (FallingData.inputTool.isMousePressed(GLFW_MOUSE_BUTTON_LEFT) && Objects.equals(action, "heat")) {
            for (int ix = x - radius; ix < x + radius; ix++) {
                for (int iy = y - radius; iy < y + radius; iy++) {
                    if (grid.valid(ix, iy)) {
                        Element element = grid.get(ix, iy);
                        if(element != null){
                            element.heat(grid, ix, iy, FallingData.tick, 500);
                        }
                    }
                }
            }
        }
    }

    public void render(EasyRender render) {
        render.pixel.setModelMatrix(new Matrix4f().identity());
        render.pixel.setViewMatrix(new Matrix4f().ortho2D(0, 1024, 0, 1024));
        render.line.setModelMatrix(new Matrix4f().identity());
        render.line.setViewMatrix(new Matrix4f().identity());

        Random random = new Random();
        double[] mousePosX = new double[1];
        double[] mousePosY = new double[1];
        glfwGetCursorPos(render.window.id(), mousePosX, mousePosY);
        int[] xsize = new int[1];
        int[] ysize = new int[1];
        glfwGetWindowSize(render.window.id(), xsize, ysize);
        float ratio = (float) xsize[0] / ysize[0];
        float x1 = 2 * ratio * ((float) mousePosX[0] / xsize[0] - 0.5f);
        float y1 = 0.5f - (float) mousePosY[0] / ysize[0];
        y1 *= 2;
        float length = radius / (FallingData.defaultShowGridWidth * 9f / 32) / FallingData.scale;
//        x1 -= 1 / (FallingData.defaultShowGridWidth * 9f / 32) / FallingData.scale;
//        y1 -= 1 / (FallingData.defaultShowGridWidth * 9f / 32) / FallingData.scale;
        render.line.drawLine2D(x1 - length, y1 - length, x1 + length, y1 - length, 1, 0, 0, 0.5f);
        render.line.drawLine2D(x1 - length, y1 - length, x1 - length, y1 + length, 1, 0, 0, 0.5f);
        render.line.drawLine2D(x1 + length, y1 - length, x1 + length, y1 + length, 1, 0, 0, 0.5f);
        render.line.drawLine2D(x1 - length, y1 + length, x1 + length, y1 + length, 1, 0, 0, 0.5f);

//        float color[] = elements.getFromId(id).defaultColor();
//        for (int x = 0; x < 32; x++)
//            for (int y = 0; y < 32; y++) {
//                if (random.nextFloat() < density)
//                    render.pixel.drawPixel(x - 256, y + 900, color[0], color[1], color[2], color[3]);
//            }
//
        render.pixel.flush();
    }
}
