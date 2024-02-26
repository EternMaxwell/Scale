package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import org.apache.logging.log4j.core.appender.RandomAccessFileManager;

import java.util.Random;

public class Sand extends Element {

    boolean falling = true;
    int lastTick = -1;
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
    public boolean step(Grid grid, int x, int y, int tick) {
        int distance = velocity > 1 ? (int) velocity : 1;
        int iy = y;
        boolean moved = false;
        for (int i = 0; i < distance; i++) {
            Element below = grid.get(x, iy - 1);
            if (below == null && grid.valid(x, iy - 1)) {
                falling = true;
                moved = true;
                grid.set(x, iy -1, this);
                grid.set(x, iy, null);
                lastTick = tick;
                iy--;
            }else if(below != null){
                falling = grid.get(x, iy - 1).freeFall();
                break;
            }else {
                falling = false;
                lastTick = tick;
                velocity = 0.7f;
                return moved;
            }
        }
        if(moved){
            lastTick = tick;
            velocity += 0.1f;
            velocity *= 0.985f;
            return true;
        }

        if(!falling)
            velocity = 0.7f;

        if(grid.get(x, iy - 1)!=null && grid.get(x, iy - 1).type() ==1){
            grid.set(x, iy, grid.get(x, iy - 1));
            grid.set(x, iy - 1, this);
            lastTick = tick;
            return true;
        }

        int dir = Math.random()>=0.5?1:-1;
        Element side = grid.get(x + dir, iy - 1);
        if (side == null && grid.valid(x + dir, iy - 1)) {
            grid.set(x + dir, iy - 1, this);
            grid.set(x, iy, null);
            moved = true;
        } else {
            side = grid.get(x - dir, iy - 1);
            if (side == null && grid.valid(x - dir, iy - 1)) {
                grid.set(x - dir, iy - 1, this);
                grid.set(x, iy, null);
                moved = true;
            }
        }

        if (!moved) {
            side = grid.get(x + dir, iy-1);
            if(side != null && side.type()==1 ){
                grid.set(x + dir, iy-1, this);
                grid.set(x, iy, side);
                moved = true;
            }else {
                side = grid.get(x - dir, iy-1);
                if(side != null && side.type()==1 ){
                    grid.set(x - dir, iy-1, this);
                    grid.set(x, iy, side);
                    moved = true;
                }
            }
        }
        if(moved){
            lastTick = tick;
        }

        return moved;
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

    /**
     * get the last step tick of the element.
     *
     * @return the last step tick.
     */
    @Override
    public int lastStepTick() {
        return lastTick;
    }

    /**
     * get if the element is in free fall.
     *
     * @return if the element is in free fall.
     */
    @Override
    public boolean freeFall() {
        return falling;
    }

    /**
     * get the default color of the element.
     * @return the default color.
     */
    @Override
    public float[] defaultColor() {
        return new float[]{0.8f, 0.8f, 0.01f, 1};
    }
}
