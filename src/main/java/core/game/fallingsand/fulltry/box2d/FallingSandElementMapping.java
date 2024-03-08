package core.game.fallingsand.fulltry.box2d;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

public class FallingSandElementMapping extends Element {
    /**
     * update the element.
     *
     * @param grid the grid.
     * @param x    the x position.
     * @param y    the y position.
     * @param tick
     */
    @Override
    public boolean step(Grid grid, int x, int y, int tick) {
        return false;
    }

    /**
     * get the type of the element.
     *
     * @return the type.
     */
    @Override
    public int type() {
        return 0;
    }

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    @Override
    public int id() {
        return 0;
    }

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density() {
        return 0;
    }

    /**
     * get the last step tick of the element.
     *
     * @return the last step tick.
     */
    @Override
    public int lastStepTick() {
        return 0;
    }

    /**
     * get if the element is in free fall.
     *
     * @return if the element is in free fall.
     */
    @Override
    public boolean freeFall() {
        return false;
    }

    /**
     * get the default color of the element.
     *
     * @return the default color.
     */
    @Override
    public float[] defaultColor() {
        return new float[0];
    }
}
