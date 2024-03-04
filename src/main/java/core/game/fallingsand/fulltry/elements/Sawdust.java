package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.fulltry.FluidSolid;

public class Sawdust extends FluidSolid {

    public Sawdust() {
        float l = (float) Math.random();
        color = new float[]{0.75f+0.15F*l, 0.7f+0.15f*l, 0.01f*(1f-l), 1};
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
        return new float[]{0.9f, 0.9f, 0.1f, 1};
    }
}
