package com.supersackboy.gui.techtree;

import com.supersackboy.spells.Spell;
import com.supersackboy.spells.SpellManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;

import static net.minecraft.item.Items.*;

public class TechTreeHandler {
    static TreeMenu TechTree;
    public static TreeNode[] buttons;

    public static void init(NbtCompound nbt) {
        buttons = new TreeNode[]{
                new TreeNode("id",null, OAK_LOG.getDefaultStack()),
                new TreeNode("balls",new String[]{"id"},OAK_PLANKS.getDefaultStack()),
                new TreeNode("cock",new String[]{"id"},OAK_SAPLING.getDefaultStack()),
                new TreeNode("dick",new String[]{"cock", "balls"},SPRUCE_PLANKS.getDefaultStack()),
                new TreeNode("abcd", new String[]{"balls"}, SPRUCE_LOG.getDefaultStack()),
                new TreeNode("fireball", new String[]{"id"}, FLINT_AND_STEEL.getDefaultStack())

        };

        TreeSideBar[] menus = new TreeSideBar[]{
                new TreeSideBar("id","does nothing",new int[]{2,2,2},new String[]{"minecraft:diamond","2","minecraft:dirt","64"}),
                new TreeSideBar("balls","does nothing",new int[]{3,2,3},new String[]{"minecraft:dirt","2"}),
                new TreeSideBar("cock","does nothing",new int[]{6,6,6},new String[]{"minecraft:dirt","2"}),
                new TreeSideBar("dick","does nothing",new int[]{4,2,2},new String[]{"minecraft:dirt","2"}),
                new TreeSideBar("abcd","does nothing",new int[]{2,3,2},new String[]{"minecraft:dirt","2"}),
                new TreeSideBar("fireball","Spawns a fireball",new int[]{1},new String[]{"minecraft:blaze_rod","1"})

        };

        for(TreeSideBar menu : menus) {
            for(Spell spell : SpellManager.spells) {
                if(spell.id.equals(menu.title)) {
                    spell.connect(menu);
                }
            }
        }

        if(nbt != null) {
            for(TreeNode btn : buttons) {
                if(nbt.contains(btn.id)) {
                    btn.isUnlocked = true;
                }
            }
        }


        TechTree = new TreeMenu(buttons,menus);
    }

    public static void openMenu(MinecraftClient client) {
        if(TechTree != null) {
            client.setScreen(TechTree);
        }
    }
}
