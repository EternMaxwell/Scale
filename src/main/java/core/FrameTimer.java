package core;

import static org.lwjgl.glfw.GLFW.*;

public class FrameTimer {
    private long currentTime;
    private long sleepTime;
    private long targetTime = 0;
    private double fps;
    private double frameTime;
    private long frameCount;

    public FrameTimer(double fps) {
        this.fps = fps;
        frameTime = 1000.0 / fps;
        frameCount=0;
    }

    /**
     * update the timer
     * <p>this method will stop the thread until the next frame</p>
     */
    public void frame(){
        if(targetTime == 0){
            targetTime = currentTime;
        }
        targetTime += (long) (frameTime * 1000000);
        currentTime = System.nanoTime();
        sleepTime = targetTime - currentTime;
        if(sleepTime < 0){
            targetTime = currentTime;
            sleepTime = 0;
        }
        if(sleepTime > 0){
            try {
                Thread.sleep(sleepTime / 1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frameCount++;
    }

    /**
     * get the fps
     * @return the fps
     */
    public double fps() {
        return fps;
    }

    /**
     * get the frame count
     * @return the frame count
     */
    public long frameCount() {
        return frameCount;
    }

    /**
     * set the fps
     * <p>This allows the application to change the fps at runtime</p>
     * @param fps the fps
     */
    public void setFps(double fps) {
        this.fps = fps;
        frameTime = 1000.0 / fps;
    }
}
