package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import org.apache.logging.log4j.core.appender.RandomAccessFileManager;

import java.util.Random;

public class Sand extends Element {

    float[] color;
    float velocity;

    public Sand(){
        Random random = new Random();
        float l = random.nextFloat();
        color = new float[]{0.75f+0.15F*l, 0.7f+0.15f*l, 0.01f*(1f-l), 1};
        velocity = 0.7f;
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
        int distance = velocity > 1 ? (int) velocity : 1;
        int ix = x;
        int iy = y;
        boolean moved = false;
        for (int i = 0; i < distance; i++) {
            Element below = grid.get(ix, iy - 1);
            if (below == null && grid.valid(ix, iy - 1)) {
                moved = true;
                grid.set(ix, iy -1, this);
                grid.set(ix, iy, null);
                iy--;
            }else if(below != null){
                break;
            }else {
                velocity = 0.7f;
                return;
            }
        }
        if(moved){
            velocity += 0.1f;
            velocity *= 0.985f;
            return;
        }

        velocity = 0.7f;

        if(grid.get(ix, iy - 1)!=null && grid.get(ix, iy - 1).type() ==1){
            grid.set(ix, iy, grid.get(ix, iy - 1));
            grid.set(ix, iy - 1, this);
            return;
        }

        int dir = new Random().nextInt(2);
        dir = dir == 0 ? -1 : 1;
        Element side = grid.get(ix + dir, iy - 1);
        if (side == null && grid.valid(ix + dir, iy - 1)) {
            grid.set(ix + dir, iy - 1, this);
            grid.set(ix, iy, null);
            moved = true;
        } else {
            side = grid.get(ix - dir, iy - 1);
            if (side == null && grid.valid(ix - dir, iy - 1)) {
                grid.set(ix - dir, iy - 1, this);
                grid.set(ix, iy, null);
                moved = true;
            }
        }

        if (!moved) {
            side = grid.get(ix + dir, iy-1);
            if(side != null && side.type()==1 ){
                grid.set(ix + dir, iy-1, this);
                grid.set(ix, iy, side);
                moved = true;
            }else {
                side = grid.get(ix - dir, iy-1);
                if(side != null && side.type()==1 ){
                    grid.set(ix - dir, iy-1, this);
                    grid.set(ix, iy, side);
                    moved = true;
                }
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

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density(){
        return 3;
    }
}
