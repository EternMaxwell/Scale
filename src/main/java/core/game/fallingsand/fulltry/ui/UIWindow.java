package core.game.fallingsand.fulltry.ui;

import core.game.fallingsand.fulltry.FallingData;
import core.render.EasyRender;

import java.util.Map;

public abstract class UIWindow extends UIComponent{

    public float x, y, width, height;
    public Map<String, UIComponent> components;
    public String name;

    public UIWindow(UIManager manager, String name, float x, float y, float width, float height) {
        super(manager);
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        components = new java.util.HashMap<>();
        init();
    }

    public abstract void init();

    public abstract void renderOwn(EasyRender render);

    public abstract void handleInputOwn();

    public abstract void updateOwn();

    public abstract void moveOwn(float x, float y);

    public void addComponent(String name, UIComponent component){
        components.put(name, component);
    }

    @Override
    public void render(EasyRender render) {
        renderOwn(render);
        for(UIComponent component : components.values()){
            component.render(render);
        }
    }

    @Override
    public void handleInput() {
        handleInputOwn();
        for(UIComponent component : components.values()){
            component.handleInput();
        }
    }

    @Override
    public void update() {
        updateOwn();
        for(UIComponent component : components.values()){
            component.update();
        }
    }

    @Override
    public void move(float x, float y) {
        moveOwn(x,y);
        for(UIComponent component : components.values()){
            component.move(x,y);
        }
    }

    public boolean isHovered() {
        float mouseX = (float) FallingData.inputTool.mousePosX();
        float mouseY = (float) FallingData.inputTool.mousePosY();
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
}
