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

public class ExplosionRingEntity extends Entity {
    private static final TrackedData<Integer> MAX_SIZE = DataTracker.registerData(ExplosionRingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> LIFESPAN = DataTracker.registerData(ExplosionRingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public final int ANIMATION_TICKS = 10;
    public int age;
    private PlayerEntity user;
    private float userYOffset;

    public ExplosionRingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(MAX_SIZE, 5);
        builder.add(LIFESPAN, 100);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("MaxSize", this.dataTracker.get(MAX_SIZE));
        nbt.putInt("LifeSpan", this.dataTracker.get(LIFESPAN));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(MAX_SIZE, nbt.getInt("MaxSize"));
        this.dataTracker.set(LIFESPAN, nbt.getInt("LifeSpan"));
    }

    @Override
    public void tick() {
        int lifeSpan = this.dataTracker.get(LIFESPAN);

        //if the entity has reached the end of its lifespan, discard it
        if (this.age >= lifeSpan) {
            this.discard();
        }

        //move the ring if it is the player ring
        if (user != null) {
            this.setPosition(user.getX(), user.getY() + userYOffset, user.getZ());
        }

        ++this.age;
        super.tick();
    }

    public int getMaxSize() {
        return this.dataTracker.get(MAX_SIZE);
    }

    public void setMaxSize(int size) {
        this.dataTracker.set(MAX_SIZE, size);
    }

    public int getLifeSpan() {
        return this.dataTracker.get(LIFESPAN);
    }

    public void setLifeSpan(int ticks) {
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
}
