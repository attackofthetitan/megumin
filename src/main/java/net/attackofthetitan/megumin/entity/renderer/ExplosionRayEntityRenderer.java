package net.attackofthetitan.megumin.entity.renderer;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.attackofthetitan.megumin.Megumin;
import net.attackofthetitan.megumin.entity.custom.ExplosionRayEntity;
import net.attackofthetitan.megumin.entity.renderstates.SimpleExplosionVisualRenderState;
import org.joml.Quaternionf;

public class ExplosionRayEntityRenderer extends EntityRenderer<ExplosionRayEntity, SimpleExplosionVisualRenderState> {
    private static final Identifier TEXTURE = Identifier.of(Megumin.MOD_ID, "textures/entity/explosion_ray.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);

    public ExplosionRayEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(SimpleExplosionVisualRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();

        // Multiply y-axis by player's position
        Quaternionf playerQuaternion = this.dispatcher.getRotation();
        playerQuaternion.x = 0;
        playerQuaternion.z = 0;
        matrixStack.multiply(playerQuaternion);

        // Prepare to render the vertices
        MatrixStack.Entry entry = matrixStack.peek();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);

        // Render vertices
        float size = state.size;
        produceVertex(vertexConsumer, entry, -1.5F, size, 0, 1);
        produceVertex(vertexConsumer, entry, 1.5F, size, 1, 1);
        produceVertex(vertexConsumer, entry, 1.5F, 0, 1, 0);
        produceVertex(vertexConsumer, entry, -1.5F, 0, 0, 0);

        matrixStack.pop();
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
    public void updateRenderState(ExplosionRayEntity entity, SimpleExplosionVisualRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        float elapsedTime = entity.age + tickDelta;
        float delta = Math.min(elapsedTime / entity.ANIMATION_TICKS, 1.0f);

        state.size = MathHelper.lerp(delta, 0, -100);
    }

    @Override
    public boolean shouldRender(ExplosionRayEntity entity, Frustum frustum, double x, double y, double z) {
        return frustum.isVisible(this.getBoundingBox(entity).expand(0.5));
    }

    @Override
    public SimpleExplosionVisualRenderState createRenderState() {
        return new SimpleExplosionVisualRenderState();
    }
}
