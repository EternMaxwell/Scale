package core.game.fallingsand.fulltry.ui;

import core.game.fallingsand.Element;
import core.game.fallingsand.fulltry.FallingData;
import core.game.fallingsand.fulltry.FallingInput;
import core.game.fallingsand.fulltry.elements.Elements;
import core.render.EasyRender;
import org.joml.Matrix4f;

import static core.game.fallingsand.fulltry.FallingType.*;
import static org.lwjgl.glfw.GLFW.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import core.game.fallingsand.fulltry.FallingType.*;

public class UIManager {
    public Map<String, UIPage> pages;
    public UIPage currentPage;
    public String currentName;
    FallingInput fallingInput = new FallingInput(FallingData.world.grid);

    public UIManager() {
        pages = new HashMap<>();
        addPage("default", new UIPage(this) {
            UIWindow world_tool_window = new UIWindow(this.manager, "world_tool_window", 0,0,0.8f, 0.46f) {
                boolean dragging = false;
                float[] draggingStartPos = new float[2];
                double[] draggingStartMousePos = new double[2];

                @Override
                public void init() {
                    components.putIfAbsent("density_scroll", new UIScroller(this.manager, 0.05f, 0.37f, 0.3f,
                            new UIButton(this.manager, 0, 0, 0.015f, 0.02f, "scroller") {
                        @Override
                        public void render(EasyRender render) {
                            render.triangle.setViewMatrix(new Matrix4f().identity());
                            if(!isHovered()) {
                                render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 1, 0.8f);
                                render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 1, 0.8f);
                            }else {
                                render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 0, 0.8f);
                                render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 0, 0.8f);
                            }
                        }

                        @Override
                        public void handleInput() {

                        }

                        @Override
                        public void update() {

                        }
                    }, 0.01f) {
                        @Override
                        public void render(EasyRender render) {
                            render.triangle.setViewMatrix(new Matrix4f().identity());
                            render.triangle.drawTriangle2D(x, y, x + length, y, x + length, y + height, 1,1,1,0.5f);
                            render.triangle.drawTriangle2D(x, y, x, y + height, x + length, y + height, 1,1,1,0.5f);
                            scroller.render(render);
                            render.triangle.flush();
                            render.text.setViewMatrix(new Matrix4f().identity());
                            render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                            render.text.drawTextRelative(x + length + 0.05f,y + height / 2,0.04f,1,1,1,1,String.format("density: %2.2f", scrollValue()),
                                    new java.awt.Font("Arial", Font.PLAIN, 12), 0, 0.5f);
                        }

                        @Override
                        public void handleInput() {
                            super.handleInput();
                            fallingInput.density = scrollValue();
                        }

                        @Override
                        public void update() {
                            scroller.update();
                        }
                    });
                    addComponent("radius_scroll", new UIScroller(this.manager, 0.05f, 0.34f, 0.3f, new UIButton(this.manager, 0, 0, 0.015f, 0.02f, "scroller") {
                        @Override
                        public void render(EasyRender render) {
                            render.triangle.setViewMatrix(new Matrix4f().identity());
                            if(!isHovered()) {
                                render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 1, 0.8f);
                                render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 1, 0.8f);
                            }else {
                                render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 0, 0.8f);
                                render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 0, 0.8f);
                            }
                        }

                        @Override
                        public void handleInput() {

                        }

