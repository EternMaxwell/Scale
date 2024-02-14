package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import core.render.EasyRender;

public class ChunkAndSleepingBasedGrid extends Grid {
    public class Chunk{
        public Element[][] grid;
        public boolean[][] sleepGrid;
        public boolean[][] sleepDetectGrid;
        public int x;
        public int y;
        public final int width = 64;
        public final int bitShift = 4;
        public final int level = width/(1<<bitShift);
        public final int levelWidth = width/level;

        public Chunk(int x, int y){
            this.x = x;
            this.y = y;
            grid = new Element[width][width];
            sleepGrid = new boolean[level][level];
            sleepDetectGrid = new boolean[level][level];
        }
        public Element get(int x, int y){
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length){
                return grid[x][y];
            }
            return null;
        }
        public void set(int x, int y, Element element){
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length){
                grid[x][y] = element;
            }
        }
        public Element replace(int x, int y, Element element){
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length){
                Element old = grid[x][y];
                grid[x][y] = element;
                return old;
            }
            return null;
        }
        public Element pop(int x, int y){
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length){
                Element old = grid[x][y];
                grid[x][y] = null;
                return old;
            }
            return null;
        }
        public void remove(int x, int y){
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length){
                grid[x][y] = null;
            }
        }
        public boolean sleep(int x, int y){
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length){
                return sleepGrid[x>>bitShift][y>>bitShift];
            }
            return false;
        }
        public void setSleep(int x, int y, boolean sleep){
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length){
                sleepDetectGrid[x][y] = sleep;
            }
        }
        public void awake(int x, int y){
            if(x >= 0 && x < grid.length && y >= 0 && y < grid[0].length){
                sleepDetectGrid[x>>bitShift][y>>bitShift] = false;
                //sleepGrid[x>>bitShift][y>>bitShift] = false;
            }
        }
        public void resetSleep(){
            boolean[][] temp = sleepGrid;
            sleepGrid = sleepDetectGrid;
            sleepDetectGrid = temp;
            for(int x = 0; x < sleepDetectGrid.length; x++){
                for(int y = 0; y < sleepDetectGrid[0].length; y++){
                    sleepDetectGrid[x][y] = true;
                }
            }
        }
    }

    int tick = 0;
    boolean inverse = false;
    public Chunk[][] chunks;

    public ChunkAndSleepingBasedGrid(int chunkWidth, int chunkHeight){
        chunks = new Chunk[chunkWidth][chunkHeight];
        for(int x = 0; x < chunks.length; x++){
            for(int y = 0; y < chunks[0].length; y++){
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
        awake(x, y);
        awake(x-1, y);
        awake(x+1, y);
        awake(x, y-1);
        awake(x, y+1);
        try {
            chunks[x >> 6][y >> 6].set(x & 63, y & 63, element);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private void awake(int x, int y){
        try {
            chunks[x >> 6][y >> 6].awake(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public Element replace(int x, int y, Element element) {
        awake(x, y);
        awake(x-1, y);
        awake(x+1, y);
        awake(x, y-1);
        awake(x, y+1);
        try {
            return chunks[x >> 6][y >> 6].replace(x & 63, y & 63, element);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Element pop(int x, int y) {
        awake(x, y);
        awake(x-1, y);
        awake(x+1, y);
        awake(x, y-1);
        awake(x, y+1);
        try {
            return chunks[x >> 6][y >> 6].pop(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void remove(int x, int y) {
        awake(x, y);
        awake(x-1, y);
        awake(x+1, y);
        awake(x, y-1);
        awake(x, y+1);
        try {
            chunks[x >> 6][y >> 6].remove(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public double step() {
        double start = System.nanoTime();
        for (Chunk[] chunk : chunks) {
            for (int y = 0; y < chunks[0].length; y++) {
                chunk[y].resetSleep();
            }
        }
        for(int y = 0; y < chunks[0].length * chunks[0][0].width; y++){
            if(inverse) {
                for (int x = chunks.length * chunks[0][0].width - 1; x >= 0; x--) {
                    if(chunks[x>>6][y>>6].sleep(x & 63, y & 63)){
                        x -= chunks[0][0].width/chunks[0][0].level - 1;
                        continue;
                    }
                    if (chunks[x >> 6][y >> 6].get(x & 63, y & 63) != null) {
                        chunks[x >> 6][y >> 6].get(x & 63, y & 63).step(this, x, y, tick);
                    }
                }
            }else {
                for (int x = 0; x < chunks.length * chunks[0][0].width; x++) {
                    if(chunks[x>>6][y>>6].sleep(x & 63, y & 63)){
                        x += chunks[0][0].width/chunks[0][0].level - 1;
                        continue;
                    }
                    if (chunks[x >> 6][y >> 6].get(x & 63, y & 63) != null) {
                        chunks[x >> 6][y >> 6].get(x & 63, y & 63).step(this, x, y, tick);
                    }
                }
            }
        }
        inverse = !inverse;
        tick++;
        double stepTime = System.nanoTime() - start;
        return stepTime/1e6;
    }

    @Override
    public boolean valid(int x, int y) {
        try{
            chunks[x >> 6][y >> 6].get(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    @Override
    public void render(EasyRender render){
        for(int x = 0; x < chunks.length; x++){
            for(int y = 0; y < chunks[0].length; y++){
                for(int xx = 0; xx < chunks[0][0].level; xx++){
                    for(int yy = 0; yy < chunks[0][0].level; yy++){float drawX = x*64 + xx*((float) 64 /chunks[0][0].level);
                        float drawY = y*64 + yy*((float) 64 /chunks[0][0].level);
                        float drawWidth = ((float) 64 /chunks[0][0].level)/512f;
                        drawY /= 512f;
                        drawX /= 512f;
                        float ratio = render.window().width()/(float)render.window().height();
                        drawY -= 1;
                        drawX -= 1;
                        float[] color = new float[]{1, 1, 1, 0.05f};
                        if(!chunks[x][y].sleep(xx*chunks[0][0].width/chunks[0][0].level, yy*chunks[0][0].width/chunks[0][0].level)){
                            color = new float[]{1, 1, 1, 0.2f};
                        }
                        render.line.drawLine2D(drawX, drawY, drawX+drawWidth, drawY,
                                color[0], color[1], color[2], color[3]);
                        render.line.drawLine2D(drawX, drawY, drawX, drawY+drawWidth,
                                color[0], color[1], color[2], color[3]);
                        render.line.drawLine2D(drawX+drawWidth, drawY, drawX+drawWidth, drawY+drawWidth,
                                color[0], color[1], color[2], color[3]);
                        render.line.drawLine2D(drawX, drawY+drawWidth, drawX+drawWidth, drawY+drawWidth,
                                color[0], color[1], color[2], color[3]);
                    }
                }
            }
        }
    }
}
