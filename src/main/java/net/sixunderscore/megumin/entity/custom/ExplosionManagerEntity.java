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
                    spawnRing(80, 90, 125);
                    spawnPlayerRing();
                }
                case 10 -> spawnRing(30, 80,115);
                case 20 -> spawnRing(40, 70, 105);
                case 30 -> spawnRing(50, 60, 95);
                case 40 -> spawnRing(40, 50,85);
                case 50 -> spawnRing(30, 40,75);
                case 115 -> spawnRay();
                case 120 -> spawnExplosionCircle();
                case 121 -> {
                    if (!this.getWorld().isClient) {
                        spawnExplosions();
                        applyEffectsToUser();
                    }
                }
                case 135 -> this.discard();
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
        ring.setLifeSpan(125);
        ring.setPosition(user.getX(), user.getY() + 0.3f, user.getZ());
        this.getWorld().spawnEntity(ring);
    }

    private void spawnRay() {
        ExplosionRayEntity ray = new ExplosionRayEntity(ModEntities.EXPLOSION_RAY, this.getWorld());
        ray.setPosition(this.getX(), this.getY() + 90, this.getZ());
        this.getWorld().spawnEntity(ray);
    }

    private void spawnExplosionCircle() {
        ExplosionCircleEntity circle = new ExplosionCircleEntity(ModEntities.EXPLOSION_CIRCLE, this.getWorld());
        circle.setPosition(this.getX(), this.getY(), this.getZ());
        this.getWorld().spawnEntity(circle);
    }

    private void spawnExplosions() {
        World world = this.getWorld();
        float depth = 10;

        for (int i = 1; i <= 31; ++i) {
            world.createExplosion(user,
                    this.getX() + random.nextBetween(-i, i),
                    this.getY() - depth,
                    this.getZ() + random.nextBetween(-i, i),
                    15, false, World.ExplosionSourceType.BLOCK
            );

            if (depth > 0) depth -= 0.5f;
        }
    }

    private void applyEffectsToUser() {
        if (!user.getAbilities().creativeMode) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 1));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1600, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1600, 3));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1600, 2));
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }
}
