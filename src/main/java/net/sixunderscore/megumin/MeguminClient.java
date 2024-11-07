package net.sixunderscore.megumin;

import net.fabricmc.api.ClientModInitializer;
import net.sixunderscore.megumin.entity.ModEntities;

public class MeguminClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEntities.registerRenderers();
    }
}
