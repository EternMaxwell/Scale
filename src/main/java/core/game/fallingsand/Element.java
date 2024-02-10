package core.game.fallingsand;

public abstract class Element {

    /**
     * update the element.
     *
     * @param grid the grid.
     * @param x    the x position.
     * @param y    the y position.
     */
    public abstract boolean step(Grid grid, int x, int y, int tick);

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

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    public abstract float density();

    /**
     * get the last step tick of the element.
     *
     * @return the last step tick.
     */
    public abstract int lastStepTick();
}
