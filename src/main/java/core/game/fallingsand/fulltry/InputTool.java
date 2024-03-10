package core.game.fallingsand.fulltry;

import core.render.Window;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class InputTool {
    private Window window;

    private double[] mousePos;
    private double[] mousePosLast;

    private Map<Integer, Integer> keyMap;
    private Map<Integer, Integer> keyMapLast;
    private Map<Integer, Integer> mouseMap;
    private Map<Integer, Integer> mouseMapLast;

    public InputTool(Window window) {
        this.window = window;
        keyMap = new HashMap<>();
        keyMapLast = new HashMap<>();
        mouseMap = new HashMap<>();
        mouseMapLast = new HashMap<>();
        mousePos = new double[2];
        mousePosLast = new double[2];
    }

    public void input(){
        Map<Integer, Integer> temp = keyMapLast;
        keyMapLast = keyMap;
        keyMap = temp;
        temp = mouseMapLast;
        mouseMapLast = mouseMap;
        mouseMap = temp;
        for(Map.Entry<Integer, Integer> entry : keyMap.entrySet()){
            entry.setValue(glfwGetKey(window.id(), entry.getKey()));
        }
        for(Map.Entry<Integer, Integer> entry : mouseMap.entrySet()){
            entry.setValue(glfwGetMouseButton(window.id(), entry.getKey()));
        }

        mousePosLast = mousePos;
        mousePos = mousePos();
    }

    public boolean isKeyPressed(int key){
        keyMap.putIfAbsent(key, 0);
        keyMapLast.putIfAbsent(key, 0);
        return keyMap.get(key) == GLFW_PRESS;
    }

    public boolean isKeyJustPressed(int key){
        keyMap.putIfAbsent(key, 0);
        keyMapLast.putIfAbsent(key, 0);
        return keyMap.get(key) == GLFW_PRESS && keyMapLast.get(key) == GLFW_RELEASE;
    }

    public boolean isKeyReleased(int key){
        keyMap.putIfAbsent(key, 0);
        keyMapLast.putIfAbsent(key, 0);
        return keyMap.get(key) == GLFW_RELEASE;
    }

    public boolean isKeyJustReleased(int key){
        keyMap.putIfAbsent(key, 0);
        keyMapLast.putIfAbsent(key, 0);
        return keyMap.get(key) == GLFW_RELEASE && keyMapLast.get(key) == GLFW_PRESS;
    }

    public boolean isMousePressed(int key){
        mouseMap.putIfAbsent(key, 0);
        mouseMapLast.putIfAbsent(key, 0);
        return mouseMap.get(key) == GLFW_PRESS;
    }

    public boolean isMouseJustPressed(int key){
        mouseMap.putIfAbsent(key, 0);
        mouseMapLast.putIfAbsent(key, 0);
        return mouseMap.get(key) == GLFW_PRESS && mouseMapLast.get(key) == GLFW_RELEASE;
    }

    public boolean isMouseReleased(int key){
        mouseMap.putIfAbsent(key, 0);
        mouseMapLast.putIfAbsent(key, 0);
        return mouseMap.get(key) == GLFW_RELEASE;
    }

    public boolean isMouseJustReleased(int key){
        mouseMap.putIfAbsent(key, 0);
        mouseMapLast.putIfAbsent(key, 0);
        return mouseMap.get(key) == GLFW_RELEASE && mouseMapLast.get(key) == GLFW_PRESS;
    }

    public double[] mousePos(){
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(window.id(), x, y);

        return new double[]{2 * x[0] / window.width() - 1, 1 - 2 * y[0] / window.height()};
    }

    public double mousePosX(){
        return mousePos()[0];
    }

    public double mousePosY(){
        return mousePos()[1];
    }

    public double[] mousePosLast(){
        return mousePosLast;
    }

    public double mousePosLastX(){
        return mousePosLast()[0];
    }

    public double mousePosLastY(){
        return mousePosLast()[1];
    }
}
