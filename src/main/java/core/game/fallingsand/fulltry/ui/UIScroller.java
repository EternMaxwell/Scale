package core.game.fallingsand.fulltry.ui;

import core.game.fallingsand.fulltry.FallingData;
import core.render.EasyRender;

import static org.lwjgl.glfw.GLFW.*;

public abstract class UIScroller extends UIComponent{

    public float x, y, length, height;
    public UIButton scroller;

    public UIScroller(UIManager manager, float x, float y, float length, UIButton scroller, float height) {
        super(manager);
        this.x = x;
        this.y = y;
        this.length = length;
        this.scroller = scroller;
        scroller.x = x - scroller.width / 2;
        scroller.y = y + height / 2 - scroller.height / 2;
        this.height = height;
    }

    public boolean dragging = false;
    public boolean isHovered() {
        float mouseX = (float) FallingData.inputTool.mousePosX();
        float mouseY = (float) FallingData.inputTool.mousePosY();
        return mouseX > x && mouseX < x + length && mouseY > y && mouseY < y + scroller.height;
    }

    public float scrollValue() {
        return (scroller.x - x) / length;
    }

    public abstract void render(EasyRender render);
    public void handleInput(){
        if(FallingData.inputTool.isMouseReleased(GLFW_MOUSE_BUTTON_LEFT)){
            dragging = false;
        }
        if(FallingData.inputTool.isMouseJustPressed(GLFW_MOUSE_BUTTON_LEFT) && scroller.isHovered()){
            dragging = true;
        }
        if(dragging) {
            scroller.x = (float) FallingData.inputTool.mousePosX() - scroller.width / 2;
            if (scroller.x < x - scroller.width / 2)
                scroller.x = x - scroller.width / 2;
            if (scroller.x > x + length - scroller.width / 2)
                scroller.x = x + length - scroller.width / 2;
        }else if(FallingData.inputTool.isMouseJustPressed(GLFW_MOUSE_BUTTON_LEFT) && isHovered()){
            scroller.x = (float) FallingData.inputTool.mousePosX() - scroller.width / 2;
            if (scroller.x < x - scroller.width / 2)
                scroller.x = x - scroller.width / 2;
            if (scroller.x > x + length - scroller.width / 2)
                scroller.x = x + length - scroller.width / 2;
        }
    }
    public abstract void update();
}
