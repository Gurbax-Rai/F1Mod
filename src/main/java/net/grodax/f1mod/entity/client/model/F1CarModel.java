package net.grodax.f1mod.entity.client.model;
// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;


public class F1CarModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("f1mod", "f1carmodel"), "main");
	private final ModelPart bone;

	public F1CarModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(214, 288).addBox(-40.0F, -8.0F, -1.0F, 41.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(258, 144).addBox(-18.0F, 0.0F, -36.0F, 19.0F, 10.0F, 37.0F, new CubeDeformation(0.0F))
				.texOffs(258, 0).addBox(-22.0F, 0.0F, -91.0F, 23.0F, 17.0F, 55.0F, new CubeDeformation(0.0F))
				.texOffs(210, 306).addBox(-41.0F, 8.0F, -27.0F, 20.0F, 19.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(128, 332).addBox(-26.0F, 14.0F, -21.0F, 23.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(344, 345).addBox(-40.0F, 12.0F, -136.0F, 2.0F, 9.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(316, 288).addBox(-39.0F, 16.0F, -133.0F, 40.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(108, 272).addBox(-39.0F, 19.0F, -136.0F, 40.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(252, 345).addBox(-24.0F, 17.0F, -101.0F, 19.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(258, 238).addBox(-5.0F, 8.0F, -114.0F, 6.0F, 2.0F, 48.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, 10.0F, -123.0F, 6.0F, 13.0F, 123.0F, new CubeDeformation(0.0F))
				.texOffs(108, 302).addBox(1.0F, -8.0F, -1.0F, 41.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(258, 191).addBox(1.0F, 0.0F, -36.0F, 19.0F, 10.0F, 37.0F, new CubeDeformation(0.0F))
				.texOffs(258, 72).addBox(1.0F, 0.0F, -91.0F, 23.0F, 17.0F, 55.0F, new CubeDeformation(0.0F))
				.texOffs(290, 306).addBox(23.0F, 8.0F, -27.0F, 20.0F, 19.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(190, 345).addBox(5.0F, 14.0F, -21.0F, 23.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(128, 347).addBox(40.0F, 12.0F, -136.0F, 2.0F, 9.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(108, 320).addBox(1.0F, 16.0F, -133.0F, 40.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(108, 287).addBox(1.0F, 19.0F, -136.0F, 40.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(298, 345).addBox(7.0F, 17.0F, -101.0F, 19.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 272).addBox(1.0F, 8.0F, -114.0F, 6.0F, 2.0F, 48.0F, new CubeDeformation(0.0F))
				.texOffs(0, 136).addBox(1.0F, 10.0F, -123.0F, 6.0F, 13.0F, 123.0F, new CubeDeformation(0.0F))
				.texOffs(0, 322).addBox(25.0F, 11.0F, -107.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(64, 332).addBox(-39.0F, 11.0F, -107.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -3.0F, 67.0F, 0.0F, 0.0F, -3.1416F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Animation logic can be added here if needed
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		bone.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}
}
