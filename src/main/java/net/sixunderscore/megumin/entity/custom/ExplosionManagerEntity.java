package net.sixunderscore.megumin.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.sixunderscore.megumin.entity.ModEntities;

public class ExplosionManagerEntity extends Entity {
    private static final TrackedData<Integer> TIMER = DataTracker.registerData(ExplosionManagerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private PlayerEntity user;

    public ExplosionManagerEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setUser(PlayerEntity user) {
        this.user = user;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(TIMER, 0);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(TIMER, nbt.getInt("Timer"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Timer", this.dataTracker.get(TIMER));
    }

    @Override
    public void tick() {
        if (user != null) {
            switch (this.dataTracker.get(TIMER)) {
                case 0 -> {
                    spawnRing(80, 80, 115);
                    spawnPlayerRing();
                }
                case 10 -> spawnRing(30, 70,105);
                case 20 -> spawnRing(40, 60, 95);
                case 30 -> spawnRing(50, 50, 85);
                case 40 -> spawnRing(40, 40,75);
                case 50 -> spawnRing(30, 30,65);
                case 105 -> spawnRay();
                case 115 -> {
                    if (!this.getWorld().isClient) {
                        spawnExplosions();
                        applyEffectsToUser();
                    }
                }
                case 125 -> this.discard();
            }
        }

        this.dataTracker.set(TIMER, this.dataTracker.get(TIMER) + 1);
        super.tick();
    }

    private void spawnRing(int size, int height, int ticks) {
        ExplosionRingEntity ring = new ExplosionRingEntity(ModEntities.EXPLOSION_RING, this.getWorld());
        ring.setMaxSize(size);
        ring.setLifeSpan(ticks);
        ring.setPosition(this.getX(), this.getY() + height, this.getZ());
        this.getWorld().spawnEntity(ring);
    }

    private void spawnPlayerRing() {
        ExplosionRingEntity ring = new ExplosionRingEntity(ModEntities.EXPLOSION_RING, this.getWorld());
        ring.setUser(user, 0.3f);
        ring.setLifeSpan(115);
        ring.setPosition(user.getX(), user.getY() + (float) 0.3, user.getZ());
        this.getWorld().spawnEntity(ring);
    }

    private void spawnRay() {
        ExplosionRayEntity ray = new ExplosionRayEntity(ModEntities.EXPLOSION_RAY, this.getWorld());
        ray.setPosition(this.getX(), this.getY() + 80, this.getZ());
        this.getWorld().spawnEntity(ray);
    }

    private void spawnExplosions() {
        int explosionDepth = 15;

        for (int i = 0; i <= 25; ++i) {
            this.getWorld().createExplosion(user,
                    this.getX() + random.nextGaussian() * i,
                    this.getY() - explosionDepth,
                    this.getZ() + random.nextGaussian() * i,
                    20, false, World.ExplosionSourceType.MOB
            );
            if (explosionDepth > 0) --explosionDepth;
        }
    }

    private void applyEffectsToUser() {
        if (!user.getAbilities().creativeMode) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 1));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1200, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1200, 2));
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }
}
