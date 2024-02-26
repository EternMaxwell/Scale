package core.game.fallingsand.fulltry;

import core.game.fallingsand.Element;

public abstract class Gas extends Element {
    /**
     * @return Gas type.
     */
    @Override
    public int type() {
        return FallingType.GAS;
    }
}
