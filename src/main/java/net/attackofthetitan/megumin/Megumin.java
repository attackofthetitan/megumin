package net.attackofthetitan.megumin;

import net.fabricmc.api.ModInitializer;

import net.attackofthetitan.megumin.entity.ModEntities;
import net.attackofthetitan.megumin.item.ModItems;
import net.attackofthetitan.megumin.particle.ModParticles;
import net.attackofthetitan.megumin.sound.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Megumin implements ModInitializer {
	public static final String MOD_ID = "megumin";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.load();
		ModEntities.registerModEntities();
		ModParticles.register();
		ModSounds.register();
	}
}
