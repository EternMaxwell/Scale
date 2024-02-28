package core.game.fallingsand.fulltry;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;

public abstract class FluidSolid extends Element {
    private int lastTick = -1;
    private float velocity = 0.7f;
    private float sinkingProcess = 0.0f;
    private boolean falling = true;

    /**
     * @return FluidSolid type.
     */
    @Override
    public int type() {
        return FallingType.FLUIDSOLID;
    }

    @Override
    public boolean step(Grid grid, int x, int y, int tick){
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
        if(below!=null && below.type() == FallingType.FLUID){
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
            if(side != null && side.type()==FallingType.FLUID ){
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
                if(side != null && side.type()==FallingType.FLUID ){
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
        //if moved then update lastTick
        if(moved){
            lastTick = tick;
        }

        return moved;
    }

    /**
     * get if the element is free fall.
     * @return true if the element is free fall.
     */
    @Override
    public boolean freeFall(){
        return falling;
    }

    /**
     * get the last step tick.
     * @return the last step tick.
     */
    @Override
    public int lastStepTick() {
        return lastTick;
    }
}
