package com.supersackboy.networking.packets;

import com.supersackboy.playerdata.RuneData;
import com.supersackboy.playerdata.IEntityDataSaver;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class CastRuneC2S { //adds a rune to the players NBT data
    @SuppressWarnings("unused")
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int rune = buf.readInt();
        RuneData.addRune(((IEntityDataSaver) player),rune);
    }
}
