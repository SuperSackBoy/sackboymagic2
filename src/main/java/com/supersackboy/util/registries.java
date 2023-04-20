package com.supersackboy.util;

import com.supersackboy.commands.ReloadTreeCommand;
import com.supersackboy.commands.SpellCommand;
import com.supersackboy.event.PlayerEventCopyFrom;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class registries {
    public static void register() {
        registerCommands();
        registerEvents();
    }

    public static void registerClient() {
        registerClientCommands();
    }
    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            SpellCommand.register(dispatcher);
        }));
    }

    private static void registerClientCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ReloadTreeCommand.register(dispatcher);
        });
    }

    private static void registerEvents() {
        ServerPlayerEvents.COPY_FROM.register(new PlayerEventCopyFrom());
    }
}
