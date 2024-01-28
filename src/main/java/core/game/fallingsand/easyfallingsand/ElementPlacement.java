package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Grid;
import core.render.EasyRender;
import core.render.Window;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class ElementPlacement {
    public Elements elements;
    public Grid grid;
    private int id = 0;
    private int radius = 10;
    private float density = 0.1f;
    private final int[] lastMousePos = new int[2];

    public ElementPlacement(Grid grid) {
        elements = new Elements();
        this.grid = grid;
    }

    public void input(long window) {
        Random random = new Random();
        if(glfwGetKey(window, GLFW_KEY_EQUAL) == GLFW_PRESS){
            id = (id+1)%3;
        }
        if(glfwGetKey(window, GLFW_KEY_MINUS) == GLFW_PRESS){
            id = (id-1)%3;
            if(id < 0)
                id += 2;
        }
        if(glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS){
            radius++;
        }
        if(glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS){
            radius--;
            if(radius < 0){
                radius = 0;
            }
        }
        if(glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS){
            density += 0.01f;
            if(density > 1){
                density = 1;
            }
        }
        if(glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS){
            density -= 0.01f;
            if(density < 0){
                density = 0;
            }
        }
        if(glfwGetKey(window, GLFW_KEY_1) == GLFW_PRESS){
            id = 0;
        }
        if(glfwGetKey(window, GLFW_KEY_2) == GLFW_PRESS){
            id = 1;
        }
        if(glfwGetKey(window, GLFW_KEY_3) == GLFW_PRESS){
            id = 2;
        }

        double[] xpos = new double[1];
        double[] ypos = new double[1];
        int[] xsize = new int[1];
        int[] ysize = new int[1];
        glfwGetCursorPos(window, xpos, ypos);
        glfwGetWindowSize(window, xsize, ysize);
        float ratio = (float) xsize[0]/ysize[0];
        float x1 = ratio * ((float) xpos[0]/xsize[0]-0.5f)+0.5f;
        float y1 = 1- (float) ypos[0]/ysize[0];
        int x = (int) (x1*1000);
        int y = (int) (y1*1000);

        if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS){
            float length = (float) Math.sqrt((x-lastMousePos[0])*(x-lastMousePos[0])+(y-lastMousePos[1])*(y-lastMousePos[1]));
            if(length!=0) {
                for (int i = 0; i <= length / radius; i++) {
                    int nx = (int) (lastMousePos[0] + (x - lastMousePos[0]) / length * i * radius);
                    int ny = (int) (lastMousePos[1] + (y - lastMousePos[1]) / length * i * radius);
                    for (int ix = nx - radius; ix <= nx + radius; ix++) {
                        for (int iy = ny - radius; iy <= ny + radius; iy++) {
                            if (grid.valid(ix, iy)) {
                                grid.set(ix, iy, null);
                                if (random.nextFloat() < density)
                                    grid.set(ix, iy, elements.getFromId(id));
                            }
                        }
                    }
                }
            }else {
                for (int ix = x - radius; ix <= x + radius; ix++) {
                    for (int iy = y - radius; iy <= y + radius; iy++) {
                        if (grid.valid(ix, iy)) {
                            grid.set(ix, iy, null);
                            if (random.nextFloat() < density)
                                grid.set(ix, iy, elements.getFromId(id));
                        }
                    }
                }
            }
        }

        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS) {
            float length = (float) Math.sqrt((x-lastMousePos[0])*(x-lastMousePos[0])+(y-lastMousePos[1])*(y-lastMousePos[1]));
            if(length!=0) {
                for (int i = 0; i <= length / radius; i++) {
                    int nx = (int) (lastMousePos[0] + (x - lastMousePos[0]) / length * i * radius);
                    int ny = (int) (lastMousePos[1] + (y - lastMousePos[1]) / length * i * radius);
                    for (int ix = nx - radius; ix <= nx + radius; ix++) {
                        for (int iy = ny - radius; iy <= ny + radius; iy++) {
                            if (grid.valid(ix, iy)) {
                                grid.set(ix, iy, null);
                            }
                        }
                    }
                }
            }else {
                for (int ix = x - radius; ix <= x + radius; ix++) {
                    for (int iy = y - radius; iy <= y + radius; iy++) {
                        if (grid.valid(ix, iy)) {
                            grid.set(ix, iy, null);
                        }
                    }
                }
            }
        }

        lastMousePos[0] = x;
        lastMousePos[1] = y;
    }

    public void render(EasyRender render) {
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
        float length = radius / 500f;
        render.line.drawLine2D(x1 - length, y1 - length, x1 + length, y1 - length, 1, 0, 0, 0.5f);
        render.line.drawLine2D(x1 - length, y1 - length, x1 - length, y1 + length, 1, 0, 0, 0.5f);
        render.line.drawLine2D(x1 + length, y1 - length, x1 + length, y1 + length, 1, 0, 0, 0.5f);
        render.line.drawLine2D(x1 - length, y1 + length, x1 + length, y1 + length, 1, 0, 0, 0.5f);

        float color[] = elements.getFromId(id).color();
        for (int x = 0; x < 32; x++)
            for (int y = 0; y < 32; y++) {
                if(random.nextFloat() < density)
                    render.pixel.drawPixel(x-128,y+800, color[0], color[1], color[2], color[3]);
            }
    }
}
