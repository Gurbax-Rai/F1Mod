package net.grodax.f1mod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.math.Axis;
import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.entity.custom.F1CarEntity;
import net.grodax.f1mod.entity.client.model.F1CarModel;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class F1CarRenderer extends EntityRenderer<F1CarEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(F1Mod.MOD_ID, "textures/entity/f1_car_entity.png");

    private final F1CarModel<F1CarEntity> model;

    public F1CarRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new F1CarModel<>(context.bakeLayer(F1CarModel.LAYER_LOCATION));
        this.shadowRadius = 1.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(F1CarEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(F1CarEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(getTextureLocation(entity)));

        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getYRot()));

        poseStack.translate(0.0f, 0.55f, 0.0f);

        float scale = 2f;
        poseStack.scale(scale, scale , scale);

        model.setSteeringAngle(entity.getSteeringAngle());

        model.setupAnim(entity, 0, 0, partialTicks, 0, 0);

        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFF);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}