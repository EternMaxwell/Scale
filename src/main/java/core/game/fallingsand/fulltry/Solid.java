package core.game.fallingsand.fulltry;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

public abstract class Solid extends Element {
    /**
     * @return Solid type.
     */
    @Override
    public int type(){
        return FallingType.SOLID;
    }

    /**
     * step for Solid won't do anything.
     * @param grid the grid.
     * @param x    the x position.
     * @param y    the y position.
     * @param tick the tick.
     * @return false.
     */
    @Override
    public boolean step(Grid grid, int x, int y, int tick){
        return false;
    }

    /**
     * free fall for Solid will always return false.
     * @return false.
     */
    @Override
    public boolean freeFall(){
        return false;
    }

    /**
     * last step tick for Solid will always return -1.
     * @return -1.
     */
    @Override
    public int lastStepTick(){
        return -1;
    }
}
