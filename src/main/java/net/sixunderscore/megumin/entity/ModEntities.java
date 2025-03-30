package net.sixunderscore.megumin.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.sixunderscore.megumin.Megumin;
import net.sixunderscore.megumin.entity.custom.*;
import net.sixunderscore.megumin.entity.renderer.*;

public class ModEntities {
    public static final EntityType<ExplosionRingEntity> EXPLOSION_RING = register("explosion_ring", EntityType.Builder.create(ExplosionRingEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.5F, 0.5F));
    public static final EntityType<ExplosionRayEntity> EXPLOSION_RAY = register("explosion_ray", EntityType.Builder.create(ExplosionRayEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.5F, 0.5F));
    public static final EntityType<ExplosionBlastEntity> EXPLOSION_BLAST = register("explosion_blast", EntityType.Builder.create(ExplosionBlastEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.5F, 0.5F));
    public static final EntityType<ExplosionManagerEntity> EXPLOSION_MANAGER = register("explosion_manager", EntityType.Builder.create(ExplosionManagerEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.5F, 0.5F));

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.ofVanilla(id)), type);
    }

    private static <T extends Entity> EntityType<T> register(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    public static void registerRenderers() {
        Megumin.LOGGER.info("Registering renderers for: " + Megumin.MOD_ID);
        EntityRendererRegistry.register(EXPLOSION_RING, ExplosionRingEntityRenderer::new);
        EntityRendererRegistry.register(EXPLOSION_RAY, ExplosionRayEntityRenderer::new);
        EntityRendererRegistry.register(EXPLOSION_BLAST, ExplosionBlastEntityRenderer::new);
        EntityRendererRegistry.register(EXPLOSION_MANAGER, ExplosionManagerEntityRenderer::new);
    }
}
