package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

import java.util.Random;

public class Water extends Element {

    boolean falling = true;
    int lastStepTick = -1;
    float[] color;
    float velocity = 0.7f;
    int dir = Math.random() > 0.5 ? 1 : -1;
    boolean lastBlocked = false;

    public Water() {
        color = new float[]{0.1f, 0.1f, 1, 0.8f};
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
                grid.set(x, iy - 1, this);
                grid.set(x, iy, null);
                lastStepTick = tick;
                iy--;
            } else if (below != null) {
                if(falling != grid.get(x, iy - 1).freeFall())
                    //dir = Math.random() > 0.5 ? 1 : -1;
                    falling = grid.get(x, iy - 1).freeFall();
                break;
            } else {
                falling = false;
                lastStepTick = tick;
                velocity = 0.7f;
                return moved;
            }
        }
        if (moved) {
            lastStepTick = tick;
            velocity += 0.1f;
            velocity *= 0.98f;
            return true;
        }

        if(!falling)
            velocity = 0.7f;

        Element diagnose = grid.get(x + dir, iy - 1);
        if (diagnose == null && grid.valid(x + dir, iy - 1)) {
            grid.set(x + dir, iy - 1, this);
            grid.set(x, iy, null);
            moved = true;
        } else {
            diagnose = grid.get(x - dir, iy - 1);
            if (diagnose == null && grid.valid(x - dir, iy - 1)) {
                grid.set(x - dir, iy - 1, this);
                grid.set(x, iy, null);
                moved = true;
            }
        }

        if (!moved) {
            velocity = 0.7f;
            int dirL = 0;
            boolean dirBlock = false;
            for (int i = 1; i <= 3; i++) {
                Element side = grid.get(x + dir * i, iy);
                if (side == null && grid.valid(x + dir * i, iy)) {
                    dirL++;
                } else {
                    dirBlock = true;
                    break;
                }
            }
            if (dirL > 0) {
                grid.set(x + dir * dirL, iy, this);
                grid.set(x, iy, null);
                moved = true;
            }else{
                if(lastBlocked && dirBlock){
                    dir = -dir;
                    for(int i = 1; i <= 3; i++){
                        Element side = grid.get(x + dir * i, iy);
                        if (side == null && grid.valid(x + dir * i, iy)) {
                            dirL++;
                        } else {
                            break;
                        }
                    }
                    if (dirL > 0) {
                        grid.set(x + dir * dirL, iy, this);
                        grid.set(x, iy, null);
                        moved = true;
                    }
                }else {
                    grid.set(x, iy, this);
                }
            }
            lastBlocked = dirBlock;
        }

        if (moved) {
            lastStepTick = tick;
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
        return 1;
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
        return 1;
    }

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density() {
        return 1;
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
        return falling;
    }

    /**
     * get the default color of the element.
     * @return the default color.
     */
    @Override
    public float[] defaultColor() {
        return new float[]{0.1f, 0.1f, 1, 0.8f};
    }
}
