package main;

import core.FrameTimer;
import core.TickTimer;
import core.game.CurvedSpaceTest;
import core.game.Tests;
import core.game.fallingsand.easyfallingsand.EasyFallingSandWorld;
import core.game.fallingsand.fulltry.FallingWorld;
import core.render.EasyRender;
import core.render.Window;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL33.glGenSamplers;
import static org.lwjgl.opengl.GL33.glSamplerParameteri;
import static org.lwjgl.stb.STBImage.*;

public class Application {
    public boolean debug = false;
    public Window window;
    public FrameTimer timer;
    public TickTimer tickTimer;
    public EasyRender renderer;
    public int prevWidth;
    public int prevHeight;
    public int prevPosX;
    public int prevPosY;

    public int testTexture;
    public int testSampler;

    public Tests test;

    public void init(){
        initGLFW();
        window = new Window(1600, 1024, "test");
        window.setContextVersionMajor(4);
        window.setContextVersionMinor(6);
        window.setResizable(true);
        window.setVisible(false);
        window.setFocused(true);
        window.createWindow();
//        window.setRefreshRate(60);
//        glfwSwapInterval(0);
        timer = new FrameTimer(60);
        tickTimer = new TickTimer(30);
        //====INITIALIZE THE GAME====//
        test = new FallingWorld();
        test.init(window);

        //====INITIALIZE THE RENDERER====//
        glfwMakeContextCurrent(window.id());
        GL.createCapabilities();
        renderer = new EasyRender(window);

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
//
//        //====LOAD TEXTURES====//
//        stbi_set_flip_vertically_on_load(true);
//        int[] width = new int[1];
//        int[] height = new int[1];
//        int[] channels = new int[1];
//        ByteBuffer buffer = stbi_load("src/test/resources/textures/test.png", width, height, channels, 4);
//        if(buffer == null){
//            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
//        }
//        glBindTexture(GL_TEXTURE_2D, testTexture);
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
//        glGenerateMipmap(GL_TEXTURE_2D);
//        MemoryUtil.memFree(buffer);
//        glBindTexture(GL_TEXTURE_2D, 0);
//
//        //====LOAD SAMPLERS====//
//        testSampler = glGenSamplers();
//        glSamplerParameteri(testSampler, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//        glSamplerParameteri(testSampler, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//        glSamplerParameteri(testSampler, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        glSamplerParameteri(testSampler, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

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
        test.input();
    }

    public void update(){
        //====UPDATE THE GAME====//
        test.update();
    }

    public void render(){
        renderer.begin();
        //====RENDER THE GAME====//
        float ratio = window.width() / (float) window.height();
        renderer.line.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));
        renderer.triangle.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));
        renderer.pixel.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));
        renderer.image.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));
        renderer.point.setProjectionMatrix(new Matrix4f().ortho(-ratio, ratio, -1, 1, -1, 1));
        int height = window.height();
        int width = window.width();
        renderer.text.setProjectionMatrix(new Matrix4f().ortho(0, width, 0, height, -1, 1));
//
//        float rate = 1.35f;
//        renderer.image.drawTexture(-1 * rate,-1 * rate,2 * rate,2 * rate,1,1, 1,0.8f,0,0,1,1, testTexture, testSampler);

        test.render(renderer);

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
