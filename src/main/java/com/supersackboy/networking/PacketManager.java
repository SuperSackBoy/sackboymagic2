package com.supersackboy.networking;

import com.supersackboy.MagicMod;
import com.supersackboy.networking.packets.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class PacketManager {

    public static Identifier CAST_RUNE = new Identifier(MagicMod.ModID, "cast_rune");
    public static Identifier CAST_SYNC = new Identifier(MagicMod.ModID, "cast_sync");
    public static Identifier CAST = new Identifier(MagicMod.ModID,"cast");
    public static Identifier SPELL_SYNC = new Identifier(MagicMod.ModID,"spell_sync");
    public static Identifier REMOVE_ITEMS = new Identifier(MagicMod.ModID, "remove_items");
    public static Identifier RELOAD_TREE = new Identifier(MagicMod.ModID, "reload_tree");
    public static Identifier ADD_SPELL = new Identifier(MagicMod.ModID, "add_spell");
    public static Identifier REQUEST_RELOAD_TREE = new Identifier(MagicMod.ModID, "request_reload_tree");



    public static void registerC2SPackets()  { //client to server
        ServerPlayNetworking.registerGlobalReceiver(CAST_RUNE, CastRuneC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(CAST, CastC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(REMOVE_ITEMS, RemoveItemsC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(ADD_SPELL, AddSpellC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_RELOAD_TREE, ReloadRequestC2S::receive);

    }
    public static void registerS2CPackets() { //server to client
        ClientPlayNetworking.registerGlobalReceiver(CAST_SYNC, SyncRuneS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(SPELL_SYNC, SyncSpellS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(RELOAD_TREE, ReloadTreeS2C::receive);
    }
}
