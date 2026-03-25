package net.grodax.f1mod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

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
            ResourceLocation.fromNamespaceAndPath(F1Mod.MOD_ID, "textures/entity/f1_car.png");

    private final F1CarModel<F1CarEntity> model;

    public F1CarRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new F1CarModel<>(context.bakeLayer(F1CarModel.LAYER_LOCATION));
        this.shadowRadius = 1.0f; // Adjust shadow size to match scaled car
    }

    @Override
    public ResourceLocation getTextureLocation(F1CarEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(F1CarEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Get VertexConsumer from buffer for solid rendering
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(getTextureLocation(entity)));

        poseStack.pushPose(); // Save current transform state

        // Scale the model so it's roughly 1 Minecraft block in size
        float scale = 1.0f; // Adjust this value if car still looks too small or large
        poseStack.scale(scale * 16f, scale * 16f, scale * 16f); // 16 = pixels per block

        // Render the model
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose(); // Restore transform

        // Call super to render shadows and other default entity features
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}