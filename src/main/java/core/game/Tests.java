package core.game;

import core.render.EasyRender;

public interface Tests {
    public void init();
    public void input();
    public void update();
    public void render(EasyRender render);
    public void cleanup();
}
