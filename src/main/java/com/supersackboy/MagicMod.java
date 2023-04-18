package com.supersackboy;

import com.supersackboy.networking.PacketManager;
import com.supersackboy.util.registries;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagicMod implements ModInitializer {
	public static final String ModID = "sackboymagic";
	public static final Logger LOGGER = LoggerFactory.getLogger(ModID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		PacketManager.registerC2SPackets();
		registries.register();
	}
}