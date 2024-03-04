package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.fulltry.Fluid;

public class NatureWater extends Fluid {

    public NatureWater(){
        color = new float[]{0.1f, 0.1f, 1f, 0.8f};
    }

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    @Override
    public int id() {
        return ElementID.NATURE_WATER;
    }

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density() {
        return 1;
    }

    /**
     * get the default color of the element.
     *
     * @return the default color.
     */
    @Override
    public float[] defaultColor() {
        return new float[]{0.1f, 0.1f, 0.9f, 0.8f};
    }

    /**
     * @return the dispersion rate.
     */
    @Override
    public int dispersionRate() {
        return 3;
    }
}
