package core.game.fallingsand.fulltry.ui;

import core.game.fallingsand.fulltry.FallingData;
import core.game.fallingsand.fulltry.FallingInput;
import core.render.EasyRender;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class UIManager {
    public Map<String, UIPage> pages;
    public UIPage currentPage;
    public String currentName;
    FallingInput fallingInput = new FallingInput(FallingData.world.grid);

    public UIManager() {
        pages = new HashMap<>();
        addPage("default", new UIPage(this) {
            @Override
            public void render(EasyRender render) {
                fallingInput.render(render);
            }

            @Override
            public void handleInput() {
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_ESCAPE)) {
                    manager.setCurrent("menu");
                    FallingData.pause = true;
                }
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_P)) {
                    FallingData.pause = !FallingData.pause;
                }
                if(manager.currentName.equals("default"))
                    fallingInput.input(FallingData.window.id());
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_TAB)){
                    manager.setCurrent("world_tool");
                }
            }

            @Override
            public void update() {

            }
        });
        setCurrent("default");
        addPage("menu", new UIPage(this) {
            UIButton button = new UIButton(this.manager, -0.1f, 0.07f, 0.2f, 0.06f, "continue") {
                @Override
                public void render(EasyRender render) {
                    render.text.setViewMatrix(new Matrix4f().identity());
                    if(!isHovered())
                        render.text.drawTextRelative(centerX(),centerY(),0.05f,1,1,1,1,text,
                                new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                    else
                        render.text.drawTextRelative(centerX(),centerY(),0.05f,1,1,0,1,">"+text+"<",
                                new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                }

                @Override
                public void handleInput() {
                    if(isClicked()){
                        manager.setCurrent("default");
                        FallingData.pause = false;
                    }
                }

                @Override
                public void update() {

                }
            };
            UIButton button1 = new UIButton(this.manager, -0.05f, 0, 0.1f, 0.06f, "exit") {
                @Override
                public void render(EasyRender render) {
                    render.text.setViewMatrix(new Matrix4f().identity());
                    if(isHovered())
                        render.text.drawTextRelative(centerX(),centerY(),0.05f,1,1,0,1,text,
                                new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                    else
                        render.text.drawTextRelative(centerX(),centerY(),0.05f,1,1,1,1,">"+text+"<",
                                new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                }

                @Override
                public void handleInput() {
                    if(isClicked()){
                        glfwSetWindowShouldClose(FallingData.window.id(), true);
                    }
                }

                @Override
                public void update() {

                }
            };
            @Override
            public void render(EasyRender render) {
                render.triangle.setViewMatrix(new Matrix4f().identity());
                render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, render.window.ratio(), -1,
                        render.window.ratio(), 1, 0,0,0,0.5f);
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, -render.window.ratio(), 1,
                        render.window.ratio(), 1, 0,0,0,0.5f);

                render.text.drawTextRelative(0,0.5f,0.2f,1,1,1,1,"MENU", new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                render.triangle.flush();
                render.text.flush();
                button.render(render);
                button1.render(render);
            }

            @Override
            public void handleInput() {
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_ESCAPE)) {
                    manager.setCurrent("default");
                    FallingData.pause = false;
                }
                button.handleInput();
                button1.handleInput();
            }

            @Override
            public void update() {
                button.update();
                button1.update();
            }
        });
        addPage("world_tool", new UIPage(this) {
            UIScroller scroller = new UIScroller(this.manager, -0.3f, 0.3f, 0.3f, new UIButton(this.manager, 0, 0, 0.015f, 0.02f, "scroller") {
                @Override
                public void render(EasyRender render) {
                    render.triangle.setViewMatrix(new Matrix4f().identity());
                    if(!isHovered()) {
                        render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 1, 0.8f);
                        render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 1, 0.8f);
                    }else {
                        render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 0, 0.8f);
                        render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 0, 0.8f);
                    }
                }

                @Override
                public void handleInput() {

                }

                @Override
                public void update() {

                }
            }, 0.01f) {
                @Override
                public void render(EasyRender render) {
                    render.triangle.setViewMatrix(new Matrix4f().identity());
                    render.triangle.drawTriangle2D(x, y, x + length, y, x + length, y + height, 1,1,1,0.5f);
                    render.triangle.drawTriangle2D(x, y, x, y + height, x + length, y + height, 1,1,1,0.5f);
                    scroller.render(render);
                    render.triangle.flush();
                    render.text.setViewMatrix(new Matrix4f().identity());
                    render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                    render.text.drawTextRelative(-x,y + height / 2,0.04f,1,1,1,1,String.format("density: %2.2f", scrollValue()),
                            new java.awt.Font("Arial", Font.PLAIN, 64), 1, 0.5f);
                }

                @Override
                public void handleInput() {
                    super.handleInput();
                    fallingInput.density = scrollValue();
                }

                @Override
                public void update() {
                    scroller.update();
                }
            };
            @Override
            public void render(EasyRender render) {
                render.triangle.setViewMatrix(new Matrix4f().identity());
                render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, render.window.ratio(), -1,
                        render.window.ratio(), 1, 0,0,0,0.5f);
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, -render.window.ratio(), 1,
                        render.window.ratio(), 1, 0,0,0,0.5f);

                render.text.drawTextRelative(0,0.5f,0.1f,1,1,1,1,"WORLD_TOOL", new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                render.triangle.flush();
                render.text.flush();
                scroller.render(render);
            }

            @Override
            public void handleInput() {
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_ESCAPE) || FallingData.inputTool.isKeyJustPressed(GLFW_KEY_TAB)) {
                    manager.setCurrent("default");
                }
                scroller.handleInput();
            }

            @Override
            public void update() {

            }
        });
    }

    public void addPage(String name, UIPage page){
        pages.put(name, page);
    }

    public void setCurrent(String name){
        currentName = name;
        currentPage = pages.get(name);
    }

    public void render(EasyRender render) {
        if(currentPage == null) return;
        currentPage.render(render);
    }

    public void handleInput() {
        if(currentPage == null) return;
        currentPage.handleInput();
    }

    public void update() {
        if(currentPage == null) return;
        currentPage.update();
    }

    public String getCurrentName() {
        return currentName;
    }
}
