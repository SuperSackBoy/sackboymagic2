package com.supersackboy.networking.packets;

import com.supersackboy.playerdata.IEntityDataSaver;
import com.supersackboy.playerdata.RuneData;
import com.supersackboy.spells.Spell;
import com.supersackboy.spells.SpellManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;

public class CastC2S {
    @SuppressWarnings("unused")
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        cast(((IEntityDataSaver) player).getRuneData().getIntArray("runes"),player);
        RuneData.resetRunes(((IEntityDataSaver) player));
    }
    private static void cast(int[] runes, ServerPlayerEntity player) {
        NbtCompound nbt = ((IEntityDataSaver) player).getSpellData();
        for(Spell spell : SpellManager.spells) {
            if(nbt.contains(spell.id) && Arrays.equals(spell.code, runes)) {
                spell.onCast();
                break;
            }
        }
    }
}
