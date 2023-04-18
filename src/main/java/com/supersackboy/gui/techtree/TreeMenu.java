package com.supersackboy.gui.techtree;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.SparseGraph;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class TreeMenu extends Screen {
    ArrayList<TreeNode> buttons;
    protected TreeMenu(TreeNode[] buttons) {
        super(Text.literal("TechTree"));
        this.buttons = new ArrayList<>();
        for(TreeNode btn : buttons) {
            if (btn != null) {
                this.buttons.add(btn);
            }
        }
    }
    public boolean shouldPause() {return false;}

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Color color = new Color(32, 32, 32, 204);
        DrawableHelper.fill(matrices,0,0,this.width,this.height,rgbaToArgb(color));

        for(TreeNode btn : buttons) {
            if(btn.isRoot) {
                layout.setLocation(btn,width/2f-btn.getWidth()/2f,height/2f-btn.getHeight()/2f);
            }
            btn.setPos((int) (lerp(btn.getX(),layout.getX(btn)+offsetX,0.1))
                    , (int) (lerp(btn.getY(),layout.getY(btn)+offsetY,0.1)));
        }

        layout.step();
        panView(mouseX,mouseY);
        super.render(matrices,mouseX,mouseY,delta);
    }

    private SparseGraph<TreeNode, Integer> graph;
    private SpringLayout<TreeNode, Integer> layout;
    @Override
    protected void init() {
        super.init();

        for(TreeNode btn : buttons) {
            if(btn.prerequisitesString != null) {
                for (String string : btn.prerequisitesString) {
                    for (TreeNode search : buttons) {
                        if (string.equals(search.id)) {
                            btn.prerequisites.add(search);
                        }
                    }
                }
            }
        }

        graph = new SparseGraph<>();
        int edge = 1;
        for (TreeNode btn : buttons) {
            this.addDrawable(btn);
            graph.addVertex(btn);
            if(btn.prerequisites != null) {
                for (TreeNode btn2 : btn.prerequisites) {
                    graph.addEdge(edge,btn,btn2);
                    edge++;
                }
            }
        }
        layout = new SpringLayout<>(graph);
        layout.initialize();
        Dimension size = new Dimension(width,height);
        layout.setSize(size);

        for(TreeNode btn: buttons) {
            layout.setLocation(btn, width/2f,height/2f);
        }

    }
    public boolean dragging() {
        return isMouseDown(0) && !isDragging();
    }
    /**
     * 0 = left
     * <p>1 = right
     */
    public static boolean isMouseDown(int button) {
        return GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), button) == 1;
        //0 = left
        //1 = right
    }
    int scrollAmount =0;
    int zoomAmount = 2;
    @Override //called everytime mouse wheel is scrolled :)
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scrollAmount += amount; //for the masochists that use trackpads
        if(scrollAmount >= 1) {
            scrollAmount = 0;
            zoomAmount += 1;
        }
        if(scrollAmount <= -1) {
            scrollAmount = 0;
            zoomAmount -= 1;
        }
        if(zoomAmount < 2) {
            zoomAmount = 2;
        }
        if(zoomAmount > 6) {
            zoomAmount = 6;
        }

        return false;
    }
    int startedPan = 0;
    int startX = 0;
    int startY = 0;
        float offsetX = 0;
    float offsetY = 0;
    float newOffsetX = 0;
    float newOffsetY = 0;
    float originalPositionX = 0;
    float originalPositionY = 0;
    public void panView(int mouseX,int mouseY) {
        if (dragging()) {
            if (startedPan == 0) {
                originalPositionX = newOffsetX;
                originalPositionY = newOffsetY;
                startX = mouseX;
                startY = mouseY;
                startedPan = 1;
            } else {
                newOffsetX = (mouseX - startX) + originalPositionX;
                newOffsetY = (mouseY - startY) + originalPositionY;
            }
        } else {
            startedPan = 0;
        }
        offsetX = (float) lerp(offsetX,newOffsetX,0.1f);
        offsetY = (float) lerp(offsetY,newOffsetY,0.1f);
    }

    /**
     * Linearly interpolates between two values.
     *
     * @param from
     *            the start value
     * @param to
     *            the end value
     * @param p
     *            the current interpolation position, must be between 0 and 1
     * @return the result of the interpolation
     */
    public static double lerp(double from, double to, double p) {
        assert p >= 0 && p <= 1 : "interpolation position out of range";
        return from + (to - from) * p;
    }

    public static int rgbaToArgb(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
