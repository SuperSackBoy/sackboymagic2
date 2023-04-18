package com.supersackboy.event;

import com.supersackboy.gui.techtree.TechTreeHandler;
import com.supersackboy.networking.PacketManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static KeyBinding castKey;
    public static KeyBinding rune1Key;
    public static KeyBinding rune2Key;
    public static KeyBinding rune3Key;
    public static KeyBinding rune4Key;
    public static KeyBinding rune5Key;
    public static KeyBinding rune6Key;

    private static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(castKey.wasPressed()) {
                //ClientPlayNetworking.send(PacketManager.CAST, PacketByteBufs.create());
                TechTreeHandler.openMenu(client);
            }
            if(rune1Key.wasPressed()) {
                cast(1);
            }
            if(rune2Key.wasPressed()) {
                cast(2);
            }
            if(rune3Key.wasPressed()) {
                cast(3);
            }
            if(rune4Key.wasPressed()) {
                cast(4);
            }
            if(rune5Key.wasPressed()) {
                cast(5);
            }
            if(rune6Key.wasPressed()) {
                cast(6);
            }
        });
    }
    public static void cast(int rune) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(rune);
        ClientPlayNetworking.send(PacketManager.CAST_RUNE,buf);
    }

    public static void register() {
        castKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.magicmod.cast",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.category.magicmod.magicmodbinds"
        ));
        rune1Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.magicmod.rune1",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "key.category.magicmod.magicmodbinds"
        ));
        rune2Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.magicmod.rune2",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                "key.category.magicmod.magicmodbinds"
        ));
        rune3Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.magicmod.rune3",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "key.category.magicmod.magicmodbinds"
        ));
        rune4Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.magicmod.rune4",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "key.category.magicmod.magicmodbinds"
        ));
        rune5Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.magicmod.rune5",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "key.category.magicmod.magicmodbinds"
        ));
        rune6Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.magicmod.rune6",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.category.magicmod.magicmodbinds"
        ));

        registerKeyInputs();
    }
}
