package core.game.fallingsand.fulltry.ui;

import core.game.fallingsand.fulltry.FallingData;
import core.game.fallingsand.fulltry.InputTool;
import core.render.EasyRender;
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
            @Override
            public void render(EasyRender render) {

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
            }

            @Override
            public void update() {

            }
        });
        setCurrent("default");
        addPage("menu", new UIPage(this) {
            @Override
            public void render(EasyRender render) {

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
