package main;

import core.FrameTimer;
import core.TickTimer;
import core.render.EasyRender;
import core.render.Window;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import javax.swing.plaf.PanelUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

public class Application {
    public boolean debug = true;
    public Window window;
    public FrameTimer timer;
    public TickTimer tickTimer;
    public EasyRender renderer;
    public int prevWidth;
    public int prevHeight;
    public int prevPosX;
    public int prevPosY;

    public int testTexture;

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
        glfwMakeContextCurrent(window.id());
        GL.createCapabilities();
        renderer = new EasyRender();

        //====SET UP CALLBACKS====//
        prevWidth = window.width();
        prevHeight = window.height();
        prevPosX = window.xpos();
        prevPosY = window.ypos();
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
                    prevWidth = window.width();
                    prevHeight = window.height();
                    prevPosX = window.xpos();
                    prevPosY = window.ypos();
                    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                    window.setMonitor(glfwGetPrimaryMonitor(), 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
                }else {
                    window.setMonitor(0, prevPosX, prevPosY, prevWidth, prevHeight, GLFW_DONT_CARE);
                }
            }
        });

        BufferedImage image;
        try {
            image = ImageIO.read(new File("src/test/resources/textures/test1.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        testTexture = glGenTextures();
        ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
        for (int j = image.getHeight()-1; j >= 0; j--) {
            for (int i = 0; i < image.getWidth(); i++) {
                int pixel = image.getRGB(i, j);
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
                buffer.put((byte) (pixel & 0xFF));         // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
            }
        }
        buffer.flip();
        glBindTexture(GL_TEXTURE_2D, testTexture);
        glTexImage2D(GL_TEXTURE_2D, 1, GL_RGBA32F, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        MemoryUtil.memFree(buffer);
        glBindTexture(GL_TEXTURE_2D, 0);

        //====SHOW THE WINDOW====//
        window.showWindow();
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
        renderer.triangle.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));
        renderer.pixel.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));
        renderer.image.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));

        renderer.triangle.drawTriangle(0,0,0,0.1f,.1f,.1f,0,1,1,1);
        renderer.line.drawLine(0, 0, 1, 1, 1, 1, 1, 1);
        renderer.pixel.setPixelSize(0.1f);
        renderer.pixel.drawPixel(-0.5f,-0.5f,1,1,1,1);

        renderer.image.drawTexture(-0.5f,-0.5f,1,1,1,1, 1,0.5f,0,0,1,1, testTexture);

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
