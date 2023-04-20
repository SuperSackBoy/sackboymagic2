package com.supersackboy.gui.techtree;

import com.mojang.blaze3d.systems.RenderSystem;
import com.supersackboy.MagicMod;
import com.supersackboy.networking.PacketManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

import static com.supersackboy.gui.RuneHud.RUNES;

public class TreeSideBar extends PressableWidget {
    public TreeNode connectedButton;
    public TreeMenu container;
    public String title;
    public String desc;
    private int[] code;
    public String[] requirements;
    public static final Identifier TEXTURE = new Identifier(MagicMod.ModID,"textures/gui/widgets.png");
    public TreeSideBar(String title, String desc, int[] code, String[] requirements) {
        super(0, 0, 16, 32, Text.literal(""));
        this.title = title;
        this.desc = desc;
        this.code = code;
        this.requirements = requirements;
    }
    public void init(TreeMenu container) {
        this.container = container;
        width = container.width/6;
        height = container.height/10;
        setX((container.width/6*5)-width/2);
        setY(container.height/4*3);
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        matrices.push();
        matrices.translate(0,0,500);
        drawBackground(matrices,container.width/3*2,0,container.width,container.height,new Color(32, 32, 32, 204));
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        int hovered = this.isHovered() ? 1 : 0;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        DrawableHelper.drawTexture(matrices, this.getX(), this.getY(), 0, hovered * 60, this.width / 2, this.height/2); //top left
        DrawableHelper.drawTexture(matrices, this.getX() + this.width / 2, this.getY(), 60 - this.width / 2, hovered * 60, this.width / 2, this.height/2); //top right
        DrawableHelper.drawTexture(matrices, this.getX(), this.getY()+this.height/2, 0, 60 - this.height / 2 + hovered * 60, this.width / 2, this.height/2); //bottom left
        DrawableHelper.drawTexture(matrices, this.getX() + this.width / 2, this.getY() + this.height/2, 60 - this.width / 2, 60 - this.height / 2 + hovered * 60, this.width / 2, this.height/2); //bottom right

        int j = 0xFFFFFF;
        int scale = 2;
        Text[] wrappedTitle = wrapText(title,container.width/3,scale);
        for(int x = 0; x < wrappedTitle.length; x++) {
            Text text = wrappedTitle[x];
            renderScaledText(textRenderer,matrices,text,container.width/6f*5-textRenderer.getWidth(text)/2f,(container.height/16f)+ textRenderer.fontHeight*scale*x,scale,j);
        }
        Text[] wrappedDesc = wrapText(desc,container.width/3,scale/2);
        for(int x = 0; x < wrappedDesc.length; x++) {
            Text text = wrappedDesc[x];
            renderScaledText(textRenderer,matrices,text,container.width/6f*5-textRenderer.getWidth(text)/2f,(container.height/16f)+ textRenderer.fontHeight*(scale/2f)*x + textRenderer.fontHeight*scale*wrappedTitle.length,scale/2f,j);
        }
        int color = requirementsMet() ? 0xFFFFFF : 0xA0A0A0;
        drawCenteredTextWithShadow(matrices, textRenderer, Text.literal("Unlock"), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color);
        renderCode(code,this.getX() + this.width /2,this.getY(), matrices);
        renderRequirements(requirements,this.getX() + this.width/2,(container.height/16)+ textRenderer.fontHeight*scale*wrappedDesc.length + textRenderer.fontHeight*scale*wrappedTitle.length,matrices, textRenderer);
        matrices.pop();
    }
    private void drawBackground(MatrixStack matrices, int x1, int y1, int x2, int y2, Color colour) {
        int red = colour.getRed();
        int green = colour.getGreen();
        int blue = colour.getBlue();
        int alpha = colour.getAlpha();
        //matrices.push();
        int col = (alpha << 24) | (red << 16) | (green << 8) | blue;
        //matrices.translate(0,0,150);
        DrawableHelper.fill(matrices,x1,y1,x2,y2,col);
        //matrices.pop();
    }
    private void renderCode(int[] code, int x, int y, MatrixStack matrices) {
        y -= 20;
        x -= 16*code.length/2;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        for(int i = 0; i < code.length; i++) {
            RenderSystem.setShaderTexture(0,RUNES[code[i]]);
            DrawableHelper.drawTexture(matrices,x+(i*16),y,0,0,16,16,16,16);
        }
    }
    public void connect(TreeNode btn) {
        this.connectedButton = btn;
        btn.connectedMenu = this;
    }
    private void renderRequirements(String[] requirements, int x, int y, MatrixStack matrices, TextRenderer textRenderer) {

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        Inventory inventory = MinecraftClient.getInstance().player.getInventory();
        //itemRenderer.zOffset = 100f;

        x-=25;

        for(int i = 0; i < requirements.length; i+=2) {
            Identifier id = new Identifier(requirements[i]);
            ItemStack itemStack = Registries.ITEM.get(id).getDefaultStack();

            itemRenderer.renderGuiItemIcon(matrices, itemStack, x, y);
            int amountReq = Integer.parseInt(requirements[i+1]);
            int amountOwned = inventory.count(itemStack.getItem());
            int color = amountOwned >= amountReq ? 0xFFFFFF : 0xA0A0A0;

            String amountDisplay = "(" + amountOwned + "/" + amountReq + ")";
            textRenderer.draw(matrices,amountDisplay,x+17,y+4,color);
            y+= 16;
        }
        //itemRenderer.zOffset = 0.0f;
    }

