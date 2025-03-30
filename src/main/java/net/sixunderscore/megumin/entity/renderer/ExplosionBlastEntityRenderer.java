package net.sixunderscore.megumin.entity.renderer;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.sixunderscore.megumin.Megumin;
import net.sixunderscore.megumin.entity.custom.ExplosionBlastEntity;
import net.sixunderscore.megumin.entity.renderstates.SimpleExplosionVisualRenderState;

public class ExplosionBlastEntityRenderer extends EntityRenderer<ExplosionBlastEntity, SimpleExplosionVisualRenderState> {
    private static final Identifier TEXTURE = Identifier.of(Megumin.MOD_ID, "textures/entity/explosion_circle.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);

    public ExplosionBlastEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(SimpleExplosionVisualRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();

        // Multiply rotation by player's position and scale to size
        float size = state.size;
        matrixStack.scale(size, size, size);
        matrixStack.multiply(this.dispatcher.getRotation());

        // Prepare to render the vertices
        MatrixStack.Entry entry = matrixStack.peek();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);

        // Render vertices
        produceVertex(vertexConsumer, entry, -0.5F, -0.5F, 0, 1);
        produceVertex(vertexConsumer, entry, -0.5F, 0.5F, 1, 1);
        produceVertex(vertexConsumer, entry, 0.5F, 0.5F, 1, 0);
        produceVertex(vertexConsumer, entry, 0.5F, -0.5F, 0, 0);

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
    public void updateRenderState(ExplosionBlastEntity entity, SimpleExplosionVisualRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        float elapsedTime = entity.age + tickDelta;
        float delta = Math.min(elapsedTime / entity.ANIMATION_TICKS, 1.0f);

        state.size = MathHelper.lerp(delta, 0, 125);
    }

    @Override
    public boolean shouldRender(ExplosionBlastEntity entity, Frustum frustum, double x, double y, double z) {
        return frustum.isVisible(this.getBoundingBox(entity).expand(125));
    }

    @Override
    public SimpleExplosionVisualRenderState createRenderState() {
        return new SimpleExplosionVisualRenderState();
    }
}
