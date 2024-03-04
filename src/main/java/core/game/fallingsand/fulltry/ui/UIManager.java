package core.game.fallingsand.fulltry.ui;

import core.game.fallingsand.fulltry.FallingData;
import core.game.fallingsand.fulltry.FallingInput;
import core.game.fallingsand.fulltry.InputTool;
import core.render.EasyRender;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashMap;
import java.util.Map;

public class UIManager {
    public Map<String, UIPage> pages;
    public UIPage currentPage;
    public String currentName;

    public UIManager() {
        pages = new HashMap<>();
        addPage("default", new UIPage(this) {
            FallingInput fallingInput = new FallingInput(FallingData.world.grid);
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
                fallingInput.input(FallingData.window.id());
            }

            @Override
            public void update() {

            }
        });
        setCurrent("default");
        addPage("menu", new UIPage(this) {
            @Override
            public void render(EasyRender render) {
                render.triangle.setViewMatrix(new Matrix4f().identity());
                render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, render.window.ratio(), -1,
                        render.window.ratio(), 1, 0,0,0,0.5f);
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, -render.window.ratio(), 1,
                        render.window.ratio(), 1, 0,0,0,0.5f);

                render.text.drawTextRelative(0,0,0.1f,1,1,1,1,"MENU", new java.awt.Font("Arial", java.awt.Font.PLAIN, 128), 0.5f, 0.5f);
                render.triangle.flush();
                render.text.flush();
            }

            @Override
            public void handleInput() {
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_ESCAPE)) {
                    manager.setCurrent("default");
                    FallingData.pause = false;
                }
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
}
