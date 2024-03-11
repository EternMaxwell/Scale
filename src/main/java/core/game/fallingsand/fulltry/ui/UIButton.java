package core.game.fallingsand.fulltry.ui;

import core.game.fallingsand.fulltry.FallingData;
import core.render.EasyRender;

import static org.lwjgl.glfw.GLFW.*;

public abstract class UIButton extends UIComponent {
    public String text;
    public float x, y, width, height;
    public boolean clipped = false;
    public float clipX, clipY, clipWidth, clipHeight;

    public UIButton(UIManager manager, float x, float y, float width, float height, String text) {
        super(manager);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public UIButton(UIManager manager, float x, float y, float width, float height, String text, float clipX, float clipY, float clipWidth, float clipHeight) {
        super(manager);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.clipped = true;
        this.clipX = clipX;
        this.clipY = clipY;
        this.clipWidth = clipWidth;
        this.clipHeight = clipHeight;
    }

    public abstract void render(EasyRender render);

    public float centerX() {
        return x + width / 2;
    }

    public float centerY() {
        return y + height / 2;
    }

    public boolean isClipped() {
        float mouseX = (float) FallingData.inputTool.mousePosX();
        float mouseY = (float) FallingData.inputTool.mousePosY();
        return clipped && (mouseX < clipX || mouseX > clipX + clipWidth || mouseY < clipY || mouseY > clipY + clipHeight);
    }

    public boolean isHovered() {
        float mouseX = (float) FallingData.inputTool.mousePosX();
        float mouseY = (float) FallingData.inputTool.mousePosY();
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean isClicked() {
        return isHovered() && FallingData.inputTool.isMouseJustPressed(GLFW_MOUSE_BUTTON_LEFT) && !isClipped();
    }

    public abstract void handleInput();

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }
}