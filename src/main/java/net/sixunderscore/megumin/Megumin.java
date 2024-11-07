package net.sixunderscore.megumin;

import net.fabricmc.api.ModInitializer;

import net.sixunderscore.megumin.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Megumin implements ModInitializer {
	public static final String MOD_ID = "megumin";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.load();
	}
}