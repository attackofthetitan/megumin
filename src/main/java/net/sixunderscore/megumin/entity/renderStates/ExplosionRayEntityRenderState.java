package net.sixunderscore.megumin.entity.renderStates;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class ExplosionRayEntityRenderState extends EntityRenderState {
    public float size;
}
