package core.game.fallingsand;

public abstract class Element {

    /**
     * update the element.
     *
     * @param grid the grid.
     * @param x    the x position.
     * @param y    the y position.
     */
    public abstract void step(Grid grid, int x, int y);

    /**
     * get the type of the element.
     *
     * @return the type.
     */
    public abstract int type();

    /**
     * get the color of the element.
     *
     * @return the color.
     */
    public abstract float[] color();

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    public abstract int id();
}
