package com.supersackboy.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.supersackboy.networking.PacketManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class ReloadTreeCommand { //command to refresh the tech tree
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("reloadspelltree")
                        .executes(context -> execute()));
    }
    public static int execute() {
        ClientPlayNetworking.send(PacketManager.REQUEST_RELOAD_TREE, PacketByteBufs.create());
        return 1;
    }
}
