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
//import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sixunderscore.megumin.entity.ModEntities;
import net.sixunderscore.megumin.sound.ModSounds;

public class ExplosionManagerEntity extends Entity {
    private static final TrackedData<Integer> TIMER = DataTracker.registerData(ExplosionManagerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private PlayerEntity user;
    private int explosionDepth;

    public ExplosionManagerEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    public void setUser(PlayerEntity user) {
        this.user = user;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(TIMER, 0);
    }

    @Override
    protected void writeCustomData(WriteView view){}

    @Override
    protected void readCustomData(ReadView view){}
    /*
    @Override
    public void readCustomDataFromNBT(NbtCompound nbt) {
        this.dataTracker.set(TIMER, nbt.getInt("Timer", 1));
    }

    @Override
    public void writeCustomDataToNBT(NbtCompound nbt) {
        nbt.putInt("Timer", this.dataTracker.get(TIMER));
    }
    */
    @Override
    public void tick() {
        if (user != null) {
            switch (this.dataTracker.get(TIMER)) {
                case 0 -> {
                    initializeDepth();
                    castEffect();
                    playAmbientSound();
                    playMagicChant();
                    spawnPlayerRing();
                }

                case 40 -> spawnRing(80, 90, 650);
                case 80 -> spawnRing(30, 80,610);
                case 120 -> spawnRing(40, 70, 570);
                case 160 -> spawnRing(50, 60, 530);
                case 200 -> spawnRing(40, 50,490);
                case 240 -> spawnRing(30, 40,450);

                case 683 -> spawnRay();
                case 690 -> spawnExplosionBlast();

                case 691 -> spawnExplosionRange(1, 6, 30);
                case 692 -> spawnExplosionRange(6, 12, 30);
                case 693 -> spawnExplosionRange(12, 18, 25);
                case 694 -> spawnExplosionRange(18, 24, 25);
                case 695 -> spawnExplosionRange(24, 30, 25);
                case 696 -> spawnExplosionRange(30, 36, 20);
                case 697 -> spawnExplosionRange(36, 42, 15);

                case 705 -> {
                    applyEffectsToUser();
                    this.discard();
                }
            }

            this.dataTracker.set(TIMER, this.dataTracker.get(TIMER) + 1);
        }

        super.tick();
    }

    // Workaround to have the explosion work properly on superflat worlds
    private void initializeDepth() {
        World world = this.getWorld();

        if (!world.isClient) {
            BlockPos depth = this.getBlockPos();

            for (int i = 0; i <= 10; ++i) {
                if (world.getBlockState(depth.down(i)).isOf(Blocks.BEDROCK)) {
                    this.explosionDepth = i;
                    return;
                }
            }
        }

        this.explosionDepth = 10;
    }

    private void castEffect() {
        if (!user.getAbilities().creativeMode && !this.getWorld().isClient) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 705, 7));
        }
    }
    private void playAmbientSound() {
        if (!this.getWorld().isClient) {
            this.getWorld().playSound(
                    null,
                    user.getBlockPos(),
                    ModSounds.MAGIC_AMBIENT,
                    SoundCategory.MASTER,
                    10f,
                    1f
            );
        }
    }
    private void playMagicChant() {
        if(!this.getWorld().isClient) {
            this.getWorld().playSound(
                    null,
                    user.getBlockPos(),
                    ModSounds.MAGIC_CHANT,
                    SoundCategory.MASTER,
                    10f,
                    1f
            );
        }
    }
    private void spawnPlayerRing() {
        ExplosionRingEntity ring = new ExplosionRingEntity(ModEntities.EXPLOSION_RING, this.getWorld());
        ring.setUser(user, 0.3f);
        ring.setLifeSpan(695);
        ring.setPosition(user.getX(), user.getY() + 0.3f, user.getZ());

        this.getWorld().spawnEntity(ring);

        if (!this.getWorld().isClient) { // Play ring sound effect
            this.getWorld().playSound(
                    null,
                    user.getBlockPos(),
                    ModSounds.MAGIC_RING,
                    SoundCategory.MASTER,
                    25f,
                    1f
            );
        }
    }

    private void spawnRing(int size, int height, int ticks) {
        ExplosionRingEntity ring = new ExplosionRingEntity(ModEntities.EXPLOSION_RING, this.getWorld());
        ring.setMaxSize(size);
        ring.setLifeSpan(ticks);
        ring.setPosition(this.getX(), this.getY() + height, this.getZ());

        this.getWorld().spawnEntity(ring);

        if (!this.getWorld().isClient) { // Play ring sound effect
            this.getWorld().playSound(
                    null,
                    this.getBlockPos().add(0, height,0),
                    ModSounds.MAGIC_RING,
                    SoundCategory.MASTER,
                    25f,
                    1f
            );
        }
    }

    private void spawnRay() {
        ExplosionRayEntity ray = new ExplosionRayEntity(ModEntities.EXPLOSION_RAY, this.getWorld());
        ray.setPosition(this.getX(), this.getY() + 90, this.getZ());

        this.getWorld().spawnEntity(ray);
    }

    private void spawnExplosionBlast() {
        ExplosionBlastEntity blast = new ExplosionBlastEntity(ModEntities.EXPLOSION_BLAST, this.getWorld());
        blast.setPosition(this.getX(), this.getY(), this.getZ());

        this.getWorld().spawnEntity(blast);

        if (!this.getWorld().isClient) { // Play explosion sound effect
            this.getWorld().playSound(
                    null,
                    this.getBlockPos(),
                    ModSounds.BIG_EXPLOSION,
                    SoundCategory.MASTER,
                    35f,
                    1f
            );
        }
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
                        this.getY() - this.explosionDepth,
                        this.getZ() + zOffset,
                        power, false, World.ExplosionSourceType.BLOCK
                );
            }
        }

        if (this.explosionDepth != 0) this.explosionDepth -= 2;
    }

    private void applyEffectsToUser() {
        if (!user.getAbilities().creativeMode && !this.getWorld().isClient) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 24000, 4));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 24000, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 24000, 2));
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
