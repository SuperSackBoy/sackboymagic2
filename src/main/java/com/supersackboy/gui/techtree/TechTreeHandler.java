package com.supersackboy.gui.techtree;

import net.minecraft.client.MinecraftClient;


import static net.minecraft.item.Items.OAK_LOG;
import static net.minecraft.item.Items.OAK_PLANKS;

public class TechTreeHandler {
    static TreeMenu TechTree;
    public static void init() {
        TreeNode[] buttons = new TreeNode[]{
                new TreeNode("id",null, OAK_LOG.getDefaultStack()),
                new TreeNode("balls",new String[]{"id"},OAK_PLANKS.getDefaultStack())
        };


        TechTree = new TreeMenu(buttons);
    }

    public static void openMenu(MinecraftClient client) {
        client.setScreen(TechTree);
    }
}
