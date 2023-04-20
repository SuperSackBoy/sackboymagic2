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
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TreeMenu extends Screen {
    ArrayList<TreeNode> buttons;
    ArrayList<TreeSideBar> sidebars;
    TreeSideBar menuOpen = null;
    protected TreeMenu(TreeNode[] buttons, TreeSideBar[] menus) {
        super(Text.literal("TechTree"));
        this.buttons = new ArrayList<>();
        for(TreeNode btn : buttons) { //get list of buttons
            if (btn != null) {
                this.buttons.add(btn);
            }
        }
        this.sidebars = new ArrayList<>(); //get list of sidebars
        for(TreeSideBar menu : menus) {
            if (menu != null) {
                this.sidebars.add(menu);
            }
        }

        for(TreeNode btn : buttons) { //link buttons together
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
    }
    public boolean shouldPause() {return false;}

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Color color = new Color(32, 32, 32, 204); //draw the background
        DrawableHelper.fill(matrices,0,0,this.width,this.height,rgbaToArgb(color));

        for(TreeNode btn : buttons) { //set each button to its proper location
            if(btn.isRoot) {
                layout.setLocation(btn,width/2f,height/2f);
            }
            btn.setPos((int) (lerp(btn.getX(),layout.getX(btn)+offsetX,0.1))
                    , (int) (lerp(btn.getY(),layout.getY(btn)+offsetY,0.1)));
        }

        layout.step(); //update the fancy layout bullshit
        panView(mouseX,mouseY); //run code to pan the menu around
        super.render(matrices,mouseX,mouseY,delta);
    }

    private SparseGraph<TreeNode, Integer> graph;
    private SpringLayout<TreeNode, Integer> layout;
    @Override
    protected void init() { //called every time the menu is opened
        super.init();
        menuOpen = null; //reset the menu open variable


        graph = new SparseGraph<>(); //create the graph
        int edge = 1;
        for (TreeNode btn : buttons) {
            this.addDrawable(btn); //add the buttons to the screen
            graph.addVertex(btn); //add the buttons to the graph
            if(btn.prerequisites != null) {
                for (TreeNode btn2 : btn.prerequisites) {
                    graph.addEdge(edge,btn,btn2); //connect the buttons
                    edge++;
                }
            }
        }
        if(layout == null) { //run only once (idk why this isn't in the creation method)
            layout = new SpringLayout<>(graph); //create a new layout
            layout.initialize(); //start the layout
            Dimension size = new Dimension(width, height);
            layout.setSize(size); //set the layouts size and properties

            layout.setStretch(0.7); //def: 0.7
            layout.setRepulsionRange(100); //def 100
            layout.setForceMultiplier(1); //def: 1/3
        }
        for(TreeSideBar menu : sidebars) { //link the menus to the buttons (again this could have been in the startup)
            menu.init(this);
            for(TreeNode btn : buttons) {
                if(btn.id.equals(menu.title)) {
                    menu.connect(btn);
                    break;
                }
            }
        }
    }
    public boolean dragging() { //if the user is dragging the background
        return isMouseDown(0) && !mouseOverButton();
    }

    public boolean draggingNode() { //if the user is dragging a button
        return isMouseDown(0) && mouseOverButton();
    }
    public boolean mouseOverButton() { //if the user is hovering over a button
        for(TreeNode btn : buttons) {
            if (btn.isHovered()) {
                return true;
            }
        }
        return false;
    }
    public TreeNode getHovered() { //get the hovered button
        for(TreeNode btn : buttons) {
            if (btn.isHovered()) {
                return btn;
            }
        }
        return null;
    }

    /**
     * Checks if a mouse button is down
     * <p>0 = left
     * <p>1 = right
     */
    public static boolean isMouseDown(int button) { //checks if a mouse button is down
        return GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), button) == 1;
        //0 = left
        //1 = right
    }
    /* old code to zoom in and out, i don't want to code it though it's annoying to scale shit
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

     */
    int startedPan = 0;
    int startX = 0;
    int startY = 0;
        float offsetX = 0;
    float offsetY = 0;
    float newOffsetX = 0;
    float newOffsetY = 0;
    float originalPositionX = 0;
    float originalPositionY = 0;
    int pan;
    Point2D point;
    public void panView(int mouseX,int mouseY) {
        if(menuOpen == null || mouseX < width/3*2) { //if the side menu is closed, or if the mouse is not on the side menu
            if (isMouseDown(0) && pan == 0) { //called once as soon as the mouse is pressed down
                if (dragging()) { //if the user is dragging the background set pan to 1
                    pan = 1;
                } else if (draggingNode()) { //if the user is dragging a node set pan to 2
                    pan = 2;
                }
            }
            if (!isMouseDown(0)) { //set pan to 0 when the mouse is released
                pan = 0;
            }
            if (pan == 1) { //if the user is panning the background
                if (startedPan == 0) { //get the starting location of the mouse
                    originalPositionX = newOffsetX;
                    originalPositionY = newOffsetY;
                    startX = mouseX;
                    startY = mouseY;
                    startedPan = 1;
                } else { //add the current mouse location to the old mouse location
                    newOffsetX = (mouseX - startX) + originalPositionX;
                    newOffsetY = (mouseY - startY) + originalPositionY;
                }
            } else {
                startedPan = 0;
            } //linear interpolation is hot and sexy
            offsetX = (float) lerp(offsetX, newOffsetX, 0.1f);
            offsetY = (float) lerp(offsetY, newOffsetY, 0.1f);

            if (pan == 2) { //if the user is dragging a button
                if (isMouseDown(0) && latch) { //get the hovered button and starting location
                    closest = getHovered();
                    latch = false;
                    point = new Point2D.Float(mouseX, mouseY);
                }
                layout.setLocation(closest, mouseX - offsetX, mouseY - offsetY); //update the button to the mouses location
            }
            if (!isMouseDown(0)) { //activate the buttons on press event if the mouse didn't move
                latch = true;
                Point2D point2 = new Point2D.Float(mouseX, mouseY);
                if (point2.equals(point)) {
                    closest.onPress();
                }
            }
        } else { //activate the menu's on press event when its clicked
            if (menuOpen.isHovered() && isMouseDown(0)) {
                menuOpen.onPress();
            }
        }
    }

    TreeNode closest = null;
    Boolean latch = true;

    public void openMenu(TreeSideBar menu) { //open a menu on the side of the screen
        if(menuOpen != null) {
            closeMenu();
        }
        menuOpen = menu;
        this.addDrawable(menu);
    }
    public void closeMenu() { //close a menu
        if(menuOpen != null) {
            this.remove(menuOpen);
            menuOpen = null;
        }
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

        return (alpha << 24) | (red << 16) | (green << 8) | blue; //ngl I used chatGPT for this I have no idea how the fuck this works
    }
}
