package com.supersackboy;

import com.supersackboy.event.KeyInputHandler;
import com.supersackboy.gui.RuneHud;
import com.supersackboy.gui.techtree.TechTreeHandler;
import com.supersackboy.networking.PacketManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MagicModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        PacketManager.registerS2CPackets();
        TechTreeHandler.init();

        HudRenderCallback.EVENT.register(new RuneHud());
    }
}
