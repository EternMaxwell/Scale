package main;

import core.FrameTimer;
import core.TickTimer;
import core.render.EasyRender;
import core.render.Window;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Main {
    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}
