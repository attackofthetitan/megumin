package net.sixunderscore.megumin.entity.renderer;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.sixunderscore.megumin.Megumin;
import net.sixunderscore.megumin.entity.custom.ExplosionCircleEntity;
import net.sixunderscore.megumin.entity.renderstates.SimpleExplosionVisualRenderState;

public class ExplosionCircleEntityRenderer extends EntityRenderer<ExplosionCircleEntity, SimpleExplosionVisualRenderState> {
    private static final Identifier TEXTURE = Identifier.of(Megumin.MOD_ID, "textures/entity/explosion_circle.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);

    public ExplosionCircleEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(SimpleExplosionVisualRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();

        //multiply rotation by player's position and scale to size
        float size = state.size;
        matrixStack.scale(size, size, size);
        matrixStack.multiply(this.dispatcher.getRotation());

        //prepare to render the vertices
        MatrixStack.Entry entry = matrixStack.peek();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);

        //render vertices
        produceVertex(vertexConsumer, entry, -0.5F, -0.5F, 0, 1);
        produceVertex(vertexConsumer, entry, -0.5F, 0.5F,1, 1);
        produceVertex(vertexConsumer, entry, 0.5F, 0.5F, 1, 0);
        produceVertex(vertexConsumer, entry, 0.5F, -0.5F,0, 0);

        matrixStack.pop();
        super.render(state, matrixStack, vertexConsumerProvider, light);
    }

    private void produceVertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, int textureU, int textureV) {
        vertexConsumer.vertex(matrix, x, y, 0)
                .color(Colors.WHITE)
                .texture((float) textureU, (float) textureV)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(15728880)
                .normal(matrix, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public void updateRenderState(ExplosionCircleEntity entity, SimpleExplosionVisualRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        float elapsedTime = entity.age + tickDelta;
        float t = Math.min(elapsedTime / entity.ANIMATION_TICKS, 1.0f);

        state.size = MathHelper.lerp(t, 0, 120);
    }

    @Override
    public boolean shouldRender(ExplosionCircleEntity entity, Frustum frustum, double x, double y, double z) {
        //checking if entity is closer to camera than 850 blocks
        if (entity.squaredDistanceTo(x, y, z) <= 722500) {
            return true;
        }

        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public SimpleExplosionVisualRenderState createRenderState() {
        return new SimpleExplosionVisualRenderState();
    }
}
