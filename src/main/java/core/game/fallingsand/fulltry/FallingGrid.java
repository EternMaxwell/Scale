package core.game.fallingsand.fulltry;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import core.game.fallingsand.easyfallingsand.ChunkAndSleepingBasedGrid;
import core.game.fallingsand.fulltry.box2d.FallingBody;
import core.render.EasyRender;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

public class FallingGrid extends Grid {

    public static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static class Chunk{
        public Element[][] grid;
        public boolean[][] sleepGrid;
        public boolean[][] sleepDetectGrid;
        public int x;
        public int y;
        public static final int width = 64;
        public final static int bitShift = 4;
        public static final int level = width/(1<<bitShift);
        public static final int levelWidth = width/level;
        private boolean changed = true;
        private Set<FallingBody> chunkBodies;

        public Chunk(int x, int y){
            this.x = x;
            this.y = y;
            grid = new Element[width][width];
            sleepGrid = new boolean[level][level];
            sleepDetectGrid = new boolean[level][level];
            chunkBodies = new HashSet<>();
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

        public void calculateChunkBodies(World world){
            chunkBodies.clear();
            for(int x = 0; x < grid.length; x++){
                for(int y = 0; y < grid[0].length; y++){
                    if(grid[x][y] != null && grid[x][y].type() == FallingType.SOLID || grid[x][y].type() == FallingType.FLUIDSOLID){
                        BodyDef bodyDef = new BodyDef();
                        bodyDef.type = BodyType.STATIC;
                        Body body = new Body(bodyDef, world);
                        FixtureDef fixtureDef = new FixtureDef();
                        PolygonShape shape = new PolygonShape();
                        shape.setAsBox(0.05f, 0.05f);
                        fixtureDef.shape = shape;
                        body.createFixture(fixtureDef);
                    }
                }
            }
        }

        public Set<FallingBody> getChunkBody(World world) {
            if(changed){
                calculateChunkBodies(world);
                changed = false;
            }
            return chunkBodies;
        }
    }

    int tick = 0;
    boolean inverse = false;
    public Chunk[][] chunks;
    public int currentChunkNum;
    public int[] chunkSize;
    public int[] chunkBasePos;
    public int toReverse = 0;
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f modelMatrix = new Matrix4f();

    public FallingGrid(){
        chunkSize = new int[]{10,10};
        chunks = new Chunk[chunkSize[0]][chunkSize[1]];
        chunkBasePos = new int[]{-5, -5};
        for(int x = 0; x < chunks.length; x++){
            for(int y = 0; y < chunks[0].length; y++){
                chunks[x][y] = new Chunk(x + chunkBasePos[0], y + chunkBasePos[1]);
            }
        }
        currentChunkNum = chunkSize[0] * chunkSize[1];
    }

    public Chunk insertChunk(int x, int y){
        if(x < chunkBasePos[0]){
            Chunk[][] newChunks = new Chunk[chunkSize[0] + chunkBasePos[0] - x][chunkSize[1]];
            if (newChunks.length - (chunkBasePos[0] - x) >= 0)
                System.arraycopy(chunks, 0, newChunks, chunkBasePos[0] - x, chunks.length);
            chunks = newChunks;
            chunkBasePos[0] = x;
            chunkSize[0] = chunks.length;
        }else if(x >= chunkBasePos[0] + chunkSize[0]){
            Chunk[][] newChunks = new Chunk[x - chunkBasePos[0] + 1][chunkSize[1]];
            System.arraycopy(chunks, 0, newChunks, 0, chunks.length);
            chunks = newChunks;
            chunkSize[0] = chunks.length;
        }
        if(y < chunkBasePos[1]){
            for(int i = 0; i < chunks.length; i++){
                Chunk[] newChunks = new Chunk[chunkSize[1] + chunkBasePos[1] - y];
                System.arraycopy(chunks[i], 0, newChunks, chunkBasePos[1] - y, chunks[i].length);
                chunks[i] = newChunks;
            }
            chunkBasePos[1] = y;
            chunkSize[1] = chunks[0].length;
        }else if(y >= chunkBasePos[1] + chunkSize[1]){
            for(int i = 0; i < chunks.length; i++){
                Chunk[] newChunks = new Chunk[y - chunkBasePos[1] + 1];
                System.arraycopy(chunks[i], 0, newChunks, 0, chunks[i].length);
                chunks[i] = newChunks;
            }
            chunkSize[1] = chunks[0].length;
        }
        chunks[x - chunkBasePos[0]][y - chunkBasePos[1]] = new Chunk(x, y);
        currentChunkNum++;
        for(int i = 0; i <= FallingData.chunkWidth; i++){
            awake((x - chunkBasePos[0]) * FallingData.chunkWidth + i, (y - chunkBasePos[1]) * FallingData.chunkWidth + FallingData.chunkWidth);
            awake((x - chunkBasePos[0]) * FallingData.chunkWidth - 1, (y - chunkBasePos[1]) * FallingData.chunkWidth + i);
            awake((x - chunkBasePos[0]) * FallingData.chunkWidth + FallingData.chunkWidth, (y - chunkBasePos[1]) * FallingData.chunkWidth + i -1);
            awake((x - chunkBasePos[0]) * FallingData.chunkWidth + i - 1, (y - chunkBasePos[1]) * FallingData.chunkWidth - 1);
        }

        return chunks[x - chunkBasePos[0]][y - chunkBasePos[1]];
    }

    public Chunk removeChunk(int x, int y){
        Chunk chunk = chunks[x - chunkBasePos[0]][y - chunkBasePos[1]];
        chunks[x - chunkBasePos[0]][y - chunkBasePos[1]] = null;
        if(x == chunkBasePos[0]) {
            boolean emptyCol = true;
            for(int i = 0; i < chunks[0].length; i++){
                if(chunks[0][i] != null){
                    emptyCol = false;
                    break;
                }
            }
            if(emptyCol){
                Chunk[][] newChunks = new Chunk[chunkSize[0] - 1][chunkSize[1]];
                System.arraycopy(chunks, 1, newChunks, 0, chunks.length - 1);
                chunks = newChunks;
                chunkBasePos[0]++;
                chunkSize[0]--;
            }
        }else if(x == chunkBasePos[0] + chunkSize[0] - 1){
            boolean emptyCol = true;
            for(int i = 0; i < chunks[0].length; i++){
                if(chunks[chunks.length - 1][i] != null){
                    emptyCol = false;
                    break;
                }
            }
            if(emptyCol){
                Chunk[][] newChunks = new Chunk[chunkSize[0] - 1][chunkSize[1]];
                System.arraycopy(chunks, 0, newChunks, 0, chunks.length - 1);
                chunks = newChunks;
                chunkSize[0]--;
            }
        }
        if(y == chunkBasePos[1]) {
            boolean emptyRow = true;
            for (int i = 0; i < chunks.length; i++) {
                if (chunks[i][0] != null) {
                    emptyRow = false;
                    break;
                }
            }
            if (emptyRow) {
                for (int i = 0; i < chunks.length; i++) {
                    Chunk[] newChunks = new Chunk[chunkSize[1] - 1];
                    System.arraycopy(chunks[i], 1, newChunks, 0, chunks[i].length - 1);
                    chunks[i] = newChunks;
                }
                chunkBasePos[1]++;
                chunkSize[1]--;
            }
        }else if(y == chunkBasePos[1] + chunkSize[1] - 1){
            boolean emptyRow = true;
            for (int i = 0; i < chunks.length; i++) {
                if (chunks[i][chunks[0].length - 1] != null) {
                    emptyRow = false;
                    break;
                }
            }
            if (emptyRow) {
                for (int i = 0; i < chunks.length; i++) {
                    Chunk[] newChunks = new Chunk[chunkSize[1] - 1];
                    System.arraycopy(chunks[i], 0, newChunks, 0, chunks[i].length - 1);
                    chunks[i] = newChunks;
                }
                chunkSize[1]--;
            }
        }
        currentChunkNum--;
        return chunk;
    }

    public Chunk chunkAt(int x, int y){
        if(x < chunkBasePos[0] || x >= chunkBasePos[0] + chunkSize[0] || y < chunkBasePos[1] || y >= chunkBasePos[1] + chunkSize[1]){
            return null;
        }
        return chunks[x - chunkBasePos[0]][y - chunkBasePos[1]];
    }

    public int[] farthestChunk() {
        int[] result = new int[]{0, 0};
        initialize:
        for (int x = chunkBasePos[0]; x < chunkBasePos[0] + chunkSize[0]; x++) {
            for (int y = chunkBasePos[1]; y < chunkBasePos[1] + chunkSize[1]; y++) {
                if (chunkAt(x, y) != null) {
                    result = new int[]{x, y};
                    break initialize;
                }
            }
        }

        double[] realPos = new double[2];
        double[] realPosOfResult = new double[2];
        for(int x = chunkBasePos[0]; x < chunkBasePos[0] + chunkSize[0]; x++){
            for(int y = chunkBasePos[1]; y < chunkBasePos[1] + chunkSize[1]; y++){
                if(chunkAt(x, y) != null){
                    realPos[0] = (x + 0.5) * FallingData.chunkWidth;
                    realPos[1] = (y + 0.5) * FallingData.chunkWidth;
                    realPosOfResult[0] = (result[0] + 0.5) * FallingData.chunkWidth;
                    realPosOfResult[1] = (result[1] + 0.5) * FallingData.chunkWidth;
                    if(Math.pow(realPos[0] - FallingData.cameraCentrePos[0], 2) + Math.pow(realPos[1] - FallingData.cameraCentrePos[1], 2) >
                            Math.pow(realPosOfResult[0] - FallingData.cameraCentrePos[0], 2) + Math.pow(realPosOfResult[1] - FallingData.cameraCentrePos[1], 2)){
                        result = new int[]{x, y};
                    }
                }
            }
        }
        return result;
    }

    public double[][] getScreenRectangle(long windowId){
        double[][] screenRectangle = new double[4][];
        double[] screenCenter = FallingData.cameraCentrePos;

        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(windowId, width, height);
        float ratio = (float) width[0] / (height[0] * 16 / 9f);

        screenRectangle[0] = new double[]{screenCenter[0] - FallingData.scale * FallingData.defaultShowGridWidth * ratio / 2,
                screenCenter[1] - FallingData.scale * FallingData.defaultShowGridWidth * 9 / 32};
        screenRectangle[1] = new double[]{screenCenter[0] + FallingData.scale * FallingData.defaultShowGridWidth * ratio / 2,
                screenCenter[1] - FallingData.scale * FallingData.defaultShowGridWidth * 9 / 32};
        screenRectangle[2] = new double[]{screenCenter[0] + FallingData.scale * FallingData.defaultShowGridWidth * ratio / 2,
                screenCenter[1] + FallingData.scale * FallingData.defaultShowGridWidth * 9 / 32};
        screenRectangle[3] = new double[]{screenCenter[0] - FallingData.scale * FallingData.defaultShowGridWidth * ratio / 2,
                screenCenter[1] + FallingData.scale * FallingData.defaultShowGridWidth * 9 / 32};
        return screenRectangle;
    }

    public void updateChunks(){
        double[][] screenRectangle = getScreenRectangle(FallingData.window.id());
        boolean next = true;
        while(next) {
            next = false;
            for (int x = chunkBasePos[0] - 1; x < chunkBasePos[0] + chunkSize[0] + 1; x++) {
                for(int y = chunkBasePos[1] - 1; y < chunkBasePos[1] + chunkSize[1] + 1; y++){
                    if(chunkAt(x, y) == null && !outOf(new double[][]{{x * 64, y * 64}, {(x + 1) * 64, y * 64},
                            {(x + 1) * 64, (y + 1) * 64}, {x * 64, (y + 1) * 64}}, screenRectangle)){
                        insertChunk(x, y);
                        next = true;
                    }
                }
            }
        }
        while(currentChunkNum > FallingData.chunkMaxNum){
            int[] removeChunk = farthestChunk();
            removeChunk(removeChunk[0], removeChunk[1]);
        }
        FallingData.chunkBasePos = chunkBasePos;
        FallingData.chunkNum = currentChunkNum;
    }

    public boolean outOf(double[][] obj, double[][] mother){
        double minX = mother[0][0], minY = mother[0][1], maxX = mother[0][0], maxY = mother[0][1];
        for(int i = 1; i < 4; i++){
            minX = Math.min(mother[i][0], minX);
            minY = Math.min(mother[i][1], minY);
            maxX = Math.max(mother[i][0], maxX);
            maxY = Math.max(mother[i][1], maxY);
        }
        double objMinX = obj[0][0], objMinY = obj[0][1], objMaxX = obj[0][0], objMaxY = obj[0][1];
        for(int i = 1; i < 4; i++){
            objMinX = Math.min(obj[i][0], objMinX);
            objMinY = Math.min(obj[i][1], objMinY);
            objMaxX = Math.max(obj[i][0], objMaxX);
            objMaxY = Math.max(obj[i][1], objMaxY);
        }
        if(objMaxX < minX || objMinX > maxX || objMaxY < minY || objMinY > maxY){
            return true;
        }
        return false;
    }

    @Override
    public Element get(int x, int y) {
        try {
            return chunks[x >> 6][y >> 6].get(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
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
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
        }
    }

    private void awake(int x, int y){
        try {
            chunks[x >> 6][y >> 6].awake(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
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
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
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
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
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
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
        }
    }

    @Override
    public double step() {
        if(FallingData.pause) return 0;
        double start = System.nanoTime();
        if(FallingData.enableChunkUpdate)
            updateChunks();
        for (Chunk[] chunk : chunks) {
            for (Chunk value : chunk) {
                if(value != null)
                    value.resetSleep();
            }
        }
//        for(int y = 0; y < chunkSize[1] * FallingData.chunkWidth; y++){
//            if(inverse) {
//                for (int x = chunkSize[0] * FallingData.chunkWidth - 1; x >= 0; x--) {
//                    if(chunks[x>>6][y>>6] == null)
//                        continue;
//                    if(chunks[x>>6][y>>6].sleep(x & 63, y & 63)){
//                        x -= FallingData.chunkWidth/FallingData.chunkSleepLevel - 1;
//                        continue;
//                    }
//                    if (chunks[x >> 6][y >> 6].get(x & 63, y & 63) != null) {
//                        chunks[x >> 6][y >> 6].get(x & 63, y & 63).step(this, x, y, tick);
//                    }
//                }
//            }else {
//                for (int x = 0; x < chunkSize[0] * FallingData.chunkWidth; x++) {
//                    if(chunks[x>>6][y>>6] == null)
//                        continue;
//                    if(chunks[x>>6][y>>6].sleep(x & 63, y & 63)){
//                        x += FallingData.chunkWidth/FallingData.chunkSleepLevel - 1;
//                        continue;
//                    }
//                    if (chunks[x >> 6][y >> 6].get(x & 63, y & 63) != null) {
//                        chunks[x >> 6][y >> 6].get(x & 63, y & 63).step(this, x, y, tick);
//                    }
//                }
//            }
//        }
        Set<Future> futures = new HashSet<>();
        for(int chunkY = 0; chunkY < chunkSize[1]; chunkY+=2){
            int finalChunkY = chunkY;
            futures.add(executorService.submit(() -> {
                for(int y = 0; y < FallingData.chunkWidth; y++){
                    if(inverse) {
                        for (int chunkX = chunkSize[0] - 1; chunkX >= 0; chunkX--) {
                            if(chunks[chunkX][finalChunkY] == null)
                                continue;
                            for (int x = FallingData.chunkWidth - 1; x >= 0; x--) {
                                if(chunks[chunkX][finalChunkY].sleep(x, y)){
                                    x -= FallingData.chunkWidth/FallingData.chunkSleepLevel - 1;
                                    continue;
                                }
                                if (chunks[chunkX][finalChunkY].get(x, y) != null) {
                                    chunks[chunkX][finalChunkY].get(x, y).step(this, chunkX * FallingData.chunkWidth + x, finalChunkY * FallingData.chunkWidth + y, tick);
                                }
                            }
                        }
                    }else {
                        for (int chunkX = 0; chunkX < chunkSize[0]; chunkX++) {
                            if(chunks[chunkX][finalChunkY] == null)
                                continue;
                            for (int x = 0; x < FallingData.chunkWidth; x++) {
                                if(chunks[chunkX][finalChunkY].sleep(x, y)){
                                    x += FallingData.chunkWidth/FallingData.chunkSleepLevel - 1;
                                    continue;
                                }
                                if (chunks[chunkX][finalChunkY].get(x, y) != null) {
                                    chunks[chunkX][finalChunkY].get(x, y).step(this, chunkX * FallingData.chunkWidth + x, finalChunkY * FallingData.chunkWidth + y, tick);
                                }
                            }
                        }
                    }
                }
                return null;
            }));
        }
        for(Future future : futures){
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int chunkY = 1; chunkY < chunkSize[1]; chunkY+=2){
            int finalChunkY = chunkY;
            futures.add(executorService.submit(() -> {
                for(int y = 0; y < FallingData.chunkWidth; y++){
                    if(inverse) {
                        for (int chunkX = chunkSize[0] - 1; chunkX >= 0; chunkX--) {
                            if(chunks[chunkX][finalChunkY] == null)
                                continue;
                            for (int x = FallingData.chunkWidth - 1; x >= 0; x--) {
                                if(chunks[chunkX][finalChunkY].sleep(x, y)){
                                    x -= FallingData.chunkWidth/FallingData.chunkSleepLevel - 1;
                                    continue;
                                }
                                if (chunks[chunkX][finalChunkY].get(x, y) != null) {
                                    chunks[chunkX][finalChunkY].get(x, y).step(this, chunkX * FallingData.chunkWidth + x, finalChunkY * FallingData.chunkWidth + y, tick);
                                }
                            }
                        }
                    }else {
                        for (int chunkX = 0; chunkX < chunkSize[0]; chunkX++) {
                            if(chunks[chunkX][finalChunkY] == null)
                                continue;
                            for (int x = 0; x < FallingData.chunkWidth; x++) {
                                if(chunks[chunkX][finalChunkY].sleep(x, y)){
                                    x += FallingData.chunkWidth/FallingData.chunkSleepLevel - 1;
                                    continue;
                                }
                                if (chunks[chunkX][finalChunkY].get(x, y) != null) {
                                    chunks[chunkX][finalChunkY].get(x, y).step(this, chunkX * FallingData.chunkWidth + x, finalChunkY * FallingData.chunkWidth + y, tick);
                                }
                            }
                        }
                    }
                }
                return null;
            }));
        }
        for(Future future : futures){
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        randomTick();
        toReverse++;
        if(toReverse > 0) {
            toReverse = 0;
            inverse = !inverse;
        }
        tick++;
        FallingData.tick = tick;
        double stepTime = System.nanoTime() - start;
        return stepTime/1e6;
    }

    @Override
    public boolean valid(int x, int y) {
        try{
            chunks[x >> 6][y >> 6].get(x & 63, y & 63);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public void render(EasyRender render){
        modelMatrix.setColumn(3, new Vector4f(-(float) FallingData.cameraCentrePos[0],
                -(float) FallingData.cameraCentrePos[1], 0, 1));
        viewMatrix.setOrtho2D(-FallingData.scale * FallingData.defaultShowGridWidth * 9/32f,
                FallingData.scale * FallingData.defaultShowGridWidth * 9/32f,
                -FallingData.scale * FallingData.defaultShowGridWidth * 9/32f,
                FallingData.scale * FallingData.defaultShowGridWidth * 9/32f);
        //viewMatrix.setOrtho2D(0, 1024, 0, 1024);

        render.pixel.setModelMatrix(modelMatrix);
        render.pixel.setViewMatrix(viewMatrix);
        render.line.setModelMatrix(modelMatrix);
        render.line.setViewMatrix(viewMatrix);
        render.pixel.setPixelSize(1);

        double[][] screenRectangle = getScreenRectangle(FallingData.window.id());

        for (int x = chunkBasePos[0] - 1; x < chunkBasePos[0] + chunkSize[0] + 1; x++) {
            for(int y = chunkBasePos[1] - 1; y < chunkBasePos[1] + chunkSize[1] + 1; y++){
                Chunk chunk = chunkAt(x, y);
                if(chunk != null
                        && !outOf(new double[][]{{x * 64, y * 64}, {(x + 1) * 64, y * 64},
                                {(x + 1) * 64, (y + 1) * 64}, {x * 64, (y + 1) * 64}}, screenRectangle)
                ){
                    for(int xx = 0; xx < FallingData.chunkWidth; xx++) {
                        for(int yy = 0; yy < FallingData.chunkWidth; yy++) {
                            Element element = chunk.get(xx, yy);
                            if (element != null) {
                                if(element.type() != FallingType.GAS){
                                    float[] color = element.color();
                                    render.pixel.drawPixel((float) (x * FallingData.chunkWidth + xx),
                                            (float) (y * FallingData.chunkWidth + yy), color[0], color[1], color[2], color[3]);
                                }else{
                                    float[] color = element.color();
                                    render.pixel.drawPixel((float) (x * FallingData.chunkWidth + xx) + 0.5f,
                                            (float) (y * FallingData.chunkWidth + yy), color[0], color[1], color[2], color[3]/2);
                                    render.pixel.drawPixel((float) (x * FallingData.chunkWidth + xx) - 0.5f,
                                            (float) (y * FallingData.chunkWidth + yy), color[0], color[1], color[2], color[3]/2);
                                }
                            }
                        }
                    }
                }
            }
        }

        render.pixel.flush();

        for (int x = chunkBasePos[0] - 1; x < chunkBasePos[0] + chunkSize[0] + 1; x++) {
            for(int y = chunkBasePos[1] - 1; y < chunkBasePos[1] + chunkSize[1] + 1; y++){
                if (chunkAt(x,y) == null
                        || outOf(new double[][]{{x * 64, y * 64}, {(x + 1) * 64, y * 64},
                        {(x + 1) * 64, (y + 1) * 64}, {x * 64, (y + 1) * 64}}, screenRectangle)
                )
                    continue;
                for (int xx = 0; xx < FallingData.chunkSleepLevel; xx++) {
                    for (int yy = 0; yy < FallingData.chunkSleepLevel; yy++) {
                        float drawX = x * FallingData.chunkWidth + xx * ((float) FallingData.chunkWidth / FallingData.chunkSleepLevel);
                        float drawY = y * FallingData.chunkWidth + yy * ((float) FallingData.chunkWidth / FallingData.chunkSleepLevel);
                        float drawWidth = (float) FallingData.chunkWidth / FallingData.chunkSleepLevel;
                        drawY -= 0.5f;
                        drawX -= 0.5f;
                        float[] color = new float[]{1, 1, 1, 0.05f};
                        if (!chunkAt(x,y).sleep(xx * FallingData.chunkWidth / FallingData.chunkSleepLevel,
                                yy * FallingData.chunkWidth / FallingData.chunkSleepLevel)) {
                            color = new float[]{1, 1, 1, 0.2f};
                        }
                        render.line.drawLine2D(drawX, drawY, drawX + drawWidth, drawY,
                                color[0], color[1], color[2], color[3]);
                        render.line.drawLine2D(drawX, drawY, drawX, drawY + drawWidth,
                                color[0], color[1], color[2], color[3]);
                        render.line.drawLine2D(drawX + drawWidth, drawY, drawX + drawWidth, drawY + drawWidth,
                                color[0], color[1], color[2], color[3]);
                        render.line.drawLine2D(drawX, drawY + drawWidth, drawX + drawWidth, drawY + drawWidth,
                                color[0], color[1], color[2], color[3]);
                    }
                }
            }
        }

        render.line.flush();
    }

    public void randomTick(){
        Set<Future> futures = new HashSet<>();
        Random random = new Random();
        int chunkWidth = FallingData.chunkWidth;
        for(int chunkX = 0; chunkX < chunkSize[0]; chunkX++){
            for(int chunkY = 0; chunkY < chunkSize[1]; chunkY++){
                int finalChunkX = chunkX;
                int finalChunkY = chunkY;
//                futures.add(executorService.submit(() -> {
                    Chunk chunk = chunks[finalChunkX][finalChunkY];
                    if (chunk == null)
                        return;
                    int x;
                    int y;
                    for (int i = 0; i < 64; i++) {
                        x = random.nextInt(chunkWidth);
                        y = random.nextInt(64);
                        Element element = chunk.get(x, y);
                        if (element != null) {
                            element.randomTick(this, finalChunkX * chunkWidth + x, finalChunkY * chunkWidth + y, tick, 1);
                        }
                    }
                    for (int i = 0; i < 32; i++) {
                        x = random.nextInt(chunkWidth);
                        y = random.nextInt(chunkWidth);
                        Element element = chunk.get(x, y);
                        if (element != null) {
                            element.randomTick(this, finalChunkX * chunkWidth + x, finalChunkY * chunkWidth + y, tick, 2);
                        }
                    }
                    for (int i = 0; i < 16; i++) {
                        x = random.nextInt(chunkWidth);
                        y = random.nextInt(chunkWidth);
                        Element element = chunk.get(x, y);
                        if (element != null) {
                            element.randomTick(this, finalChunkX * chunkWidth + x, finalChunkY * chunkWidth + y, tick, 3);
                        }
                    }
                    for (int i = 0; i < 8; i++) {
                        x = random.nextInt(chunkWidth);
                        y = random.nextInt(chunkWidth);
                        Element element = chunk.get(x, y);
                        if (element != null) {
                            element.randomTick(this, finalChunkX * chunkWidth + x, finalChunkY * chunkWidth + y, tick, 4);
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        x = random.nextInt(chunkWidth);
                        y = random.nextInt(chunkWidth);
                        Element element = chunk.get(x, y);
                        if (element != null) {
                            element.randomTick(this, finalChunkX * chunkWidth + x, finalChunkY * chunkWidth + y, tick, 5);
                        }
                    }
                    for (int i = 0; i < 2; i++) {
                        x = random.nextInt(chunkWidth);
                        y = random.nextInt(chunkWidth);
                        Element element = chunk.get(x, y);
                        if (element != null) {
                            element.randomTick(this, finalChunkX * chunkWidth + x, finalChunkY * chunkWidth + y, tick, 6);
                        }
                    }
                    for (int i = 0; i < 1; i++) {
                        x = random.nextInt(chunkWidth);
                        y = random.nextInt(chunkWidth);
                        Element element = chunk.get(x, y);
                        if (element != null) {
                            element.randomTick(this, finalChunkX * chunkWidth + x, finalChunkY * chunkWidth + y, tick, 7);
                        }
                    }
//                    return;
//                }));
            }
        }
        for(Future future : futures){
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int[] basePos() {
        return new int[]{chunkBasePos[0] * FallingData.chunkWidth, chunkBasePos[1] * FallingData.chunkWidth};
    }

    @Override
    public Set<FallingBody> toBodies(World world) {
        Set<FallingBody> bodies = new HashSet<>();
        for(int x = 0; x < chunkSize[0]; x++){
            for(int y = 0; y < chunkSize[1]; y++){
                if(chunks[x][y] != null){
                    bodies.addAll(chunks[x][y].getChunkBody(world));
                }
            }
        }
        return bodies;
    }
}
