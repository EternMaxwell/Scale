package core.game.fallingsand;

public abstract class Grid {
    public abstract Element get(int x, int y);
    public abstract void set(int x, int y, Element element);
    public abstract void step();
}
