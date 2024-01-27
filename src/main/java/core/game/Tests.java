package core.game;

import core.render.EasyRender;
import core.render.Window;

public interface Tests {
    public void init(Window window);
    public void input();
    public void update();
    public void render(EasyRender render);
    public void cleanup();
}
