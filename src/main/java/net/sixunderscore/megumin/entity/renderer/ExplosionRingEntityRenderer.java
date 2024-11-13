package net.sixunderscore.megumin.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.sixunderscore.megumin.Megumin;
import net.sixunderscore.megumin.entity.custom.ExplosionRingEntity;
import net.sixunderscore.megumin.entity.renderstates.ExplosionRingEntityRenderState;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class ExplosionRingEntityRenderer extends EntityRenderer<ExplosionRingEntity, ExplosionRingEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.of(Megumin.MOD_ID, "textures/entity/explosion_ring.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);

    public ExplosionRingEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ExplosionRingEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();

        //scale and rotate by the entity's provided size and rotation
        float size = state.size;
        matrixStack.scale(size, size, size);
        matrixStack.multiply(new Quaternionf().rotateY(state.rotation));

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

    private void produceVertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float z, int textureU, int textureV) {
        vertexConsumer.vertex(matrix, x, 0, z)
                .color(Colors.WHITE)
                .texture((float)textureU, (float)textureV)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(15728880)
                .normal(matrix, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public void updateRenderState(ExplosionRingEntity entity, ExplosionRingEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        float maxSize = entity.getMaxSize();
        float entityLifeSpan = entity.getLifeSpan();
        float animationTicks = (float) entity.ANIMATION_TICKS;

        if (entity.age < animationTicks) {
            //growing phase
            float elapsedTime = (entity.age + tickDelta); //time since growth started
            float delta = Math.min(elapsedTime / animationTicks, 1.0f);

            state.size = MathHelper.lerp(delta, 0, maxSize);
        } else if (entity.age > entityLifeSpan - animationTicks) {
            //shrinking phase
            float elapsedTime = (entity.age + tickDelta) - (entityLifeSpan - animationTicks); //time since shrink started
            float delta = Math.min(elapsedTime / animationTicks, 1.0f);

            state.size = MathHelper.lerp(delta, maxSize, 0);
        } else {
            state.size = maxSize;
        }

        state.rotation = (entity.age + tickDelta) * 0.2f;
    }

    @Override
    public boolean shouldRender(ExplosionRingEntity entity, Frustum frustum, double x, double y, double z) {
        //checking if entity is closer to camera than 850 blocks
        if (entity.squaredDistanceTo(x, y, z) <= 722500) {
            return true;
        }

        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public ExplosionRingEntityRenderState createRenderState() {
        return new ExplosionRingEntityRenderState();
    }
}
