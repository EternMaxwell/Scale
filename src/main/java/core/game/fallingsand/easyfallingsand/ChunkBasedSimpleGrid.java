package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

import java.util.*;

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
    boolean inverse = false;
    int tick = 0;
    Deque<int[]> stepping = new ArrayDeque<>(4096);
    Deque<int[]> toStep = new ArrayDeque<>(4096);
    Deque<Integer> stack = new ArrayDeque<>(8192);

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
        addToStep(x, y, element);
        try {
            chunks[x >> 6][y >> 6].set(x & 63, y & 63, element);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public Element replace(int x, int y, Element element) {
        addToStep(x, y, element);
        try {
            return chunks[x >> 6][y >> 6].replace(x & 63, y & 63, element);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Element pop(int x, int y) {
        addToStep(x, y, null);
        try {
            return chunks[x >> 6][y >> 6].pop(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void remove(int x, int y) {
        addToStep(x, y, null);
        try {
            chunks[x >> 6][y >> 6].remove(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private void stepSingle(int x, int y) {
        stack.push(x);
        stack.push(y);
        int ix, iy;
        while (!stack.isEmpty()) {
            iy = stack.pop();
            ix = stack.pop();
//            if (valid(ix, iy - 1) && get(ix, iy - 1) != null && get(ix, iy - 1).lastStepTick() != tick) {
//                stack.push(ix);
//                stack.push(iy);
//                stack.push(ix);
//                stack.push(iy - 1);
//                continue;
//            }
            if (valid(ix, iy) && get(ix, iy) != null && get(ix, iy).lastStepTick() != tick && get(ix, iy).step(this, ix, iy, tick)) {
                int dir = -(inverse ? 1 : -1);
                stack.push(ix + dir);
                stack.push(iy);
                stack.push(ix - dir);
                stack.push(iy);
                stack.push(ix);
                stack.push(iy + 1);
                stack.push(ix + dir);
                stack.push(iy + 1);
                stack.push(ix - dir);
                stack.push(iy + 1);
            }
        }
////        try {
////            if(valid(x, y-1) && get(x, y-1) != null && get(x, y-1).lastStepTick() != tick)
////                stepSingle(x, y-1);
////        }catch (StackOverflowError e) {
////            System.err.println("StackOverflowError");
////            addToStep(x, y, get(x, y));
////        }
//        if(valid(x,y) && get(x, y) != null && get(x,y).lastStepTick() != tick && get(x, y).step(this, x, y, tick)) {
//            try {
//                stepSingle(x - (inverse ? 1 : -1), y + 1);
//                stepSingle(x + (inverse ? 1 : -1), y + 1);
//                stepSingle(x, y + 1);
//                stepSingle(x - (inverse ? 1 : -1), y);
//                stepSingle(x + (inverse ? 1 : -1), y);
//            }catch (StackOverflowError e) {
//                System.err.println("StackOverflowError");
//                addToStep(x, y, get(x, y));
//            }
//        }
    }

    @Override
    public double step() {
        double start = System.nanoTime();
//        for(int y = chunks[0][0].y * chunks[0][0].width; y < chunks[0].length * chunks[0][0].width + chunks[0][0].width; y++) {
//            if (!inverse)
//                for (int x = chunks[0][0].x * chunks[0][0].width; x < chunks[0].length * chunks[0][0].width + chunks[0][0].width; x++) {
//                    if (get(x, y) != null) {
//                        get(x, y).step(this, x, y, tick);
//                    }
//                }
//            else {
//                for (int x = chunks[0].length * chunks[0][0].width + chunks[0][0].width - 1; x >= chunks[0][0].x * chunks[0][0].width; x--) {
//                    if (get(x, y) != null) {
//                        get(x, y).step(this, x, y, tick);
//                    }
//                }
//            }
//        }
        Deque<int[]> temp = toStep;
        toStep = stepping;
        stepping = temp;
//        for(int[] pos : stepping) {
//            stepSingle(pos[0], pos[1]);
//        }
//        stepping.clear();
        while(!stepping.isEmpty()){
            int[] pos = stepping.poll();
            stepSingle(pos[0], pos[1]);
        }
        inverse = !inverse;
        tick++;
        return (System.nanoTime() - start) / 1e6;
    }

    @Override
    public void addToStep(int x, int y, Element element) {
        int dir = -(inverse?1:-1);
        toStep.add(new int[]{x+dir, y});
        toStep.add(new int[]{x, y});
        toStep.add(new int[]{x-dir, y});
        toStep.add(new int[]{x, y+1});
    }

    @Override
    public boolean valid(int x, int y) {
        try {
            chunks[x >> 6][y >> 6].get(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
}
