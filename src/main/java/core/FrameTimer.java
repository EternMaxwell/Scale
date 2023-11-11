package core;

public class FrameTimer {
    private long lastTime;
    private long currentTime;
    private long deltaTime;
    private long sleepTime;
    private double fps;
    private double frameTime;
    private long frameCount;

    public FrameTimer(double fps) {
        this.fps = fps;
        frameTime = 1000.0 / fps;
        lastTime = System.nanoTime();
        frameCount=1;
    }

    /**
     * update the timer
     * <p>this method will stop the thread until the next frame</p>
     */
    public void frame(){
        currentTime = System.nanoTime();
        deltaTime = currentTime - lastTime;
        sleepTime = (long) (frameTime * 1000000 - deltaTime);
        if(sleepTime > 0){
            try {
                Thread.sleep(sleepTime / 1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frameCount++;
        lastTime = System.nanoTime();
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
