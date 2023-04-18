package com.supersackboy.networking.packets;

import com.supersackboy.playerdata.IEntityDataSaver;
import com.supersackboy.playerdata.RuneData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class CastC2S {
    @SuppressWarnings("unused")
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        cast(((IEntityDataSaver) player).getRuneData().getIntArray("runes"),player);
        RuneData.resetRunes(((IEntityDataSaver) player));
    }
    private static void cast(int[] runes, ServerPlayerEntity player) {

    }
}
