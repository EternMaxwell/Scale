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
        boolean debug = true;
        initGLFW();
        Window window = new Window(800, 600, "test");
        window.setContextVersionMajor(4);
        window.setContextVersionMinor(6);
        window.setResizable(true);
        window.setVisible(false);
        window.setFocused(true);
        window.createWindow();
        FrameTimer timer = new FrameTimer(60);
        TickTimer tickTimer = new TickTimer(20);
        //====INITIALIZE THE GAME====//

        window.showWindow();
        glfwMakeContextCurrent(window.id());
        GL.createCapabilities();
        EasyRender renderer = new EasyRender();

        //====SET UP CALLBACKS====//
        window.setFramebufferSizeCallback(new GLFWFramebufferSizeCallback() {
                                              @Override
                                              public void invoke(long window1, int width, int height) {
                                                  glViewport(0, 0, width, height);

                                                  timer.frame();
                                                  if (debug)
                                                      System.out.println("frame");
                                                  tickTimer.tick();
                                                  if (debug)
                                                      System.out.println("tick");
                                                  renderer.begin();
                                                  //====RENDER THE GAME====//
                                                  renderer.drawTriangleTest();

                                                  renderer.end(window);
                                              }
                                          }
        );
        glfwSetKeyCallback(window.id(), (window1, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_F11 && action == GLFW_RELEASE) {
                if(glfwGetWindowMonitor(window.id()) != glfwGetPrimaryMonitor()) {
                    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                    window.setMonitor(glfwGetPrimaryMonitor(), 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
                }else {
                    window.setMonitor(0, 0, 0, 800, 600, GLFW_DONT_CARE);
                    window.setPosition(200,200);
                }
            }
        });
        while (!glfwWindowShouldClose(window.id())) {
            glfwPollEvents();
            timer.frame();
            if (debug)
                System.out.println("frame");
            //====UPDATE THE GAME====//
            if (tickTimer.tick()) {
                //====TICK THE GAME====//
                if (debug)
                    System.out.println("tick");

            }

            renderer.begin();
            //====RENDER THE GAME====//
            renderer.line.setModelMatrix(new Matrix4f().ortho(0, 1, 0, 1, -1, 1));
            renderer.line.drawLine(0, 0, 0.5f, 0.5f, 1, 0, 0, 1);

            renderer.end(window);
        }
    }

    public static void initGLFW() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        } else {
            System.out.println("GLFW initialized");
        }
    }
}
