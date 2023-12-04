package core.game;

import core.render.EasyRender;
import org.joml.Vector2f;

public class Camera {
    private final Point globalPos;
    private Vector2f dir;

    /**
     * Creates a camera with the given global point.
     *
     * @param globalPos The global point.
     */
    public Camera(Point globalPos) {
        this.globalPos = globalPos;
    }

    /**
     * get the container of the camera.
     *
     * @return the container.
     */
    public Container container() {
        return globalPos.container();
    }

    /**
     * get the position of the camera.
     *
     * @return the position.
     */
    public Vector2f pos() {
        return globalPos.pos();
    }

    /**
     * set the position of the camera.
     *
     * @param pos The new position.
     */
    public void setPos(Vector2f pos) {
        globalPos.setPos(pos);
    }

    /**
     * set the position of the camera.
     *
     * @param x The x coordinate of the new position.
     * @param y The y coordinate of the new position.
     */
    public void setPos(float x, float y) {
        globalPos.setPos(x, y);
    }

    /**
     * set the container of the camera.
     *
     * @param container The new container.
     */
    public void setContainer(Container container) {
        globalPos.setContainer(container);
    }

    /**
     * get the global point of the camera.
     *
     * @return the global point.
     */
    public Point globalPoint() {
        return globalPos;
    }

    /**
     * move the camera by the given delta with clamping.
     * @param delta The delta.
     * @param dirs the dirs that needs to transform between containers.
     * @return true if the camera was clamped, false otherwise.
     */
    public boolean clampMove(Vector2f delta, Vector2f[] dirs){
        return globalPos.clampMove(delta, dirs);
    }

    /**
     * move the camera by the given delta with clamping.
     * @param delta The delta.
     * @param max The max distance to move.
     * @param dirs the dirs that needs to transform between containers.
     * @return true if the camera was clamped, false otherwise.
     */
    public boolean clampMove(Vector2f delta, int max, Vector2f[] dirs){
        return globalPos.clampMove(delta, max, dirs);
    }

    /**
     * move the camera through the inner dir with clamping.
     * @param distanceToMove The distance to move.
     * @param maxIteration The max iteration to clamp.
     * @return true if the camera was clamped, false otherwise.
     */
    public boolean clampMoveThroughDir(float distanceToMove, int maxIteration){
        boolean result = globalPos.clampMove(dir.mul(distanceToMove), maxIteration, new Vector2f[]{dir});
        dir.normalize();
        return result;
    }

    //TODO: implement the render methods
    //TODO: before implementing, implement shaders with indexed ssbo pos, dir arguments for entity rendering.
    /**
     * render the scene with this camera setting.
     * <p>this render method use flat render which only render the current container and the nearby container with just linear transformation</p>
     * <p>used for tests and new feature preview</p>
     * @param render The render to use.
     */
    public void renderFlat(EasyRender render){

    }

    /**
     * render the scene with this camera setting.
     * <p>this render method render the scene with linear transformation and stretch the container to fit the screen</p>
     * <p>most possible to be used for the game in short term</p>
     * @param render The render to use.
     */
    public void renderLinearStretch(EasyRender render){

    }

    /**
     * render the scene with this camera setting.
     * <p>in this method, it will first call each container needs to be rendered to render a image on its coordinate</p>
     * <p>than stretch them to the screen</p>
     * @param render The render to use.
     */
    public void renderLinearStretchPreRender(EasyRender render){

    }

    /**
     * render the scene with this camera setting.
     * <p>this method render the scene with ray tracing on cpu</p>
     * <p>this is the most suitable way to render for the curved space</p>
     * <p>will ultimately integrated to gpu rendering</p>
     * @param render The render to use.
     */
    public void renderRayTraceCpu(EasyRender render){

    }
}
