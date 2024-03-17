package core.game.fallingsand.fulltry.box2d;

import org.jbox2d.dynamics.Body;

public class FallingBody {
    public Body body;

    public FallingBody(Body body) {
        this.body = body;
    }
    public Body getBox2dBody() {
        return body;
    }
}
