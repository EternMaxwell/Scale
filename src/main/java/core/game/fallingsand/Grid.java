package core.game.fallingsand;

public abstract class Grid {
    public abstract Element get(int x, int y);
    public abstract void set(int x, int y, Element element);
    public abstract Element replace(int x, int y, Element element);
    public abstract Element pop(int x, int y);
    public abstract void remove(int x, int y);
    public abstract double step();

    public abstract boolean valid(int x, int y);
}
