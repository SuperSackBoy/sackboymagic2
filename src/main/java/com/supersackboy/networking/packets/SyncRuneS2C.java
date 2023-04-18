package com.supersackboy.networking.packets;

import com.supersackboy.playerdata.IEntityDataSaver;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class SyncRuneS2C {
    @SuppressWarnings("unused")
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if(client.player != null) {
            ((IEntityDataSaver) client.player).getRuneData().putIntArray("runes", buf.readIntArray());
        }
    }
}
