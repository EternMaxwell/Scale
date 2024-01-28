package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

import java.util.Random;

public class Water extends Element {

    float[] color;
    float velocity = 0.7f;

    public Water() {
        color = new float[]{0, 0, 1,1};
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
            return;
        }

        int dir = new Random().nextInt(2);
        dir = dir == 0 ? -1 : 1;
        Element diagnose = grid.get(ix + dir, iy - 1);
        if (diagnose == null && grid.valid(ix + dir, iy - 1)) {
            grid.set(ix + dir, iy - 1, this);
            grid.set(ix, iy, null);
            moved = true;
        } else {
            diagnose = grid.get(ix - dir, iy - 1);
            if (diagnose == null && grid.valid(ix - dir, iy - 1)) {
                grid.set(ix - dir, iy - 1, this);
                grid.set(ix, iy, null);
                moved = true;
            }
        }

        if(!moved){
            velocity = 0.7f;
            for(int i = 5;i>0; i--){
                Element side = grid.get(ix + dir*i, iy);
                if (side == null && grid.valid(ix + dir*i, iy)) {
                    grid.set(ix + dir*i, iy, this);
                    grid.set(ix, iy, null);
                    break;
                } else {
                    side = grid.get(ix - dir*i, iy);
                    if (side == null && grid.valid(ix - dir*i, iy)) {
                        grid.set(ix - dir*i, iy, this);
                        grid.set(ix, iy, null);
                        break;
                    }
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
        return 0;
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
}
