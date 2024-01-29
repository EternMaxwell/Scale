package core.game.fallingsand;

public abstract class Monitor {
    /**
     * get the message the monitor should display.
     * @param message a string where to put the message.
     * @return true if the monitor has next message.
     */
    public abstract boolean message(String[] message);
}
