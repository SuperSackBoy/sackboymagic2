package com.supersackboy.gui.techtree;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.supersackboy.MagicMod;
import com.supersackboy.util.IItemRendererMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;

public class TreeNode extends PressableWidget {
    public String[] prerequisitesString;
    public ArrayList<TreeNode> prerequisites;
    public ItemStack icon;
    public String id;
    public boolean isRoot = false;
    public boolean isUnlocked = false;
    public boolean requirementsMet = false;
    public static final Identifier TEXTURE = new Identifier(MagicMod.ModID,"textures/gui/widgets.png");
    public TreeNode(String id, String[] buttons, ItemStack icon) {
        super(0,0,16,16,Text.literal(""));
        if (buttons != null) {
            prerequisitesString = buttons;
        }
        this.id = id;
        prerequisites = new ArrayList<>();
        this.icon = icon;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = client.getItemRenderer();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        int hovered = this.isHovered() ? 1 : 0;

        int u = 0;
        /*
        if(isUnlocked) {
            u = 120;
        } else if(requirementsMet) {
            u = 0;
        } else {
            u = 60;
        }
         */

        drawTexture(matrices, this.getX(), this.getY(), u, hovered * 60, this.width / 2, this.height/2); //top left
        drawTexture(matrices, this.getX() + this.width / 2, this.getY(), u+60 - this.width / 2, hovered * 60, this.width / 2, this.height/2); //top right
        drawTexture(matrices, this.getX(), this.getY()+this.height/2, u, 60 - this.height / 2 + hovered * 60, this.width / 2, this.height/2); //bottom left
        drawTexture(matrices, this.getX() + this.width / 2, this.getY() + this.height/2, u+60 - this.width / 2, 60 - this.height / 2 + hovered * 60, this.width / 2, this.height/2); //bottom right
        renderGuiItem(icon,this.getX() + this.width/2,this.getY() + this.height/2,itemRenderer,this.height*0.75f);
        Color color = new Color(255,255,255);
        if(!prerequisites.isEmpty()) {
            for(TreeNode btn : prerequisites) {
                drawLine(matrices,this.getCenterX(),this.getCenterY(),btn.getCenterX(),btn.getCenterY(),color);
            }
        } else {
            isRoot = true;
            isUnlocked = true;
        }
    }

    public int getCenterX() {
        return this.getX()+(this.getWidth()/2);
    }
    public int getCenterY() {
        return this.getY()+(this.getHeight()/2);
    }
    public void setPos(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void onPress() {

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }


    private void drawLine(MatrixStack matrices, int x1, int y1, int x2, int y2, Color colour) { //stolen code from Bawnorton#0001 on the fabric discord :)
        RenderSystem.enableBlend();
        //RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        int r = colour.getRed();
        int g = colour.getGreen();
        int b = colour.getBlue();
        int a = colour.getAlpha();
        if(x1 > x2) {
            bufferBuilder.vertex(matrix, x2, y2 + 1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x1, y1 + 1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x1, y1 - 1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x2, y2 - 1, 0).color(r,g,b,a).next();
        } else {
            bufferBuilder.vertex(matrix, x1, y1 + 1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x2, y2 + 1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x2, y2 - 1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x1, y1 - 1, 0).color(r,g,b,a).next();
        }
        if(y1 > y2) {
            bufferBuilder.vertex(matrix, x2 + 1, y2, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x2 - 1, y2, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x1 - 1, y1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x1 + 1, y1, 0).color(r,g,b,a).next();
        } else {
            bufferBuilder.vertex(matrix, x1 + 1, y1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x1 - 1, y1, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x2 - 1, y2, 0).color(r,g,b,a).next();
            bufferBuilder.vertex(matrix, x2 + 1, y2, 0).color(r,g,b,a).next();
        }


        Tessellator.getInstance().draw();
        //RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
    protected void renderGuiItem(ItemStack stack, int x, int y, ItemRenderer itemRenderer, float scale) {
        BakedModel model = itemRenderer.getModel(stack, null, null, 0);
        boolean bl;
        TextureManager textureManager = ((IItemRendererMixin) itemRenderer).getTextureManager();
        textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y, 100.0f);
        //matrixStack.translate(8.0f, 8.0f, 0.0f);
        matrixStack.scale(1.0f, -1.0f, 1.0f);
        matrixStack.scale(scale, scale, scale);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        boolean bl2 = bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }
        itemRenderer.renderItem(stack, ModelTransformationMode.GUI, false, matrixStack2, immediate, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, model);
        immediate.draw();
        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
    }
}
