package core;

public class TickTimer {
    private long lastTime;
    private long currentTime;

    private double tps;
    private double tickTime;
    private long tickCount;

    public TickTimer(double tps) {
        this.tps = tps;
        tickTime = 1000.0 / tps;
        lastTime = System.nanoTime();
        tickCount = 1;
    }
    
    /**
     * update the timer
     * <p>this method will check the timer and return true if it is time to get the next tick</p>
     */
    public boolean tick() {
        currentTime = System.nanoTime();
        if (currentTime - lastTime >= tickTime * 1000000) {
            tickCount++;
            lastTime = currentTime-(long)(currentTime%(tickTime*1000000));
            return true;
        }
        return false;
    }

    /**
     * get the tps
     * @return the tps
     */
    public double tps() {
        return tps;
    }

    /**
     * get the tick count
     * @return the tick count
     */
    public long tickCount() {
        return tickCount;
    }

    /**
     * set the tps
     * <p>This allows the application to change the tps at runtime</p>
     * <p>Though it is not recommended to change the tps at runtime</p>
     * @param tps the tps
     */
    public void setTps(double tps) {
        this.tps = tps;
    }
}
