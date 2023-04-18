package com.supersackboy.networking;

import com.supersackboy.MagicMod;
import com.supersackboy.networking.packets.CastC2S;
import com.supersackboy.networking.packets.CastRuneC2S;
import com.supersackboy.networking.packets.SyncRuneS2C;
import com.supersackboy.networking.packets.SyncSpellS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class PacketManager {

    public static Identifier CAST_RUNE = new Identifier(MagicMod.ModID, "cast_rune");
    public static Identifier CAST_SYNC = new Identifier(MagicMod.ModID, "cast_sync");
    public static Identifier CAST = new Identifier(MagicMod.ModID,"cast");
    public static Identifier SPELL_SYNC = new Identifier(MagicMod.ModID,"spell_sync");

    public static void registerC2SPackets()  { //client to server
        ServerPlayNetworking.registerGlobalReceiver(CAST_RUNE, CastRuneC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(CAST, CastC2S::receive);
    }
    public static void registerS2CPackets() { //server to client
        ClientPlayNetworking.registerGlobalReceiver(CAST_SYNC, SyncRuneS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(SPELL_SYNC, SyncSpellS2C::receive);
    }
}
