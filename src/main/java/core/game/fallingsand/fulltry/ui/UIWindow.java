package core.game.fallingsand.fulltry.ui;

import core.render.EasyRender;

import java.util.Map;

public abstract class UIWindow extends UIComponent{

    public Map<String, UIComponent> components;
    public String name;

    public UIWindow(UIManager manager, String name) {
        super(manager);
        this.name = name;
    }

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
}