    private boolean requirementsMet() {
        if(!connectedButton.requirementsMet) {
            return false;
        }
        Inventory inventory = MinecraftClient.getInstance().player.getInventory();
        for(int i = 0; i < requirements.length; i+= 2) {
            Identifier id = new Identifier(requirements[i]);
            int amountReq = Integer.parseInt(requirements[i+1]);
            int amountOwned = inventory.count(Registries.ITEM.get(id).asItem());

            if(amountOwned < amountReq) {
                return false;
            }
        }
        return true;
    }
    private void removeItems() {
        for(int x = 0; x < requirements.length; x+= 2) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(requirements[x]);
            buf.writeInt(Integer.parseInt(requirements[x+1]));
            ClientPlayNetworking.send(PacketManager.REMOVE_ITEMS, buf);
        }
    }
    public Text[] wrapText(String text, int width, int textScale) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String widthAsText = "a";
        while(textRenderer.getWidth(widthAsText) < width/textScale) {
            widthAsText = widthAsText.concat("a");
        }
        if(textRenderer.getWidth(text) > textRenderer.getWidth(widthAsText)) {
            int lines = (int) Math.ceil(((float) text.length()) / ((float) widthAsText.length()));
            Text[] texts = new Text[lines];
            for(int y = 0; y < lines; y++) {
                StringBuilder builder = new StringBuilder();
                char[] chars = text.toCharArray();
                for (int x = 0; x < widthAsText.length(); x++) {
                    if(x + (y*widthAsText.length()) < chars.length) {
                        builder.append(chars[x + (y * widthAsText.length())]);
                    } else {
                        break;
                    }
                }
                texts[y] = Text.literal(builder.toString());
            }
            return texts;
        } else {
            return new Text[]{Text.literal(text)};
        }
    }
    public void renderScaledText(TextRenderer renderer, MatrixStack matrices, Text text, float x, float y, float scale, int color) {
        matrices.push();
        int width = renderer.getWidth(text);
        int height = renderer.fontHeight;

        matrices.translate((x + (width / 2f)), (y + (height / 2f)), 0);
        matrices.scale(scale, scale, 0);
        matrices.translate(-(x + (width / 2f)), -(y + (height / 2f)), 0);

        renderer.draw(matrices,text,x,y,color);
        matrices.pop();
    }
    @Override
    public void onPress() {
        if(!connectedButton.isUnlocked && connectedButton.requirementsMet) {
            if(requirementsMet()) {
                connectedButton.isUnlocked = true;
                removeItems();
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeString(this.title);
                ClientPlayNetworking.send(PacketManager.ADD_SPELL,buf);
            }
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
