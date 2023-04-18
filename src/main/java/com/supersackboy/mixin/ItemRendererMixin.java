package com.supersackboy.mixin;

import com.supersackboy.util.IItemRendererMixin;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin implements IItemRendererMixin {
    @Shadow @Final private TextureManager textureManager;

    @Override
    public TextureManager getTextureManager() {
        return this.textureManager;
    }
}

