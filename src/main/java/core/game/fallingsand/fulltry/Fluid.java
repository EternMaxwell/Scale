package core.game.fallingsand.fulltry;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

public abstract class Fluid extends Element {
    boolean falling = true;
    int lastTick = -1;
    float sinkingProcess = 0.0f;
    float velocity = 0.7f;

    /**
     * @return the dispersion rate.
     */
    public abstract int dispersionRate();

    /**
     * @return Fluid type.
     */
    @Override
    public int type() {
        return FallingType.FLUID;
    }

    /**
     * get if the fluid is free-falling.
     * @return true if the fluid is free-falling.
     */
    public boolean freeFall() {
        return falling;
    }

    /**
     * get the last step tick.
     * @return the last step tick.
     */
    public int lastStepTick() {
        return lastTick;
    }

    @Override
    public boolean step(Grid grid, int x, int y, int tick) {
        boolean moved = false;

        int distance = velocity > 1 ? (int) velocity : 1;
        int iy = y;
        //falling
        for (int i = 0; i < distance; i++) {
            Element below = grid.get(x, iy - 1);
            if ((below == null || below.type() == FallingType.GAS) && (grid.valid(x, iy - 1) || FallingData.invalidPassThrough)) {
                falling = true;
                moved = true;
                grid.set(x, iy -1, this);
                grid.set(x, iy, below);
                lastTick = tick;
                iy--;
            }else if(below != null){
                falling = grid.get(x, iy - 1).freeFall();
                if(!falling)
                    velocity = 0.7f;
                break;
            }else {
                falling = false;
                lastTick = tick;
                velocity = 0.7f;
                return moved;
            }
        }
        //if moved then update velocity and return true
        if(moved){
            lastTick = tick;
            velocity += 0.1f;
            velocity *= 0.985f;
            return true;
        }
        //if didn't move then check the block below
        //if the block below is liquid then move to it
        Element below = grid.get(x, iy - 1);
        if(below!=null && (below.type() == FallingType.FLUID || below.type() == FallingType.FLUIDSOLID)){
            if(below.density() >= density()){
                sinkingProcess = 0.0f;
            }else {
                sinkingProcess += (density() - below.density())/density();
                lastTick = tick;
                grid.set(x, iy, this);
                if(sinkingProcess >= 1) {
                    grid.set(x, iy, grid.get(x, iy - 1));
                    grid.set(x, iy - 1, this);
                    sinkingProcess = 0.0f;
                    return true;
                }
            }
            return false;
        }
        //check the block at the bottom left and right
        //if the block is empty then move to it
        int dir = Math.random()>=0.5?1:-1;
        Element side = grid.get(x + dir, iy - 1);
        if ((side == null || side.type() == FallingType.GAS) && (grid.valid(x + dir, iy - 1) || FallingData.invalidPassThrough)) {
            grid.set(x + dir, iy - 1, this);
            grid.set(x, iy, side);
            moved = true;
        } else {
            side = grid.get(x - dir, iy - 1);
            if ((side == null || side.type() == FallingType.GAS) && (grid.valid(x - dir, iy - 1) || FallingData.invalidPassThrough)) {
                grid.set(x - dir, iy - 1, this);
                grid.set(x, iy, side);
                moved = true;
            }
        }
        //if the block at the bottom left and right is liquid then try sinking
        if (!moved) {
            side = grid.get(x + dir, iy-1);
            if(side != null && (side.type() == FallingType.FLUID || side.type() == FallingType.FLUIDSOLID)){
                if(side.density() >= density()){
                    sinkingProcess = 0.0f;
                }else {
                    sinkingProcess += (density() - side.density()) / density();
                    lastTick = tick;
                    grid.set(x, iy, this);
                    if (sinkingProcess >= 1) {
                        grid.set(x + dir, iy - 1, this);
                        grid.set(x, iy, side);
                        sinkingProcess = 0.0f;
                        moved = true;
                    }
                }
            }else {
                side = grid.get(x - dir, iy-1);
                if(side != null && (side.type() == FallingType.FLUID || side.type() == FallingType.FLUIDSOLID)){
                    if(side.density() >= density()){
                        sinkingProcess = 0.0f;
                    }else {
                        sinkingProcess += (density() - side.density()) / density();
                        lastTick = tick;
                        grid.set(x, iy, this);
                        if (sinkingProcess >= 1) {
                            grid.set(x - dir, iy - 1, this);
                            grid.set(x, iy, side);
                            sinkingProcess = 0.0f;
                            moved = true;
                        }
                    }
                }
            }
        }
        //check the left and right blocks
        int dirL = 0;
        int idirL = 0;
        boolean dirLM = false;
        boolean idirLM = false;
        if (!moved) {
            for(int i = 1; i <= dispersionRate(); i++){
                if(!dirLM){
                    side = grid.get(x + dir * i, iy);
                    if(side == null || side.type() == FallingType.GAS){
                        dirL = i;
                    }else
                        dirLM = true;
                }
                if(!idirLM){
                    side = grid.get(x - dir * i, iy);
                    if(side == null || side.type() == FallingType.GAS){
                        idirL = i;
                    }else
                        idirLM = true;
                }
            }
            if(!(dirL == 0 && idirL == 0)){
                if(dirL < idirL){
                    side = grid.get(x + dir * dirL, iy);
                    grid.set(x + dir * idirL, iy, this);
                    grid.set(x, iy, side);
                }else {
                    side = grid.get(x - dir * idirL, iy);
                    grid.set(x - dir * dirL, iy, this);
                    grid.set(x, iy, side);
                }
                moved = true;
            }
        }
        //if moved then update lastTick
        if(moved){
            lastTick = tick;
        }

        return moved;
    }
}
