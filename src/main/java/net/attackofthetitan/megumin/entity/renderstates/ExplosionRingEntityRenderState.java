package net.attackofthetitan.megumin.entity.renderstates;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class ExplosionRingEntityRenderState extends EntityRenderState {
    public float size;
    public float rotation;
}
