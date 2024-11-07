package net.sixunderscore.megumin.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ExplosionRingEntity extends Entity {
    private static final TrackedData<Integer> SIZE = DataTracker.registerData(ExplosionRingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MAX_SIZE = DataTracker.registerData(ExplosionRingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public int age;

    public ExplosionRingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SIZE, 1);
        builder.add(MAX_SIZE, 5);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Size", this.dataTracker.get(SIZE));
        nbt.putInt("MaxSize", this.dataTracker.get(MAX_SIZE));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(SIZE, nbt.getInt("Size"));
        this.dataTracker.set(MAX_SIZE, nbt.getInt("MaxSize"));
    }

    @Override
    public void tick() {
        if (this.age > 130) this.discard();
        int size = this.dataTracker.get(SIZE);

        if (size < this.dataTracker.get(MAX_SIZE)) {
             this.dataTracker.set(SIZE, size + 1);
        }

        ++this.age;
        super.tick();
    }

    public int getSize() {
        return this.dataTracker.get(SIZE);
    }

    public int getMaxSize() {
        return this.dataTracker.get(MAX_SIZE);
    }

    public void setSize(int size) {
        this.dataTracker.set(MAX_SIZE, size);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }
}
