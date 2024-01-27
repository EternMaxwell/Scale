package core.game.fallingsand;

import core.game.Tests;
import core.render.EasyRender;
import core.render.Window;

public abstract class FallingSandWorld implements Tests {
    @Override
    public abstract void init(Window window);

    @Override
    public abstract void input();

    @Override
    public abstract void update();

    @Override
    public abstract void render(EasyRender render);

    @Override
    public abstract void cleanup();
}