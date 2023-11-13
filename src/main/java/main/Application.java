package main;

import core.FrameTimer;
import core.TickTimer;
import core.render.EasyRender;
import core.render.Window;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import javax.swing.plaf.PanelUI;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class Application {
    public boolean debug = true;
    public Window window;
    public FrameTimer timer;
    public TickTimer tickTimer;
    public EasyRender renderer;

    public void init(){
        initGLFW();
        window = new Window(800, 600, "test");
        window.setContextVersionMajor(4);
        window.setContextVersionMinor(6);
        window.setResizable(true);
        window.setVisible(false);
        window.setFocused(true);
        window.createWindow();
        timer = new FrameTimer(60);
        tickTimer = new TickTimer(20);
        //====INITIALIZE THE GAME====//

        //====INITIALIZE THE RENDERER====//
        window.showWindow();
        glfwMakeContextCurrent(window.id());
        GL.createCapabilities();
        renderer = new EasyRender();

        //====SET UP CALLBACKS====//
        window.setFramebufferSizeCallback(new GLFWFramebufferSizeCallback() {
                                              @Override
                                              public void invoke(long window1, int width, int height) {
                                                  glViewport(0, 0, width, height);

                                                  timer.frame();
                                                  if (debug)
                                                      System.out.println("frame");
                                                  if (tickTimer.tick()) {
                                                      if (debug)
                                                          System.out.println("tick");
                                                      update();
                                                  }
                                                  render();
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
    }

    public void initGLFW(){
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }else{
            System.out.println("GLFW initialized");
        }
    }

    public void loop(){
        while(running()){
            input();
            timer.frame();
            if(debug)
                System.out.println("frame");
            if(tickTimer.tick()){
                if(debug)
                    System.out.println("tick");
                update();
            }
            render();
        }
    }

    public boolean running(){
        return !glfwWindowShouldClose(window.id());
    }

    public void input(){
        //====INPUT THE GAME====//
        glfwPollEvents();
    }

    public void update(){
        //====UPDATE THE GAME====//
    }

    public void render(){
        renderer.begin();
        //====RENDER THE GAME====//
        float ratio = window.width() / (float) window.height();
        renderer.line.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));
        renderer.line.drawLine(0, 0, 1, 1, 1, 1, 1, 1);

        renderer.end(window);
    }

    public void destroy(){
        //====DESTROY THE GAME====//
        renderer.dispose();
        window.dispose();
    }

    public void run(){
        init();
        loop();
        destroy();
    }
}
