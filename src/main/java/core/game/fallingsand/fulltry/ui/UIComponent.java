package core.game.fallingsand.fulltry.ui;

import core.render.EasyRender;

public abstract class UIComponent {

    public abstract void render(EasyRender render);
    public abstract void handleInput();
}
