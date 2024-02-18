package core.game.fallingsand.fulltry;

public class FallingData {
    public static float scale;
    public static int defaultShowGridWidth;
    public static double[] cameraCentrePos;

    public static void startup(){
        scale = 1;
        defaultShowGridWidth = 512;
        cameraCentrePos = new double[]{0,0};
    }
}
