package core.game.fallingsand.fulltry;

import core.render.Window;

public class FallingData {
    public static float scale;
    public static int defaultShowGridWidth;
    public static double[] cameraCentrePos;
    public static int chunkMaxNum;

    public static void startup(){
        scale = 1;
        defaultShowGridWidth = 512;
        cameraCentrePos = new double[]{0,0};
        chunkMaxNum = 256;
    }
}
