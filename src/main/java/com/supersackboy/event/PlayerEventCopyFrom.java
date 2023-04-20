package com.supersackboy.event;

import com.supersackboy.playerdata.IEntityDataSaver;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEventCopyFrom implements ServerPlayerEvents.CopyFrom {
    //on death, copies over the players known spells
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        IEntityDataSaver original = ((IEntityDataSaver) oldPlayer);
        IEntityDataSaver player = ((IEntityDataSaver) newPlayer);

        player.getSpellData().copyFrom(original.getSpellData());
    }
}
