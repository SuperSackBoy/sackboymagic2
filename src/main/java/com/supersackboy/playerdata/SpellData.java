package com.supersackboy.playerdata;

import com.supersackboy.networking.PacketManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpellData {
    public static int addSpell(IEntityDataSaver player, String spell) {
        NbtCompound nbt = player.getSpellData();

        if(!nbt.contains(spell)) {
            if (!nbt.contains("spellsKnown")) {
                int spellsKnown = 0;
                nbt.putInt("spellsKnown", spellsKnown);
            }
            int spellsKnown = nbt.getInt("spellsKnown");
            spellsKnown++;
            nbt.putInt("spellsKnown", spellsKnown);

            nbt.putString(spell, "true");

            syncSpells(true, ((ServerPlayerEntity) player), spell, spellsKnown);
            return 1;
        }

        return 0;
    }

    public static int removeSpell(IEntityDataSaver player, String spell) {
        NbtCompound nbt = player.getSpellData();
        if(nbt.contains(spell)) {
            if (!nbt.contains("spellsKnown")) {
                int spellsKnown = 0;
                nbt.putInt("spellsKnown", spellsKnown);
            }
            int spellsKnown = nbt.getInt("spellsKnown");
            spellsKnown--;
            nbt.putInt("spellsKnown", spellsKnown);

            nbt.remove(spell);

            syncSpells(false, ((ServerPlayerEntity) player), spell, spellsKnown);
            return 1;
        }
        return 0;
    }

    public static int reset(IEntityDataSaver player) {
        return player.resetSpellData();
    }
    public static void syncSpells(boolean addRemove, ServerPlayerEntity player, String spell, int spellsKnown) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(spellsKnown);
        buffer.writeString(spell);
        buffer.writeBoolean(addRemove);

        ServerPlayNetworking.send(player, PacketManager.SPELL_SYNC,buffer);
    }
}