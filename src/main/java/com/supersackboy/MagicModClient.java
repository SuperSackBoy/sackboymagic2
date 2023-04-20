package com.supersackboy;

import com.supersackboy.event.KeyInputHandler;
import com.supersackboy.gui.RuneHud;
import com.supersackboy.networking.PacketManager;
import com.supersackboy.util.registries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;


public class MagicModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PacketManager.registerS2CPackets(); //register client packets

        HudRenderCallback.EVENT.register(new RuneHud()); //add the hud display to the HudRenderer

        registries.registerClient(); //register client shit
    }
}
