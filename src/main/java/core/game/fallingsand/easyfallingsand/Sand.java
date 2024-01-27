package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import org.apache.logging.log4j.core.appender.RandomAccessFileManager;

import java.util.Random;

public class Sand extends Element {

    float[] color;

    public Sand(){
        Random random = new Random();
        color = new float[]{0.8f+0.2F*random.nextFloat(), 0.8f+0.2f*random.nextFloat(), 0.01f*random.nextFloat(), 1};
    }

    /**
     * update the element.
     *
     * @param grid the grid.
     * @param x    the x position.
     * @param y    the y position.
     */
    @Override
    public void step(Grid grid, int x, int y) {
        Element below = grid.get(x, y - 1);
        if(below == null){
            if (grid.valid(x, y - 1)) {
                grid.set(x, y - 1, this);
                grid.set(x, y, null);
            }
        }else {
            if(grid.valid(x - 1, y - 1) && grid.get(x - 1, y - 1) == null){
                grid.set(x - 1, y - 1, this);
                grid.set(x, y, null);
            }else if(grid.valid(x + 1, y - 1) && grid.get(x + 1, y - 1) == null){
                grid.set(x + 1, y - 1, this);
                grid.set(x, y, null);
            }
        }
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
        return 0;
    }
}
