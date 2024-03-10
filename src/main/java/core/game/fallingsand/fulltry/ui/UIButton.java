package core.game.fallingsand.fulltry.ui;

import core.game.fallingsand.fulltry.FallingData;
import core.render.EasyRender;

import static org.lwjgl.glfw.GLFW.*;

public abstract class UIButton extends UIComponent {
    public String text;
    public float x, y, width, height;

    public UIButton(UIManager manager, float x, float y, float width, float height, String text) {
        super(manager);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public abstract void render(EasyRender render);

    public float centerX() {
        return x + width / 2;
    }

    public float centerY() {
        return y + height / 2;
    }

    public boolean isHovered() {
        float mouseX = (float) FallingData.inputTool.mousePosX();
        float mouseY = (float) FallingData.inputTool.mousePosY();
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean isClicked() {
        return isHovered() && FallingData.inputTool.isMouseJustPressed(GLFW_MOUSE_BUTTON_LEFT);
    }

    public abstract void handleInput();
}