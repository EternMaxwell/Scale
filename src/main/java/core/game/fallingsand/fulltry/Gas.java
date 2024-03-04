package core.game.fallingsand.fulltry;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

public abstract class Gas extends Element {
    int lastStepTick = -1;
    float sinkingProcess = 0.0f;
    public static final float AIR_DENSITY = 0.05f;

    /**
     * @return Gas type.
     */
    @Override
    public int type() {
        return FallingType.GAS;
    }

    /**
     * free fall for Gas will always return true.
     * @return true.
     */
    public boolean freeFall(){
        return true;
    }

    /**
     * get the last step tick.
     * @return the last step tick.
     */
    public int lastStepTick(){
        return lastStepTick;
    }

    public boolean step(Grid grid, int x, int y, int tick) {
        boolean moved = false;
        //check the above block
        Element above = grid.get(x, y + 1);
        if (above == null && (grid.valid(x, y + 1) || FallingData.invalidPassThrough)) {
            sinkingProcess += 1 - density();
            if(sinkingProcess >= 1){
                sinkingProcess = 0;
                lastStepTick = tick;
                moved = true;
                grid.set(x, y + 1, this);
                grid.set(x, y, null);
            }else {
                grid.set(x, y, this);
            }
        } else if(above != null && above.type() == FallingType.GAS){
            if(above.density() > density()) {
                sinkingProcess += (above.density() - density())/above.density();
                if(sinkingProcess >= 1) {
                    sinkingProcess = 0;
                    moved = true;
                    lastStepTick = tick;
                    grid.set(x, y + 1, this);
                    grid.set(x, y, above);
                }else {
                    grid.set(x, y, this);
                }
            }else
                sinkingProcess = 0;
        }
        //check the diagonal blocks
        int dir = Math.random() > 0.5 ? 1 : -1;
        Element diagonal = grid.get(x + dir, y + 1);
        if (diagonal == null && (grid.valid(x + dir, y + 1) || FallingData.invalidPassThrough)) {
            sinkingProcess += 1 - density()/AIR_DENSITY;
            if(sinkingProcess >= 1){
                sinkingProcess = 0;
                moved = true;
                lastStepTick = tick;
                grid.set(x + dir, y + 1, this);
                grid.set(x, y, null);
            }else {
                grid.set(x, y, this);
            }
        } else {
            diagonal = grid.get(x - dir, y + 1);
            if (diagonal == null && (grid.valid(x - dir, y + 1) || FallingData.invalidPassThrough)) {
                sinkingProcess += 1 - density()/AIR_DENSITY;
                if(sinkingProcess >= 1){
                    sinkingProcess = 0;
                    moved = true;
                    lastStepTick = tick;
                    grid.set(x - dir, y + 1, this);
                    grid.set(x, y, null);
                }else {
                    grid.set(x, y, this);
                }
            }
        }
        //if the diagonal blocks are gas then try to move to them
        if (!moved) {
            diagonal = grid.get(x + dir, y + 1);
            if (diagonal != null && diagonal.type() == FallingType.GAS) {
                if (diagonal.density() > density()) {
                    sinkingProcess += (diagonal.density() - density()) / diagonal.density();
                    if (sinkingProcess >= 1) {
                        sinkingProcess = 0;
                        moved = true;
                        lastStepTick = tick;
                        grid.set(x + dir, y + 1, this);
                        grid.set(x, y, diagonal);
                    } else {
                        grid.set(x, y, this);
                    }
                } else
                    sinkingProcess = 0;
            } else {
                diagonal = grid.get(x - dir, y + 1);
                if (diagonal != null && diagonal.type() == FallingType.GAS) {
                    if (diagonal.density() > density()) {
                        sinkingProcess += (diagonal.density() - density()) / diagonal.density();
                        if (sinkingProcess >= 1) {
                            sinkingProcess = 0;
                            moved = true;
                            lastStepTick = tick;
                            grid.set(x - dir, y + 1, this);
                            grid.set(x, y, diagonal);
                        } else {
                            grid.set(x, y, this);
                        }
                    } else
                        sinkingProcess = 0;
                }
            }
        }
        //check the side blocks
        if(!moved){
            Element side = grid.get(x + dir, y);
            if (side == null && (grid.valid(x + dir, y) || FallingData.invalidPassThrough)) {
                grid.set(x + dir, y, this);
                grid.set(x, y, null);
                moved = true;
            } else {
                side = grid.get(x - dir, y);
                if (side == null && (grid.valid(x - dir, y) || FallingData.invalidPassThrough)) {
                    grid.set(x - dir, y, this);
                    grid.set(x, y, null);
                    moved = true;
                }
            }
        }
        return moved;
    }
}
