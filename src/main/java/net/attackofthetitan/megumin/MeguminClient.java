package net.attackofthetitan.megumin;

import net.fabricmc.api.ClientModInitializer;
import net.attackofthetitan.megumin.entity.ModEntities;
import net.attackofthetitan.megumin.particle.ModParticles;

public class MeguminClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntities.registerMoveCancel();
        ModEntities.registerRenderers();
        ModParticles.registerClient();
    }
}
