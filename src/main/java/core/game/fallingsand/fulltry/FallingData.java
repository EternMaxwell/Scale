package core.game.fallingsand.fulltry;

import core.render.Window;

public class FallingData {
    public static float scale;
    public static int defaultShowGridWidth;
    public static double[] cameraCentrePos;
    public static int chunkMaxNum;
    public static int chunkWidth;
    public static int chunkSleepLevel;
    public static int chunkSleepBitShift;
    public static int[] chunkBasePos;
    public static int chunkNum;
    public static boolean enableChunkUpdate;
    public static Window window;

    public static InputTool inputTool;

    public static void startup() {
        scale = 1;
        defaultShowGridWidth = 512;
        cameraCentrePos = new double[]{0, 0};
        chunkMaxNum = 256;
        chunkWidth = 64;
        chunkSleepBitShift = FallingGrid.Chunk.bitShift;
        chunkSleepLevel = chunkWidth / (1 << chunkSleepBitShift);
        chunkBasePos = new int[]{0, 0};
        chunkNum = 0;
        enableChunkUpdate = true;
    }
}
