package net.sixunderscore.megumin;

import net.fabricmc.api.ClientModInitializer;
import net.sixunderscore.megumin.entity.ModEntities;
import net.sixunderscore.megumin.particle.ModParticles;

public class MeguminClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntities.registerRenderers();
        ModParticles.registerClient();
    }
}
