package com.supersackboy.playerdata;

import com.supersackboy.networking.PacketManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class RuneData {
    public static void addRune(IEntityDataSaver player, int rune) {
        NbtCompound nbt = player.getRuneData();
        if(nbt.getIntArray("runes").length==0) {
            int[] arr = {0,0,0,0,0,0};
            nbt.putIntArray("runes",arr);
        }
        int[] runes = nbt.getIntArray("runes");
        for(int i = 0; i < runes.length; i++) {
            if(runes[i] == 0) {
                runes[i] = rune;
                break;
            }
        }
        nbt.putIntArray("runes", runes);
        syncRunes(runes, ((ServerPlayerEntity) player));
    }
    public static void resetRunes(IEntityDataSaver player) {
        NbtCompound nbt = player.getRuneData();
        int[] runes = {0,0,0,0,0,0};
        nbt.putIntArray("runes",runes);
        syncRunes(runes, ((ServerPlayerEntity) player));
    }
    public static void syncRunes(int[] runes, ServerPlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeIntArray(runes);
        ServerPlayNetworking.send(player, PacketManager.CAST_SYNC,buffer);
    }
}
