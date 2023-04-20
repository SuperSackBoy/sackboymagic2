package com.supersackboy.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.supersackboy.networking.PacketManager;
import com.supersackboy.playerdata.IEntityDataSaver;
import com.supersackboy.playerdata.SpellData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SpellCommand { //command to add and remove spells from players
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        //spell add 'player' 'spell'
        //spell add 'spell' //defaults to self
        dispatcher.register(CommandManager.literal("spell").requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("spell", StringArgumentType.string())
                                .executes(context -> add(context, context.getSource().getPlayer(),StringArgumentType.getString(context,"spell"))))
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .then(CommandManager.argument("spell", StringArgumentType.string())
                                        .executes(context -> add(context, EntityArgumentType.getPlayer(context,"target"),StringArgumentType.getString(context, "spell")))))));
        //spell remove 'player' 'spell'
        //spell remove 'spell' //defaults to self
        dispatcher.register(CommandManager.literal("spell").requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("spell", StringArgumentType.string())
                                .executes(context -> remove(context, context.getSource().getPlayer(),StringArgumentType.getString(context,"spell"))))
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .then(CommandManager.argument("spell", StringArgumentType.string())
                                        .executes(context -> remove(context, EntityArgumentType.getPlayer(context,"target"),StringArgumentType.getString(context, "spell")))))));
        //spell removeall 'player'
        //spell removeall //defaults to self
        dispatcher.register(CommandManager.literal("spell").requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.literal("removeall")
                    .executes(context -> removeall(context, context.getSource().getPlayer()))
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> removeall(context, EntityArgumentType.getPlayer(context,"target"))))));
    }

    private static int add(CommandContext<ServerCommandSource> context, ServerPlayerEntity target, String spell) {
        int valid = SpellData.addSpell((IEntityDataSaver) target,spell);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(((IEntityDataSaver) target).getSpellData());
        ServerPlayNetworking.send(target, PacketManager.RELOAD_TREE, buf);
        if(context.getSource().isExecutedByPlayer()) {
            if (valid == 1) {
                context.getSource().getPlayer().sendMessage(Text.literal("Successfully added spell").formatted(Formatting.GREEN));
            } else {
                context.getSource().getPlayer().sendMessage(Text.literal("Error").formatted(Formatting.RED));
            }
        }
        return valid;
    }

    private static int remove(CommandContext<ServerCommandSource> context, ServerPlayerEntity target, String spell) {
        int valid = SpellData.removeSpell((IEntityDataSaver) target,spell);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(((IEntityDataSaver) target).getSpellData());
        ServerPlayNetworking.send(target, PacketManager.RELOAD_TREE, buf);
        if(context.getSource().isExecutedByPlayer()) {
            if (valid == 1) {
                context.getSource().getPlayer().sendMessage(Text.literal("Successfully removed spell").formatted(Formatting.GREEN));
            } else {
                context.getSource().getPlayer().sendMessage(Text.literal("Error").formatted(Formatting.RED));
            }
        }
        return valid;
    }

    private static int removeall(CommandContext<ServerCommandSource> context, ServerPlayerEntity target) {
        int valid = SpellData.reset((IEntityDataSaver) target);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(((IEntityDataSaver) target).getSpellData());
        ServerPlayNetworking.send(target, PacketManager.RELOAD_TREE, buf);
        if(context.getSource().isExecutedByPlayer()) {
            if (valid == 1) {
                context.getSource().getPlayer().sendMessage(Text.literal("Successfully removed all spells").formatted(Formatting.GREEN));
            } else {
                context.getSource().getPlayer().sendMessage(Text.literal("Error").formatted(Formatting.RED));
            }
        }
        return valid;
    }
}