package com.supersackboy.event;

import com.supersackboy.gui.RuneHud;
import com.supersackboy.gui.techtree.TechTreeHandler;
import com.supersackboy.gui.techtree.TreeNode;
import com.supersackboy.networking.PacketManager;
import com.supersackboy.playerdata.IEntityDataSaver;
import com.supersackboy.spells.Spell;
import com.supersackboy.spells.SpellManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

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
            if(castKey.wasPressed()) { //if no runes are being cast, open the tech tree, other wise, cast the spell
                if(((IEntityDataSaver) client.player).getRuneData().getIntArray("runes").length == 0) {
                    TechTreeHandler.openMenu(client);
                } else if (((IEntityDataSaver) client.player).getRuneData().getIntArray("runes")[0] != 0) {
                    ClientPlayNetworking.send(PacketManager.CAST, PacketByteBufs.create());
                } else {
                    TechTreeHandler.openMenu(client);
                }
            }
            if(rune1Key.wasPressed()) { //self explanatory code
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
    public static boolean validSpell;
    static Timer timer;

    public static void cast(int rune) {
        //get the players cast runes and add the rune being cast to it
        int[] playerRunes = ((IEntityDataSaver) MinecraftClient.getInstance().player).getRuneData().getIntArray("runes");
        if(playerRunes.length != 0 && RuneHud.blinking == 0) {
            for(int x=0; x<6;x++) {
                if (playerRunes[x] == 0) {
                    playerRunes[x] = rune;
                    break;
                }
            }
        } else { //if the spell is being timed out or the player has no data saved just use the one rune
            playerRunes = new int[]{rune,0,0,0,0,0};
        }
        validSpell = false;
        outer: //if the rune code matches a known spell set valid spell to true, otherwise keep it false
        for(TreeNode btn : TechTreeHandler.buttons) {
            if(btn.isUnlocked) for(Spell spell : SpellManager.spells) {
                if(spell.id.equals(btn.id)) {
                    if(Arrays.equals(spell.code, playerRunes)) {
                        validSpell = true;
                        break outer;
                    }
                }
            }
        }

        if(timer == null) { //if the timer hasn't been created, create it
            timer = new Timer();
        } else { //if the timer exists
            if(RuneHud.blinking != 0) { //if the runes are being timed out
                RuneHud.blinking = 0; //stop the time-out and clear the runes
                ClientPlayNetworking.send(PacketManager.CAST, PacketByteBufs.create());
            }
            timer.cancel(); //cancel the timer
            timer = new Timer(); //create a new timer
        }
        timer.schedule(new TimerTask() { //schedule a timer for 1s
            @Override
            public void run() { //after 1s start blinking the runes if it's not valid
                if (!validSpell) {
                    RuneHud.blinking++;
                    timer.schedule(new TimerTask() { //set a new timer for 2.45 seconds to stop the blinking
                        @Override
                        public void run() {
                            RuneHud.blinking = 0;
                            ClientPlayNetworking.send(PacketManager.CAST, PacketByteBufs.create());
                        }
                    }, 2450);
                }
            }
        }, 1000);

        PacketByteBuf buf = PacketByteBufs.create(); //send the newly added rune to the server
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
