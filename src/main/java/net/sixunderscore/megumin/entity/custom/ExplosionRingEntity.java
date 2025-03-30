package net.sixunderscore.megumin.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.sixunderscore.megumin.particle.ModParticles;

public class ExplosionRingEntity extends Entity {
    private static final TrackedData<Float> MAX_SIZE = DataTracker.registerData(ExplosionRingEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> LIFESPAN = DataTracker.registerData(ExplosionRingEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public final int ANIMATION_TICKS = 7;
    public int age;
    private PlayerEntity user;
    private float userYOffset;

    public ExplosionRingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(MAX_SIZE, 5.0f);
        builder.add(LIFESPAN, 100.0f);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("MaxSize", this.dataTracker.get(MAX_SIZE));
        nbt.putFloat("LifeSpan", this.dataTracker.get(LIFESPAN));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(MAX_SIZE, nbt.getFloat("MaxSize", 1.0f));
        this.dataTracker.set(LIFESPAN, nbt.getFloat("LifeSpan", 1.0f));
    }

    @Override
    public void tick() {
        // If the entity has reached the end of its lifespan, discard it
        if (this.age >= this.dataTracker.get(LIFESPAN)) {
            this.discard();
        }

        // Move the ring and render star particles if it's the player's ring
        if (user != null) {
            this.setPosition(user.getX(), user.getY() + userYOffset, user.getZ());

            if (this.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                        ModParticles.STAR_PARTICLE,
                        user.getX() + random.nextGaussian(),
                        user.getY(),
                        user.getZ() + random.nextGaussian(),
                        2,
                        0, 0, 0,
                        1
                );
            }
        }

        ++this.age;
        super.tick();
    }

    public float getMaxSize() {
        return this.dataTracker.get(MAX_SIZE);
    }

    public void setMaxSize(float size) {
        this.dataTracker.set(MAX_SIZE, size);
    }

    public float getLifeSpan() {
        return this.dataTracker.get(LIFESPAN);
    }

    public void setLifeSpan(float ticks) {
        this.dataTracker.set(LIFESPAN, ticks);
    }

    public void setUser(PlayerEntity user, float yOffset) {
        this.user = user;
        this.userYOffset = yOffset;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}
