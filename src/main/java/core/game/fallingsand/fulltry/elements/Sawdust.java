package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.fulltry.FluidSolid;

public class Sawdust extends FluidSolid {

    public Sawdust() {
        float l = (float)(Math.random() * 0.2 + 0.8);
        color = new float[]{defaultColor()[0] * l, defaultColor()[1] * l, defaultColor()[2] * l, 1};
    }

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    @Override
    public int id() {
        return ElementID.SAWDUST;
    }

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density() {
        return 0.7f;
    }

    /**
     * get the default color of the element.
     *
     * @return the default color.
     */
    @Override
    public float[] defaultColor() {
        return new float[]{73f/256, 57f/256, 0, 1};
    }
}
