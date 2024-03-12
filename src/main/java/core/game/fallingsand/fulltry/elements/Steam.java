package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import core.game.fallingsand.fulltry.Gas;

public class Steam extends Gas {

    static {
        Elements.registerElement("steam", new Steam());
    }

    @Override
    public Element createInstance() {
        return new Steam();
    }

    public Steam() {
        super(2400);
        color = defaultColor();
    }

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    @Override
    public int id() {
        return ElementID.STEAM;
    }

    /**
     * get the density of the element.
     *
     * @return the density.
     */
    @Override
    public float density() {
        return 0.026f;
    }

    /**
     * get the default color of the element.
     *
     * @return the default color.
     */
    @Override
    public float[] defaultColor() {
        return new float[]{0.8f,0.8f,0.8f,0.4f};
    }

    @Override
    public Element existTimeEndReplaceElement(){
        if(Math.random() < 0.2){
            return new NatureWater();
        }else {
            return null;
        }
    }
}
