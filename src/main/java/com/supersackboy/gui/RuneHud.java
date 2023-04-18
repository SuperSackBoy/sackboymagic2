package com.supersackboy.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import com.supersackboy.MagicMod;
import com.supersackboy.playerdata.IEntityDataSaver;

public class RuneHud implements HudRenderCallback {
    //get rune textures
    private static final Identifier NONE = new Identifier(MagicMod.ModID, "textures/runes/blank.png");
    private static final Identifier RUNE1 = new Identifier(MagicMod.ModID,"textures/runes/rune_one.png");
    private static final Identifier RUNE2 = new Identifier(MagicMod.ModID,"textures/runes/rune_two.png");
    private static final Identifier RUNE3 = new Identifier(MagicMod.ModID,"textures/runes/rune_three.png");
    private static final Identifier RUNE4 = new Identifier(MagicMod.ModID,"textures/runes/rune_four.png");
    private static final Identifier RUNE5 = new Identifier(MagicMod.ModID,"textures/runes/rune_five.png");
    private static final Identifier RUNE6 = new Identifier(MagicMod.ModID,"textures/runes/rune_six.png");
    //array of rune textures because im a lazy bitch
    public static Identifier[] RUNES = {NONE,RUNE1,RUNE2,RUNE3,RUNE4,RUNE5,RUNE6};

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) { //make sure we dont run this on the server (even though this code is never executed on the server and im fucking insane)
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width;
            y = height;
        }
        int[] playerRunes = {0,0,0,0,0,0}; //create the array
        //get the players data and put it into the array
        if(((IEntityDataSaver) MinecraftClient.getInstance().player).getRuneData().getIntArray("runes").length != 0) {
            playerRunes = ((IEntityDataSaver) MinecraftClient.getInstance().player).getRuneData().getIntArray("runes");
        }

        int xOffset = 32;
        int yOffset = 32;
        int xInterval = 16;
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        //RenderSystem.setShaderTexture(0,RUNE1);
        //actually draw the fucking gui
        for(int i = 2; i > -1; i--) {
            RenderSystem.setShaderTexture(0,RUNES[playerRunes[i+3]]);
            DrawableHelper.drawTexture(matrixStack,x-xOffset,y-yOffset,0,0,16,16,16,16);

            RenderSystem.setShaderTexture(0,RUNES[playerRunes[i]]);
            DrawableHelper.drawTexture(matrixStack,x-xOffset-(xInterval/2),y-yOffset-xInterval,0,0,16,16,16,16);

            x-=xInterval;
        }
    }
}
