package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import core.game.fallingsand.fulltry.box2d.FallingBody;
import org.jbox2d.dynamics.World;

import java.util.Set;

public class EasyGrid extends Grid {

    Element[][] grid;
    boolean inverse = true;
    int tick = 0;

    public EasyGrid(int width, int height) {
        grid = new Element[width][height];
    }

    @Override
    public Element get(int x, int y) {
        if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            return grid[x][y];
        }
        return null;
    }

    @Override
    public void set(int x, int y, Element element) {
        if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            grid[x][y] = element;
        }
    }

    @Override
    public Element replace(int x, int y, Element element) {
        if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            Element old = grid[x][y];
            grid[x][y] = element;
            return old;
        }
        return null;
    }

    @Override
    public Element pop(int x, int y) {
        if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            Element old = grid[x][y];
            grid[x][y] = null;
            return old;
        }
        return null;
    }

    @Override
    public void remove(int x, int y) {
        if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            grid[x][y] = null;
        }
    }

    @Override
    public double step() {
        double start = System.nanoTime();
        if(inverse){
            for(int y = 0; y < grid[0].length; y++) {
                for(int x = grid.length-1; x >= 0; x--) {
                    if(grid[x][y] != null) {
                        grid[x][y].step(this, x, y, tick);
                    }
                }
            }
        }else {
            for (int y = 0; y < grid[0].length; y++) {
                for (int x = 0; x < grid.length; x++) {
                    if (grid[x][y] != null) {
                        grid[x][y].step(this, x, y, tick);
                    }
                }
            }
        }
        inverse = !inverse;
        tick++;
        return (System.nanoTime() - start) / 1000000;
    }

    @Override
    public boolean valid(int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    @Override
    public Set<FallingBody> toBodies(World world) {
        return null;
    }
}
