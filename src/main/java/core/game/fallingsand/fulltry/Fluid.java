package core.game.fallingsand.fulltry;

import core.game.fallingsand.Element;

public abstract class Fluid extends Element {
    /**
     * @return Fluid type.
     */
    @Override
    public int type() {
        return FallingType.FLUID;
    }
}
