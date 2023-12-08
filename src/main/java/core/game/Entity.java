package core.game;

import core.render.EasyRender;
import org.joml.Vector2f;

public abstract class Entity {
    private Point globalPos;
    private Entity prev, next;
    private Vector2f dir;

    public Entity(Point globalPos){
        this.globalPos = globalPos;
    }

    /**
     * get the global point of the entity.
     * @return the global point.
     */
    public Point globalPos(){
        return globalPos;
    }

    /**
     * get the previous entity.
     * @return the previous entity.
     */
    public Entity prev(){
        return prev;
    }

    /**
     * set the previous entity.
     * @param prev The new previous entity.
     */
    public void setPrev(Entity prev){
        this.prev = prev;
    }

    /**
     * get the next entity.
     * @return the next entity.
     */
    public Entity next(){
        return next;
    }

    /**
     * set the next entity.
     * @param next The new next entity.
     */
    public void setNext(Entity next) {
        this.next = next;
    }

    /**
     * move the entity by the given delta with clamping.
     * @param delta The delta.
     * @return true if the entity was clamped, false otherwise.
     */
    public boolean clampMove(Vector2f delta, Vector2f[] dirs){
        Container container = globalPos.container();
        boolean result = globalPos.clampMove(delta, dirs);
        if(globalPos.container() != container){
            container.removeEntity(this);
            globalPos.container().addEntity(this);

        }
        return result;
    }

    /**
     * move the entity by the given delta with clamping and a max number of iterations.
     * @param delta The delta.
     * @param maxIterations The max number of iterations.
     * @return true if the entity was clamped, false otherwise.
     */
    public boolean clampMove(Vector2f delta, int maxIterations, Vector2f[] dirs){
        Container container = globalPos.container();
        boolean result = globalPos.clampMove(delta, maxIterations, dirs);
        if(globalPos.container() != container){
            container.removeEntity(this);
            globalPos.container().addEntity(this);
        }
        return result;
    }

    public abstract void renderFlat(EasyRender renderer);
    public abstract void renderLinearStretch(EasyRender renderer);
}
