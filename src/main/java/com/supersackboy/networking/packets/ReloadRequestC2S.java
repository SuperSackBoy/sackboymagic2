package com.supersackboy.networking.packets;

import com.supersackboy.networking.PacketManager;
import com.supersackboy.playerdata.IEntityDataSaver;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ReloadRequestC2S {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeNbt(((IEntityDataSaver) player).getSpellData());

        ServerPlayNetworking.send(player, PacketManager.RELOAD_TREE, buffer);
    }
}
