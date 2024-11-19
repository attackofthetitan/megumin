package net.sixunderscore.megumin.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class StarParticle extends SpriteBillboardParticle {
    protected StarParticle(ClientWorld level, double xCords, double yCords, double zCords, SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCords, yCords, zCords, xd, yd, zd);

        this.velocityMultiplier = 1.6f;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.scale = 0.75f;
        this.maxAge = 20;

        this.setSpriteForAge(spriteSet);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientWorld level, double x, double y, double z, double dx, double dy, double dz) {
            return new StarParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}