package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

public class EasyGrid extends Grid {

    Element[][] grid;

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
    public void step() {
        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[0].length; y++) {
                if(grid[x][y] != null) {
                    grid[x][y].step(this, x, y);
                }
            }
        }
    }

    @Override
    public boolean valid(int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
}
