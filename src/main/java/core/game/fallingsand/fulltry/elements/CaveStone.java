package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.Element;
import core.game.fallingsand.fulltry.Solid;

public class CaveStone extends Solid {

    @Override
    public Element createInstance() {
        return new CaveStone();
    }

    public CaveStone() {
        float light = (float) (Math.random() * 0.2 + 0.8);
        color = new float[]{0.5f*light, 0.5f*light, 0.5f*light, 1};
    }

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    @Override
    public int id() {
        return ElementID.CAVE_STONE;
    }

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density() {
        return 6;
    }

    /**
     * get the default color of the element.
     *
     * @return the default color.
     */
    @Override
    public float[] defaultColor() {
        return new float[]{0.45f, 0.45f, 0.45f, 1};
    }
}
