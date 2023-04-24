package com.supersackboy.networking.packets;

import com.supersackboy.MagicMod;
import com.supersackboy.anim.IMagicModAnim;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class AnimS2C {
    @SuppressWarnings("unused")
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        //get animation id from buffer
        String animId = buf.readString();
        //get uuid from buffer
        UUID uuid = buf.readUuid();
        //get player from uuid
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) MinecraftClient.getInstance().world.getPlayerByUuid(uuid);

        //play animation
        var animationContainer = ((IMagicModAnim) player).magicMod_getModAnimation();
        KeyframeAnimation anim = PlayerAnimationRegistry.getAnimation(new Identifier(MagicMod.ModID,animId));
        animationContainer.setAnimation(new KeyframeAnimationPlayer(anim));
    }
}