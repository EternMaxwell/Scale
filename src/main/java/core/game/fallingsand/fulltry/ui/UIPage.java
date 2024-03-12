package core.game.fallingsand.fulltry.ui;

import java.util.Map;

public abstract class UIPage extends UIComponent{
    public UIPage parent;
    public Map<String,UIPage> children;

    public UIPage(UIManager manager) {
        super(manager);
    }

    public void move(float x, float y){
        for(UIComponent component : children.values()){
            component.move(x,y);
        }
    }
}
