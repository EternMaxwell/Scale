package main;

import core.FrameTimer;
import core.TickTimer;
import core.render.EasyRender;
import core.render.Window;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Main {
    public static void main(String[] args) {
        boolean debug = true;
        initGLFW();
        Window window = new Window(800, 600, "test");
        window.setContextVersionMajor(4);
        window.setContextVersionMinor(6);
        window.setVisible(false);
        window.setFocused(true);
        window.createWindow();
        FrameTimer timer = new FrameTimer(60);
        TickTimer tickTimer = new TickTimer(20);
        //====INITIALIZE THE GAME====//
        EasyRender renderer = new EasyRender();

        window.setFramebufferSizeCallback(new GLFWFramebufferSizeCallback() {
                    @Override
                    public void invoke(long window1, int width, int height) {
                        glViewport(0, 0, width, height);

                        timer.frame();
                        if(debug)
                            System.out.println("frame");
                        tickTimer.tick();
                        if(debug)
                            System.out.println("tick");
                        renderer.begin();
                        //====RENDER THE GAME====//
                        renderer.drawTriangleTest();

                        renderer.end(window);
                    }
                }
        );
        window.showWindow();
        glfwMakeContextCurrent(window.id());
        GL.createCapabilities();
        while (!glfwWindowShouldClose(window.id())) {
            glfwPollEvents();
            timer.frame();
            if(debug)
                System.out.println("frame");
            //====UPDATE THE GAME====//
            if(tickTimer.tick()){
                //====TICK THE GAME====//
                if(debug)
                    System.out.println("tick");

            }

            renderer.begin();
            //====RENDER THE GAME====//
            renderer.drawTriangleTest();

            renderer.end(window);
        }
    }

    public static void initGLFW(){
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }else {
            System.out.println("GLFW initialized");
        }
    }
}
