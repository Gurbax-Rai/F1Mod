package net.grodax.f1mod.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.entity.client.model.F1CarModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class F1CarItemRenderer extends BlockEntityWithoutLevelRenderer {
    // 1.21.1 resource location definition
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(F1Mod.MOD_ID, "textures/entity/f1_car_entity.png");
    private F1CarModel model;

    public F1CarItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (this.model == null) {
            this.model = new F1CarModel(Minecraft.getInstance().getEntityModels().bakeLayer(F1CarModel.LAYER_LOCATION));
        }

        poseStack.pushPose();

        // Center and flip the model (Minecraft entity models render upside down by default)
        poseStack.translate(0.5F, 0.75F, 0.5F);
        poseStack.scale(-0.5F, 0.5F, 0.5F);

        // Get foil buffer directly to support enchantment glint
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(TEXTURE), true, stack.hasFoil());

        // -1 represents 0xFFFFFFFF (White ARGB tint)
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, -1);

        poseStack.popPose();
    }
}