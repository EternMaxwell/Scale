package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

public class ChunkBasedSimpleGrid extends Grid {
    public class Chunk {
        public Element[][] grid;
        public int x;
        public int y;
        public final int width = 64;
        public Chunk(int x, int y) {
            this.x = x;
            this.y = y;
            grid = new Element[width][width];
        }

        public Element get(int x, int y) {
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
                return grid[x][y];
            }
            return null;
        }

        public void set(int x, int y, Element element) {
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
                grid[x][y] = element;
            }
        }

        public Element replace(int x, int y, Element element) {
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
                Element old = grid[x][y];
                grid[x][y] = element;
                return old;
            }
            return null;
        }

        public Element pop(int x, int y) {
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
                Element old = grid[x][y];
                grid[x][y] = null;
                return old;
            }
            return null;
        }

        public void remove(int x, int y) {
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
                grid[x][y] = null;
            }
        }
    }

    public Chunk[][] chunks;

    public ChunkBasedSimpleGrid(int chunkWidth, int chunkHeight) {
        chunks = new Chunk[chunkWidth][chunkHeight];
        for(int x = 0; x < chunks.length; x++) {
            for(int y = 0; y < chunks[0].length; y++) {
                chunks[x][y] = new Chunk(x, y);
            }
        }
    }

    @Override
    public Element get(int x, int y) {
        try {
            return chunks[x >> 6][y >> 6].get(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void set(int x, int y, Element element) {
        try {
            chunks[x >> 6][y >> 6].set(x & 63, y & 63, element);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public Element replace(int x, int y, Element element) {
        try {
            return chunks[x >> 6][y >> 6].replace(x & 63, y & 63, element);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Element pop(int x, int y) {
        try {
            return chunks[x >> 6][y >> 6].pop(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void remove(int x, int y) {
        try {
            chunks[x >> 6][y >> 6].remove(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public double step() {
        double start = System.nanoTime();
        for(int y = chunks[0][0].y * chunks[0][0].width; y < chunks[0].length * chunks[0][0].width + chunks[0][0].width; y++) {
            for(int x = chunks[0][0].x * chunks[0][0].width; x < chunks[0].length * chunks[0][0].width + chunks[0][0].width; x++) {
                if(get(x, y) != null) {
                    get(x, y).step(this, x, y);
                }
            }
        }
        return (System.nanoTime() - start) / 1e6;
    }

    @Override
    public boolean valid(int x, int y) {
        return x >= 0 && x < chunks.length * chunks[0][0].width && y >= 0 && y < chunks[0].length * chunks[0][0].width;
    }
}
