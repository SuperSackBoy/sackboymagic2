package com.supersackboy.util;

import com.supersackboy.MagicMod;
import com.supersackboy.commands.ReloadTreeCommand;
import com.supersackboy.commands.SpellCommand;
import com.supersackboy.entity.projectile.CustomFireballEntity;
import com.supersackboy.entity.projectile.CustomFireballEntityRenderer;
import com.supersackboy.event.KeyInputHandler;
import com.supersackboy.event.PlayerEventCopyFrom;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.client.EntityModelLayersMixin;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class registries {
    public static void register() {
        registerCommands(); //register server commands
        registerEvents(); //register events
        registerProjectiles();
    }

    public static void registerClient() {
        registerClientCommands(); //register clientside commands
        KeyInputHandler.register(); //register key binds
        registerEntityRenderer();
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

    public static EntityType<CustomFireballEntity> CustomFireBallEntityType;
    private static void registerProjectiles() {
         CustomFireBallEntityType = Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier(MagicMod.ModID, "magic_fireball"),
                FabricEntityTypeBuilder.<CustomFireballEntity>create(SpawnGroup.MISC, CustomFireballEntity::new)
                        .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                        .trackRangeBlocks(4).trackedUpdateRate(10)
                        .build()
        );
    }

    private static void registerEntityRenderer() {
        EntityRendererRegistry.register(CustomFireBallEntityType, CustomFireballEntityRenderer::new);

    }



    private static void registerEvents() {
        ServerPlayerEvents.COPY_FROM.register(new PlayerEventCopyFrom()); //register the event to save the players spells on death
    }
}