                        @Override
                        public void update() {

                        }
                    }, 0.01f) {
                        @Override
                        public void render(EasyRender render) {
                            render.triangle.setViewMatrix(new Matrix4f().identity());
                            render.triangle.drawTriangle2D(x, y, x + length, y, x + length, y + height, 1,1,1,0.5f);
                            render.triangle.drawTriangle2D(x, y, x, y + height, x + length, y + height, 1,1,1,0.5f);
                            scroller.render(render);
                            render.triangle.flush();
                            render.text.setViewMatrix(new Matrix4f().identity());
                            render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                            render.text.drawTextRelative(x + length + 0.05f,y + height / 2,0.04f,1,1,1,1,String.format("radios: %d", 1 + (int) (scrollValue() * 124)),
                                    new java.awt.Font("Arial", Font.PLAIN, 12), 0, 0.5f);
                        }

                        @Override
                        public void handleInput() {
                            super.handleInput();
                            fallingInput.radius = 1 + (int) (scrollValue() * 124);
                        }

                        @Override
                        public void update() {
                            scroller.update();
                        }
                    });
                    addComponent("element_chooser", new UIButton(this.manager, 0.05f, 0.29f, 0.3f, 0.04f, "element_chooser") {
                        int elementIndex = 0;
                        String[] elementNames = Elements.elements.keySet().toArray(new String[0]);
                        @Override
                        public void render(EasyRender render) {
                            render.text.setViewMatrix(new Matrix4f().identity());
                            render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                            if(!isHovered())
                                render.text.drawTextRelative(x + width / 2, y + height / 2, 0.04f, 1, 1, 1, 1, elementNames[elementIndex],
                                        new java.awt.Font("Arial", Font.PLAIN, 12), 0.5f, 0.5f);
                            else
                                render.text.drawTextRelative(x + width / 2, y + height / 2, 0.04f, 1, 1, 0, 1, ">"+elementNames[elementIndex]+"<",
                                        new java.awt.Font("Arial", Font.PLAIN, 12), 0.5f, 0.5f);
                            render.text.drawTextRelative(x + width + 0.05f, y + height / 2, 0.04f, 1, 1, 1, 1,
                                    switch (Elements.newInstanceFromName(elementNames[elementIndex]).type()){
                                        case SOLID -> "solid";
                                        case FLUID -> "liquid";
                                        case GAS -> "gas";
                                        case FLUIDSOLID -> "powder";
                                        default -> "unknown";
                                    },
                                    new java.awt.Font("Arial", Font.PLAIN, 12), 0, 0.5f);
                            render.triangle.setViewMatrix(new Matrix4f().identity());
                            float squareX = x + width + 0.25f;
                            Element element = Elements.newInstanceFromName(elementNames[elementIndex]);
                            float r = element.defaultColor()[0];
                            float g = element.defaultColor()[1];
                            float b = element.defaultColor()[2];
                            float a = element.defaultColor()[3];
                            if(element != null) {
                                render.triangle.drawTriangle2D(squareX, y, squareX + 0.04f, y, squareX + 0.04f, y + height, r, g, b, a);
                                render.triangle.drawTriangle2D(squareX, y, squareX, y + height, squareX + 0.04f, y + height, r, g, b, a);
                            }
                        }

                        @Override
                        public void handleInput() {
                            if(isClicked()){
                                elementIndex++;
                                if(elementIndex >= elementNames.length)
                                    elementIndex = 0;
                                fallingInput.elementName = elementNames[elementIndex];
                            }
                        }

                        @Override
                        public void update() {

                        }
                    });
                    addComponent("action_chooser", new UIButton(this.manager, 0.05f, 0.24f, 0.3f, 0.04f, "action_chooser") {
                        String actions[] = {"put", "delete", "heat"};
                        int actionIndex = 0;
                        @Override
                        public void render(EasyRender render) {
                            render.text.setViewMatrix(new Matrix4f().identity());
                            render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                            if(!isHovered())
                                render.text.drawTextRelative(x + width / 2, y + height / 2, 0.04f, 1, 1, 1, 1, fallingInput.action,
                                        new java.awt.Font("Arial", Font.PLAIN, 12), 0.5f, 0.5f);
                            else
                                render.text.drawTextRelative(x + width / 2, y + height / 2, 0.04f, 1, 1, 0, 1, ">"+fallingInput.action+"<",
                                        new java.awt.Font("Arial", Font.PLAIN, 12), 0.5f, 0.5f);
                        }

                        @Override
                        public void handleInput() {
                            if(isClicked()){
                                actionIndex++;
                                if(actionIndex >= actions.length)
                                    actionIndex = 0;
                                fallingInput.action = actions[actionIndex];
                            }
                        }

                        @Override
                        public void update() {

                        }
                    });
                    move(0.3f,0.3f);
                }

                private boolean isOnBar(){
                    float mouseX = (float) FallingData.inputTool.mousePosX();
                    float mouseY = (float) FallingData.inputTool.mousePosY();
                    return mouseX > x && mouseX < x + width && mouseY > y + height - 0.06f && mouseY < y + height;
                }

                @Override
                public void renderOwn(EasyRender render) {
                    render.triangle.setViewMatrix(new Matrix4f().identity());
                    render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 0,0,0,0.5f);
                    render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 0,0,0,0.5f);
                    render.triangle.flush();
                    render.line.setViewMatrix(new Matrix4f().identity());
                    render.line.drawLine2D(x, y, x + width, y, 1, 1, 1, 1);
                    render.line.drawLine2D(x, y, x, y + height, 1, 1, 1, 1);
                    render.line.drawLine2D(x + width, y, x + width, y + height, 1, 1, 1, 1);
                    render.line.drawLine2D(x, y + height, x + width, y + height, 1, 1, 1, 1);
                    render.line.drawLine2D(x, y + height - 0.06f, x + width, y + height - 0.06f, 1, 1, 1, 1);
                    render.line.flush();
                    render.text.setViewMatrix(new Matrix4f().identity());
                    render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                    render.text.drawTextRelative(x + width / 2, y + height - 0.03f, 0.04f, 1, 1, 1, 1, "world tool",
                            new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                    render.text.flush();
                }

                @Override
                public void handleInputOwn() {
                    if(FallingData.inputTool.isMouseReleased(GLFW_MOUSE_BUTTON_LEFT)){
                        dragging = false;
                    }
                    if(FallingData.inputTool.isMouseJustPressed(GLFW_MOUSE_BUTTON_LEFT) && isOnBar()){
                        draggingStartPos[0] = x;
                        draggingStartPos[1] = y;
                        draggingStartMousePos[0] = FallingData.inputTool.mousePosX();
                        draggingStartMousePos[1] = FallingData.inputTool.mousePosY();
                        dragging = true;
                    }
                    if(dragging){
                        float movex = draggingStartPos[0] + (float) (FallingData.inputTool.mousePosX() - draggingStartMousePos[0]) - x;
                        float movey = draggingStartPos[1] + (float) (FallingData.inputTool.mousePosY() - draggingStartMousePos[1]) - y;
                        move(movex, movey);
                    }
                }

                @Override
                public void updateOwn() {
                }

                @Override
                public void moveOwn(float x, float y) {
                    this.x += x;
                    this.y += y;
                }
            };
            boolean window_show = false;

            @Override
            public void render(EasyRender render) {
                fallingInput.render(render);
                if(window_show)
                    world_tool_window.render(render);
            }

            @Override
            public void handleInput() {
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_ESCAPE)) {
                    manager.setCurrent("menu");
                    FallingData.pause = true;
                }
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_P)) {
                    FallingData.pause = !FallingData.pause;
                }
                if(fallingInput.putting || fallingInput.deleting || (manager.currentName.equals("default") && !(window_show && world_tool_window.isHovered())))
                    fallingInput.input(FallingData.window.id());
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_TAB)){
                    window_show = !window_show;
                }
                if(window_show)
                    world_tool_window.handleInput();
            }

            @Override
            public void update() {

            }
        });
        setCurrent("default");
        addPage("menu", new UIPage(this) {
            UIButton button = new UIButton(this.manager, -0.1f, 0.07f, 0.2f, 0.06f, "continue") {
                @Override
                public void render(EasyRender render) {
                    render.text.setViewMatrix(new Matrix4f().identity());
                    if(!isHovered())
                        render.text.drawTextRelative(centerX(),centerY(),0.05f,1,1,1,1,text,
                                new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                    else
                        render.text.drawTextRelative(centerX(),centerY(),0.05f,1,1,0,1,">"+text+"<",
                                new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                }

                @Override
                public void handleInput() {
                    if(isClicked()){
                        manager.setCurrent("default");
                        FallingData.pause = false;
                    }
                }

                @Override
                public void update() {

                }
            };
            UIButton button1 = new UIButton(this.manager, -0.05f, 0, 0.1f, 0.06f, "exit") {
                @Override
                public void render(EasyRender render) {
                    render.text.setViewMatrix(new Matrix4f().identity());
                    if(isHovered())
                        render.text.drawTextRelative(centerX(),centerY(),0.05f,1,1,0,1,text,
                                new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                    else
                        render.text.drawTextRelative(centerX(),centerY(),0.05f,1,1,1,1,">"+text+"<",
                                new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                }

                @Override
                public void handleInput() {
                    if(isClicked()){
                        glfwSetWindowShouldClose(FallingData.window.id(), true);
                    }
                }

                @Override
                public void update() {

                }
            };
            @Override
            public void render(EasyRender render) {
                render.triangle.setViewMatrix(new Matrix4f().identity());
                render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, render.window.ratio(), -1,
                        render.window.ratio(), 1, 0,0,0,0.5f);
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, -render.window.ratio(), 1,
                        render.window.ratio(), 1, 0,0,0,0.5f);

                render.text.drawTextRelative(0,0.5f,0.2f,1,1,1,1,"MENU", new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                render.triangle.flush();
                render.text.flush();
                button.render(render);
                button1.render(render);
            }

            @Override
            public void handleInput() {
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_ESCAPE)) {
                    manager.setCurrent("default");
                    FallingData.pause = false;
                }
                button.handleInput();
                button1.handleInput();
            }

            @Override
            public void update() {
                button.update();
                button1.update();
            }
        });
        addPage("world_tool", new UIPage(this) {
            UIScroller scroller = new UIScroller(this.manager, -0.3f, 0.3f, 0.3f, new UIButton(this.manager, 0, 0, 0.015f, 0.02f, "scroller") {
                @Override
                public void render(EasyRender render) {
                    render.triangle.setViewMatrix(new Matrix4f().identity());
                    if(!isHovered()) {
                        render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 1, 0.8f);
                        render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 1, 0.8f);
                    }else {
                        render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 0, 0.8f);
                        render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 0, 0.8f);
                    }
                }

                @Override
                public void handleInput() {

                }

                @Override
                public void update() {

                }
            }, 0.01f) {
                @Override
                public void render(EasyRender render) {
                    render.triangle.setViewMatrix(new Matrix4f().identity());
                    render.triangle.drawTriangle2D(x, y, x + length, y, x + length, y + height, 1,1,1,0.5f);
                    render.triangle.drawTriangle2D(x, y, x, y + height, x + length, y + height, 1,1,1,0.5f);
                    scroller.render(render);
                    render.triangle.flush();
                    render.text.setViewMatrix(new Matrix4f().identity());
                    render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                    render.text.drawTextRelative(-x,y + height / 2,0.04f,1,1,1,1,String.format("density: %2.2f", scrollValue()),
                            new java.awt.Font("Arial", Font.PLAIN, 64), 1, 0.5f);
                }

                @Override
                public void handleInput() {
                    super.handleInput();
                    fallingInput.density = scrollValue();
                }

                @Override
                public void update() {
                    scroller.update();
                }
            };
            UIScroller scroller2 = new UIScroller(this.manager, -0.3f, 0.27f, 0.3f, new UIButton(this.manager, 0, 0, 0.015f, 0.02f, "scroller") {
                @Override
                public void render(EasyRender render) {
                    render.triangle.setViewMatrix(new Matrix4f().identity());
                    if(!isHovered()) {
                        render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 1, 0.8f);
                        render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 1, 0.8f);
                    }else {
                        render.triangle.drawTriangle2D(x, y, x + width, y, x + width, y + height, 1, 1, 0, 0.8f);
                        render.triangle.drawTriangle2D(x, y, x, y + height, x + width, y + height, 1, 1, 0, 0.8f);
                    }
                }

                @Override
                public void handleInput() {

                }

                @Override
                public void update() {

                }
            }, 0.01f) {
                @Override
                public void render(EasyRender render) {
                    render.triangle.setViewMatrix(new Matrix4f().identity());
                    render.triangle.drawTriangle2D(x, y, x + length, y, x + length, y + height, 1,1,1,0.5f);
                    render.triangle.drawTriangle2D(x, y, x, y + height, x + length, y + height, 1,1,1,0.5f);
                    scroller.render(render);
                    render.triangle.flush();
                    render.text.setViewMatrix(new Matrix4f().identity());
                    render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                    render.text.drawTextRelative(-x,y + height / 2,0.04f,1,1,1,1,String.format("radios: %d", (int) (scrollValue() * 125)),
                            new java.awt.Font("Arial", Font.PLAIN, 64), 1, 0.5f);
                }

                @Override
                public void handleInput() {
                    super.handleInput();
                    fallingInput.radius = (int) (scrollValue() * 125);
                }

                @Override
                public void update() {
                    scroller.update();
                }
            };
            @Override
            public void render(EasyRender render) {
                render.triangle.setViewMatrix(new Matrix4f().identity());
                render.text.setProjectionMatrix(new Matrix4f().ortho(-render.window.ratio(), render.window.ratio(), -1, 1, -1, 1));
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, render.window.ratio(), -1,
                        render.window.ratio(), 1, 0,0,0,0.5f);
                render.triangle.drawTriangle2D(-render.window.ratio(), -1, -render.window.ratio(), 1,
                        render.window.ratio(), 1, 0,0,0,0.5f);

                render.text.drawTextRelative(0,0.5f,0.1f,1,1,1,1,"WORLD_TOOL", new java.awt.Font("Arial", Font.BOLD, 12), 0.5f, 0.5f);
                render.triangle.flush();
                render.text.flush();
                scroller.render(render);
                scroller2.render(render);
            }

            @Override
            public void handleInput() {
                if(FallingData.inputTool.isKeyJustPressed(GLFW_KEY_ESCAPE) || FallingData.inputTool.isKeyJustPressed(GLFW_KEY_TAB)) {
                    manager.setCurrent("default");
                }
                scroller.handleInput();
                scroller2.handleInput();
            }

            @Override
            public void update() {

            }
        });
    }

    public void addPage(String name, UIPage page){
        pages.put(name, page);
    }

    public void setCurrent(String name){
        currentName = name;
        currentPage = pages.get(name);
    }

    public void render(EasyRender render) {
        if(currentPage == null) return;
        currentPage.render(render);
    }

    public void handleInput() {
        if(currentPage == null) return;
        currentPage.handleInput();
    }

    public void update() {
        if(currentPage == null) return;
        currentPage.update();
    }

    public String getCurrentName() {
        return currentName;
    }
}
