package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

public class Stone extends Element{

    int lastStepTick = -1;
    float[] color;

    public Stone(){
        float light = (float) (Math.random() * 0.2 + 0.8);
        color = new float[]{0.5f*light, 0.5f*light, 0.5f*light, 1};
    }

    /**
     * update the element.
     *
     * @param grid the grid.
     * @param x    the x position.
     * @param y    the y position.
     */
    @Override
    public boolean step(Grid grid, int x, int y, int tick) {
        lastStepTick = tick;
        return false;
    }

    /**
     * get the type of the element.
     *
     * @return the type.
     */
    @Override
    public int type() {
        return 2;
    }

    /**
     * get the color of the element.
     *
     * @return the color.
     */
    @Override
    public float[] color() {
        return color;
    }

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    @Override
    public int id() {
        return 2;
    }

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density() {
        return 5;
    }

    /**
     * get the last step tick of the element.
     *
     * @return the last step tick.
     */
    @Override
    public int lastStepTick() {
        return lastStepTick;
    }

    /**
     * get if the element is in free fall.
     * @return if the element is in free fall.
     */
    @Override
    public boolean freeFall() {
        return false;
    }
}
