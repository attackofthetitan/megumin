package net.attackofthetitan.megumin.item.custom;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.attackofthetitan.megumin.entity.ModEntities;
import net.attackofthetitan.megumin.entity.custom.ExplosionManagerEntity;

public class MeguminStaffItem extends Item {
    public MeguminStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!user.getItemCooldownManager().isCoolingDown(stack)) {
            Vec3d cameraVec = user.getCameraPosVec(1.0F);

            BlockHitResult result = world.raycast(new RaycastContext(
                    cameraVec,
                    cameraVec.add(user.getRotationVec(1.0F).multiply(800)),
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    user
            ));

            if (result.getType() == HitResult.Type.BLOCK) {
                Vec3d hitPos = result.getPos();

                if (!world.isClient) {
                    ExplosionManagerEntity explosionManager = new ExplosionManagerEntity(ModEntities.EXPLOSION_MANAGER, world);
                    explosionManager.setUser(user);
                    explosionManager.setPosition(hitPos);
                    world.spawnEntity(explosionManager);

                    if (!user.getAbilities().creativeMode) {
                        stack.damage(1, user, EquipmentSlot.MAINHAND);
                    }
                }

                // Set lower cooldown if player in creative
                if (user.getAbilities().creativeMode) {
                    user.getItemCooldownManager().set(stack, 200);
                } else {
                    user.getItemCooldownManager().set(stack, 24000);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }
}
