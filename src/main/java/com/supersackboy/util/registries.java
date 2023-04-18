package com.supersackboy.util;

import com.supersackboy.commands.SpellCommand;
import com.supersackboy.event.PlayerEventCopyFrom;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class registries {
    public static void register() {
        registerCommands();
        registerEvents();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            SpellCommand.register(dispatcher);
        }));
    }

    private static void registerEvents() {
        ServerPlayerEvents.COPY_FROM.register(new PlayerEventCopyFrom());
    }
}
