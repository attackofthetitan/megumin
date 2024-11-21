package net.sixunderscore.megumin.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.sixunderscore.megumin.Megumin;
import net.sixunderscore.megumin.particle.custom.StarParticle;

public class ModParticles {
    public static final SimpleParticleType STAR_PARTICLE = FabricParticleTypes.simple();

    public static void register() {
        Megumin.LOGGER.info("Registering particles for: " + Megumin.MOD_ID);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Megumin.MOD_ID, "star_particle"), STAR_PARTICLE);
    }

    public static void registerClient() {
        Megumin.LOGGER.info("Registering client-side particles for: " + Megumin.MOD_ID);
        ParticleFactoryRegistry.getInstance().register(ModParticles.STAR_PARTICLE, StarParticle.Factory::new);
    }
}
