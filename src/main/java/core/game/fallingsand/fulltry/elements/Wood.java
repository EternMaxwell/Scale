package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import core.game.fallingsand.fulltry.Solid;

public class Wood extends Solid {

    private boolean burning = false;
    private int burnTime = 0;
    private float life = 400;
    private final static float burnHeat = 100;

    public Wood() {
        float l = (float)(Math.random() * 0.2 + 0.8);
        color = new float[]{defaultColor()[0] * l, defaultColor()[1] * l, defaultColor()[2] * l, 1};
    }

    @Override
    public float[] color(){
        if(burning) {
            float l = (float) (Math.random() * 0.2 + 0.8);
            return new float[]{1 * l, 0.5f * l, 0, 1};
        }
        return color;
    }

    /**
     * get the id of the element.
     *
     * @return the id.
     */
    @Override
    public int id() {
        return ElementID.WOOD;
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
        return new float[]{73f/256, 55f/256, 0, 1};
    }

    @Override
    public boolean heat(Grid grid, int x, int y, int tick, float heat) {
        if (heat > burnHeat) {
            burning = true;
            burnTime = (int) (500 * (heat - burnHeat) / burnHeat);
            return true;
        } else {
            burnTime += (int) (10 * (heat - burnHeat) / burnHeat);
        }
        return false;
    }

    @Override
    public boolean randomTick(Grid grid, int x, int y, int tick, int intensity) {
        if(burning) {
            life -= 10f;
            burnTime--;
            if(intensity >= 2){
                if(grid.valid(x, y + 1) && grid.get(x, y + 1) == null)
                    grid.set(x, y + 1, new Smoke());
                else if(grid.valid(x + 1, y) && grid.get(x + 1, y) == null)
                    grid.set(x + 1, y, new Smoke());
                else if(grid.valid(x - 1, y) && grid.get(x - 1, y) == null)
                    grid.set(x - 1, y, new Smoke());
            }
            float nearbyHeat = 70;
            switch (intensity){
                case 4 : nearbyHeat += 80;
                case 3 : nearbyHeat += 60;
                case 2 : nearbyHeat += 40;
                case 1 : nearbyHeat += 20;
            }
            Element nearby = grid.get(x + 1, y);
            if(nearby != null)
                nearby.heat(grid, x + 1, y, tick, nearbyHeat);
            nearby = grid.get(x - 1, y);
            if(nearby != null)
                nearby.heat(grid, x - 1, y, tick, nearbyHeat);
            nearby = grid.get(x, y + 1);
            if(nearby != null)
                nearby.heat(grid, x, y + 1, tick, nearbyHeat);
            nearby = grid.get(x, y - 1);
            if(nearby != null)
                nearby.heat(grid, x, y - 1, tick, nearbyHeat);
            if(burnTime <= 0) {
                burning = false;
            }
            if(life <= 0){
                grid.set(x, y, null);
                return true;
            }
        }
        return false;
    }
}
