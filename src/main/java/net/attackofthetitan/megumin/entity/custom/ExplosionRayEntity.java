package net.attackofthetitan.megumin.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;

public class ExplosionRayEntity extends Entity {
    public int age;
    public final int ANIMATION_TICKS = 7;

    public ExplosionRayEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void writeCustomData(WriteView nbt) {}

    @Override
    protected void readCustomData(ReadView view){}

    @Override
    public void tick() {
        if (this.age >= ANIMATION_TICKS) this.discard();
        ++this.age;
        super.tick();
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
