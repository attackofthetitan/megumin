package net.sixunderscore.megumin.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.sixunderscore.megumin.entity.custom.ExplosionManagerEntity;

@Environment(EnvType.CLIENT)
public class ExplosionManagerEntityRenderer extends EntityRenderer<ExplosionManagerEntity, EntityRenderState> {
    public ExplosionManagerEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
