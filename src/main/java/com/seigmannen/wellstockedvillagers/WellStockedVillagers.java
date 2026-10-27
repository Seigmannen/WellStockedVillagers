package com.seigmannen.wellstockedvillagers;

import com.seigmannen.wellstockedvillagers.command.WsvCommand;
import com.seigmannen.wellstockedvillagers.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WellStockedVillagers implements ModInitializer {
	public static final String MOD_ID = "wellstockedvillagers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.load();
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> WsvCommand.register(dispatcher));
	}
}
