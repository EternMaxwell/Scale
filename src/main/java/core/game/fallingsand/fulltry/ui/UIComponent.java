package core.game.fallingsand.fulltry.ui;

import core.render.EasyRender;

public abstract class UIComponent {
    public UIManager manager;
    public UIComponent(UIManager manager){
        this.manager = manager;
    }
    public abstract void render(EasyRender render);
    public abstract void handleInput();
    public abstract void update();
}
