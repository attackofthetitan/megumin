package net.sixunderscore.megumin.entity.custom;

import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sixunderscore.megumin.entity.ModEntities;

public class ExplosionManagerEntity extends Entity {
    private static final TrackedData<Integer> TIMER = DataTracker.registerData(ExplosionManagerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private PlayerEntity user;
    private int depth;

    public ExplosionManagerEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    public void setUser(PlayerEntity user) {
        this.user = user;
    }

    // Workaround to have the explosion work properly on superflat worlds
    private void initializeDepth() {
        World world = this.getWorld();

        if (!world.isClient) {
            BlockPos depth = this.getBlockPos();

            for (int i = 0; i <= 10; ++i) {
                if (world.getBlockState(depth.down(i)).isOf(Blocks.BEDROCK)) this.depth = i;
            }
        }

        this.depth = 10;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(TIMER, 0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(TIMER, nbt.getInt("Timer"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Timer", this.dataTracker.get(TIMER));
    }

    @Override
    public void tick() {
        if (user != null) {
            switch (this.dataTracker.get(TIMER)) {
                case 0 -> {
                    initializeDepth();
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

                case 121 -> spawnExplosionRange(1, 6, 20);
                case 122 -> spawnExplosionRange(6, 12, 20);
                case 123 -> spawnExplosionRange(12, 18, 15);
                case 124 -> spawnExplosionRange(18, 24, 15);
                case 125 -> spawnExplosionRange(24, 30, 15);
                case 126 -> spawnExplosionRange(30, 36, 12);

                case 135 -> {
                    applyEffectsToUser();
                    this.discard();
                }
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

    private void spawnExplosionRange(int startRange, int endRange, int power) {
        World world = this.getWorld();

        if (!world.isClient) {
            for (int i = startRange; i <= endRange; ++i) {
                int xOffset, zOffset;

                // Set explosion offset near crater radius border
                if (random.nextBoolean()) {
                    xOffset = random.nextBoolean() ? -i : i;
                    zOffset = random.nextBetween(-i, i);
                } else {
                    xOffset = random.nextBetween(-i, i);
                    zOffset = random.nextBoolean() ? -i : i;
                }

                world.createExplosion(
                        user,
                        this.getX() + xOffset,
                        this.getY() - this.depth,
                        this.getZ() + zOffset,
                        power, false, World.ExplosionSourceType.BLOCK
                );
            }
        }

        if (this.depth != 0) this.depth -= 2;
    }

    private void applyEffectsToUser() {
        if (!user.getAbilities().creativeMode && !this.getWorld().isClient) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1600, 3));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1600, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 1600, 2));
        }
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
