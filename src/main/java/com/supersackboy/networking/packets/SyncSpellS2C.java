package com.supersackboy.networking.packets;

import com.supersackboy.playerdata.IEntityDataSaver;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class SyncSpellS2C {
    @SuppressWarnings("unused")
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            int spellsKnown = buf.readInt();
            String spell = buf.readString();
            boolean addRemove = buf.readBoolean();

            ((IEntityDataSaver) client.player).getSpellData().putInt("spellsKnown", spellsKnown);
            if(addRemove) {
                ((IEntityDataSaver) client.player).getSpellData().putString(spell, "true");
            } else {
                ((IEntityDataSaver) client.player).getSpellData().remove(spell);
            }
        }
    }
}