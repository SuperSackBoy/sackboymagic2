package com.supersackboy.util;

import com.supersackboy.commands.ReloadTreeCommand;
import com.supersackboy.commands.SpellCommand;
import com.supersackboy.event.KeyInputHandler;
import com.supersackboy.event.PlayerEventCopyFrom;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class registries {
    public static void register() {
        registerCommands(); //register server commands
        registerEvents(); //register events
    }

    public static void registerClient() {
        registerClientCommands(); //register clientside commands
        KeyInputHandler.register(); //register key binds
    }
    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            SpellCommand.register(dispatcher); //register the spell command
        }));
    }

    private static void registerClientCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ReloadTreeCommand.register(dispatcher); //register the tech tree reload command
        });
    }

    private static void registerEvents() {
        ServerPlayerEvents.COPY_FROM.register(new PlayerEventCopyFrom()); //register the event to save the players spells on death
    }
}
