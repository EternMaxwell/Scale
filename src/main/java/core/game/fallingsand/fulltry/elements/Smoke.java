package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import core.game.fallingsand.fulltry.Gas;

public class Smoke extends Gas {

    @Override
    public Element createInstance() {
        return new Smoke();
    }

    public Smoke() {
        super(2000 + (int)(Math.random() * 800));
        color = defaultColor();
    }

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    @Override
    public int id() {
        return ElementID.SMOKE;
    }

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density() {
        return 0.03f;
    }

    /**
     * get the default color of the element.
     *
     * @return the default color.
     */
    @Override
    public float[] defaultColor() {
        return new float[]{0.5f, 0.5f, 0.5f, 0.6f};
    }
}
