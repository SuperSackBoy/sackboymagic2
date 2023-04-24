package com.supersackboy.networking.packets;

import com.supersackboy.networking.PacketManager;
import com.supersackboy.playerdata.IEntityDataSaver;
import com.supersackboy.playerdata.RuneData;
import com.supersackboy.spells.Spell;
import com.supersackboy.spells.SpellManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

public class CastC2S {
    @SuppressWarnings("unused")
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        cast(((IEntityDataSaver) player).getRuneData().getIntArray("runes"),player);
        RuneData.resetRunes(((IEntityDataSaver) player));
    }
    private static void cast(int[] runes, ServerPlayerEntity player) {
        NbtCompound nbt = ((IEntityDataSaver) player).getSpellData();
        for(Spell spell : SpellManager.spells) {
            System.out.println(Arrays.toString(spell.code));

            if(nbt.contains(spell.id) && Arrays.equals(spell.code, runes)) {
                sendAnim(player,"waving");
                spell.onCast(player);
                break;
            }
        }
    }

    private static void sendAnim(ServerPlayerEntity player, String animId) {
        //get all players
        List<ServerPlayerEntity> playerList = ((ServerWorld) player.world).getPlayers();
        for(ServerPlayerEntity x : playerList){ //loop through players
            if(x.distanceTo(player) < 60) { //check if they are closer than 60 blocks
                PacketByteBuf buf = PacketByteBufs.create(); //play the animation
                buf.writeString(animId);
                buf.writeUuid(player.getUuid());
                ServerPlayNetworking.send(x, PacketManager.ANIM, buf);
            }
        }
    }
}
