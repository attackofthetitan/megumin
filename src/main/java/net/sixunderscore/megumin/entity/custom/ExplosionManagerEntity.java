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

import java.util.ArrayList;
import java.util.List;

public class ExplosionManagerEntity extends Entity {
    private static final TrackedData<Integer> TIMER = DataTracker.registerData(ExplosionManagerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private final List<Entity> effectEntities = new ArrayList<>();
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
        switch (this.dataTracker.get(TIMER)) {
            case 0:
                spawnRing(70, 70);
                break;
            case 10:
                spawnRing(20, 60);
                break;
            case 20:
                spawnRing(30, 50);
                break;
            case 30:
                spawnRing(30, 40);
                break;
            case 40:
                spawnRing(20, 30);
                break;
            case 105:
                //spawn ray
                ExplosionRayEntity ray = new ExplosionRayEntity(ModEntities.EXPLOSION_RAY, this.getWorld());
                ray.setPosition(this.getX(), this.getY() + 70, this.getZ());
                effectEntities.add(ray);
                this.getWorld().spawnEntity(ray);
                break;
            case 110:
                if (!this.getWorld().isClient && user != null) {
                    for (int i = 0; i <= 20; ++i) {
                        this.getWorld().createExplosion(user,
                                this.getX() + random.nextGaussian() * 10,
                                this.getY(),
                                this.getZ() + random.nextGaussian() * 10,
                                20, false, World.ExplosionSourceType.MOB
                        );
                    }
                    if (!user.getAbilities().creativeMode) {
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 1));
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 2));
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1200, 2));
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1200, 2));
                    }
                }
                break;
            case 120:
                //clean up
                for (Entity entity : effectEntities) {
                    entity.discard();
                }
                effectEntities.clear();
                this.discard();
                break;
        }

        this.dataTracker.set(TIMER, this.dataTracker.get(TIMER) + 1);
        super.tick();
    }

    private void spawnRing(int size, int height) {
        ExplosionRingEntity ring = new ExplosionRingEntity(ModEntities.EXPLOSION_RING, this.getWorld());
        ring.setSize(size);
        ring.setPosition(this.getX(), this.getY() + height, this.getZ());
        effectEntities.add(ring);
        this.getWorld().spawnEntity(ring);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }
}
