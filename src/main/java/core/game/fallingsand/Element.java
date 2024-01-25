package core.game.fallingsand;

public abstract class Element {
    /**
     * get the grid instance the element is in.
     * @return the grid.
     */
    public abstract Grid getGrid();

    /**
     * update the element.
     */
    public abstract void step();

    /**
     * get the position of the element.
     * @return the position.
     */
    public abstract int[] pos();

    /**
     * get the type of the element.
     * @return the type.
     */
    public abstract int type();

    /**
     * get the id of the element.
     * @return the id.
     */
    public abstract int id();
}
