package com.supersackboy.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RemoveItemsC2S {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier id = new Identifier(buf.readString());
        ItemStack item = Registries.ITEM.get(id).getDefaultStack();
        int take = buf.readInt();
        Inventory inventory = player.getInventory();

        for(int i = 0; i <= 35; i++) { //loop through inventory
            ItemStack itemCheck = inventory.getStack(i); //get item in slot
            if(itemCheck.getItem() == item.getItem()) { //if item in slot is the required item
                int count = itemCheck.getCount(); //get the amount of items in that stack
                if (count >= take) { //if its more than the amount required
                    inventory.removeStack(i, take); //remove the desired amount
                    take = -1;
                } else { //if its less than the desired amount
                    inventory.removeStack(i);  //remove it
                    take -= count; //take its amount from the take var
                }
                if(take <= 0) { //if weve taken enough
                    i = 99; //break
                }
            }
        }

    }
}
