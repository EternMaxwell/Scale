package core.game.fallingsand;

import core.render.EasyRender;

public abstract class Grid {
    public abstract Element get(int x, int y);
    public abstract void set(int x, int y, Element element);
    public abstract Element replace(int x, int y, Element element);
    public abstract Element pop(int x, int y);
    public abstract void remove(int x, int y);
    public abstract double step();
    public abstract boolean valid(int x, int y);
    public void render(EasyRender render){
    }
    public int[] basePos(){
        return new int[]{0, 0};
    }
    public void action(double x, double y, int action, double[] arguments){
    }
}
