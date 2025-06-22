package net.attackofthetitan.megumin.entity;

import net.attackofthetitan.megumin.entity.custom.ExplosionBlastEntity;
import net.attackofthetitan.megumin.entity.custom.ExplosionManagerEntity;
import net.attackofthetitan.megumin.entity.custom.ExplosionRayEntity;
import net.attackofthetitan.megumin.entity.custom.ExplosionRingEntity;
import net.attackofthetitan.megumin.entity.renderer.ExplosionBlastEntityRenderer;
import net.attackofthetitan.megumin.entity.renderer.ExplosionManagerEntityRenderer;
import net.attackofthetitan.megumin.entity.renderer.ExplosionRayEntityRenderer;
import net.attackofthetitan.megumin.entity.renderer.ExplosionRingEntityRenderer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.attackofthetitan.megumin.Megumin;

public class ModEntities {

    private static boolean freezePlayer = false;

    public static final EntityType<ExplosionRingEntity> EXPLOSION_RING = register("explosion_ring", ExplosionRingEntity::new);

    public static final EntityType<ExplosionRayEntity> EXPLOSION_RAY = register("explosion_ray", ExplosionRayEntity::new);

    public static final EntityType<ExplosionBlastEntity> EXPLOSION_BLAST = register("explosion_blast", ExplosionBlastEntity::new);

    public static final EntityType<ExplosionManagerEntity> EXPLOSION_MANAGER = register("explosion_manager", ExplosionManagerEntity::new);

    private static <T extends Entity> EntityType<T> register(String id, EntityType.EntityFactory<T> factory){
        return Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(Megumin.MOD_ID, id),
                EntityType.Builder.create(factory, SpawnGroup.MISC)
                        .dropsNothing()
                        .dimensions(0.5F, 0.5F)
                        .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Megumin.MOD_ID, id)))
        );
    }

    public static void registerModEntities() {
        Megumin.LOGGER.info("Registering ModEntities for: " + Megumin.MOD_ID);
    }

    public static void registerRenderers() {
        Megumin.LOGGER.info("Registering renderers for: " + Megumin.MOD_ID);
        EntityRendererRegistry.register(EXPLOSION_RING, ExplosionRingEntityRenderer::new);
        EntityRendererRegistry.register(EXPLOSION_RAY, ExplosionRayEntityRenderer::new);
        EntityRendererRegistry.register(EXPLOSION_BLAST, ExplosionBlastEntityRenderer::new);
        EntityRendererRegistry.register(EXPLOSION_MANAGER, ExplosionManagerEntityRenderer::new);
    }

    private static void disableMovementInput(MinecraftClient client) {
        // Prevent all movement inputs
        KeyBinding[] movementKeys = {
                client.options.forwardKey,
                client.options.backKey,
                client.options.leftKey,
                client.options.rightKey,
                client.options.jumpKey,
                client.options.sprintKey,
                client.options.sneakKey
        };

        if(client.player != null) {
            for (KeyBinding key : movementKeys) {
                key.setPressed(false);
            }
        }
    }

    public static void registerMoveCancel() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null && freezePlayer) {
                disableMovementInput(client);
            }
        });
    }

    public static void setFreezePlayer(boolean currentState) {
        freezePlayer = !currentState;
    }
}
