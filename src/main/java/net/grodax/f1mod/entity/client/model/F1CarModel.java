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
	private final ModelPart f1_car;
	private final ModelPart nose;
	private final ModelPart front_suspension;
	private final ModelPart floor;
	private final ModelPart front_wheel;
	private final ModelPart sidepod_front;
	private final ModelPart sidepod_back;
	private final ModelPart divider;
	private final ModelPart rear_suspension;
	private final ModelPart rear_wheel;
	private final ModelPart rear_wing;
	private final ModelPart front_wing;
	private final ModelPart first_flap;
	private final ModelPart second_flap;
	private final ModelPart third_flap;
	private final ModelPart fourth_flap;
	private final ModelPart endplate;

	public F1CarModel(ModelPart root) {
		this.f1_car = root.getChild("f1_car");
		this.nose = this.f1_car.getChild("nose");
		this.front_suspension = this.f1_car.getChild("front_suspension");
		this.floor = this.f1_car.getChild("floor");
		this.front_wheel = this.f1_car.getChild("front_wheel");
		this.sidepod_front = this.f1_car.getChild("sidepod_front");
		this.sidepod_back = this.f1_car.getChild("sidepod_back");
		this.divider = this.f1_car.getChild("divider");
		this.rear_suspension = this.f1_car.getChild("rear_suspension");
		this.rear_wheel = this.f1_car.getChild("rear_wheel");
		this.rear_wing = this.f1_car.getChild("rear_wing");
		this.front_wing = this.f1_car.getChild("front_wing");
		this.first_flap = this.front_wing.getChild("first_flap");
		this.second_flap = this.front_wing.getChild("second_flap");
		this.third_flap = this.front_wing.getChild("third_flap");
		this.fourth_flap = this.front_wing.getChild("fourth_flap");
		this.endplate = this.front_wing.getChild("endplate");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition f1_car = partdefinition.addOrReplaceChild("f1_car", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition nose = f1_car.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 94).mirror().addBox(0.0F, -4.0F, -37.0F, 1.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(42, 22).mirror().addBox(0.0F, -6.0F, -36.0F, 1.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 94).addBox(-1.0F, -4.0F, -37.0F, 1.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 22).mirror().addBox(1.0F, -6.0F, -36.0F, 1.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 22).addBox(-2.0F, -6.0F, -36.0F, 1.0F, 4.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(24, 85).mirror().addBox(2.0F, -5.0F, -36.0F, 1.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 85).addBox(-3.0F, -5.0F, -36.0F, 1.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 46).addBox(-3.0F, -6.0F, -35.0F, 1.0F, 4.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(42, 22).addBox(-1.0F, -6.0F, -36.0F, 1.0F, 4.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(0, 46).mirror().addBox(2.0F, -6.0F, -35.0F, 1.0F, 4.0F, 19.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(40, 46).mirror().addBox(0.0F, -7.0F, -34.0F, 2.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(40, 46).addBox(-2.0F, -7.0F, -34.0F, 2.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(74, 65).mirror().addBox(0.0F, -8.0F, -16.0F, 3.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(74, 65).addBox(-3.0F, -8.0F, -16.0F, 3.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front_suspension = f1_car.addOrReplaceChild("front_suspension", CubeListBuilder.create().texOffs(20, 94).mirror().addBox(2.0F, -1.0F, -24.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, 94).addBox(-3.0F, -1.0F, -24.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(64, 92).mirror().addBox(3.0F, -1.0F, -23.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(64, 92).addBox(-7.0F, -1.0F, -23.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(58, 97).mirror().addBox(7.0F, -1.0F, -22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(58, 97).addBox(-8.0F, -1.0F, -22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(28, 83).mirror().addBox(2.0F, -1.0F, -21.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(28, 83).addBox(-7.0F, -1.0F, -21.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(86, 99).mirror().addBox(6.0F, -1.0F, -22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(86, 99).addBox(-7.0F, -1.0F, -22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(86, 101).mirror().addBox(3.0F, -1.0F, -17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(86, 101).addBox(-4.0F, -1.0F, -17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(56, 113).mirror().addBox(4.0F, -1.0F, -18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(56, 113).addBox(-5.0F, -1.0F, -18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(60, 113).mirror().addBox(5.0F, -1.0F, -19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(60, 113).addBox(-6.0F, -1.0F, -19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(64, 113).mirror().addBox(6.0F, -1.0F, -20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(64, 113).addBox(-7.0F, -1.0F, -20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(68, 113).mirror().addBox(7.0F, -1.0F, -21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(68, 113).addBox(-8.0F, -1.0F, -21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition floor = f1_car.addOrReplaceChild("floor", CubeListBuilder.create().texOffs(98, 89).mirror().addBox(-2.0F, -2.0F, -11.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(98, 89).addBox(-5.0F, -2.0F, -11.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(62, 109).mirror().addBox(3.0F, -2.0F, -10.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(62, 109).addBox(-7.0F, -2.0F, -10.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(84, 39).mirror().addBox(-2.0F, -2.0F, -9.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 39).addBox(-9.0F, -2.0F, -9.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(-2.0F, -2.0F, -4.0F, 13.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).addBox(-11.0F, -2.0F, -4.0F, 13.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(84, 21).mirror().addBox(-2.0F, -2.0F, -7.0F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 21).addBox(-10.0F, -2.0F, -7.0F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(50, 106).addBox(-10.0F, -2.0F, -7.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(64, 95).mirror().addBox(-2.0F, -2.0F, 20.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(64, 95).addBox(-9.0F, -2.0F, 20.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(66, 15).mirror().addBox(-2.0F, -2.0F, 16.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(66, 15).addBox(-10.0F, -2.0F, 16.0F, 12.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(48, 113).addBox(-11.0F, -2.0F, 15.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(50, 106).mirror().addBox(7.0F, -2.0F, -7.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(48, 113).mirror().addBox(8.0F, -2.0F, 15.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition front_wheel = f1_car.addOrReplaceChild("front_wheel", CubeListBuilder.create().texOffs(0, 69).addBox(-25.0F, -4.0001F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 69).mirror().addBox(-3.0F, -4.0001F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(11.0F, -4.0F, -21.0F));

		PartDefinition sidepod_front = f1_car.addOrReplaceChild("sidepod_front", CubeListBuilder.create().texOffs(28, 76).mirror().addBox(0.0F, -1.0F, -9.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(28, 76).addBox(-4.0F, -1.0F, -9.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(28, 69).mirror().addBox(0.0F, -1.0F, -8.0F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(28, 69).addBox(-5.0F, -1.0F, -8.0F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(86, 103).mirror().addBox(0.0F, -1.0F, -7.0F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(86, 103).addBox(-7.0F, -1.0F, -7.0F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(98, 12).mirror().addBox(0.0F, -1.0F, -6.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(98, 12).addBox(-8.0F, -1.0F, -6.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(104, 50).mirror().addBox(3.0F, -1.0F, -5.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(104, 50).addBox(-9.0F, -1.0F, -5.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(66, 0).mirror().addBox(3.0F, -1.0F, -4.0F, 7.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(66, 0).addBox(-10.0F, -1.0F, -4.0F, 7.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 85).mirror().addBox(0.0F, -1.0F, 5.0F, 9.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 85).addBox(-9.0F, -1.0F, 5.0F, 9.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition sidepod_back = f1_car.addOrReplaceChild("sidepod_back", CubeListBuilder.create().texOffs(90, 95).mirror().addBox(0.0F, -1.0F, 8.0F, 8.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(90, 95).addBox(-8.0F, -1.0F, 8.0F, 8.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(20, 97).mirror().addBox(0.0F, -1.0F, 11.0F, 7.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, 97).addBox(-7.0F, -1.0F, 11.0F, 7.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(98, 0).mirror().addBox(0.0F, -1.0F, 14.0F, 6.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(98, 0).addBox(-6.0F, -1.0F, 14.0F, 6.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 105).mirror().addBox(0.0F, -1.0F, 17.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 105).addBox(-5.0F, -1.0F, 17.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(98, 80).mirror().addBox(0.0F, -1.0F, 19.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(98, 80).addBox(-4.0F, -1.0F, 19.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 85).mirror().addBox(0.0F, -1.0F, 23.0F, 3.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(44, 85).addBox(-3.0F, -1.0F, 23.0F, 3.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition divider = f1_car.addOrReplaceChild("divider", CubeListBuilder.create().texOffs(58, 99).mirror().addBox(0.0F, -15.0F, 5.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(58, 99).addBox(-3.0F, -15.0F, 5.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(72, 99).mirror().addBox(0.0F, -14.0F, 9.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(72, 99).addBox(-2.0F, -14.0F, 9.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(80, 46).mirror().addBox(0.0F, -13.0F, 14.0F, 1.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(80, 46).addBox(-1.0F, -13.0F, 14.0F, 1.0F, 5.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(64, 85).mirror().addBox(3.0F, -11.0F, 6.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(64, 85).addBox(-4.0F, -11.0F, 6.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(102, 103).mirror().addBox(2.0F, -11.0F, 9.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(102, 103).addBox(-3.0F, -11.0F, 9.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(74, 80).mirror().addBox(1.0F, -12.0F, 14.0F, 1.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(74, 80).addBox(-2.0F, -12.0F, 14.0F, 1.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rear_suspension = f1_car.addOrReplaceChild("rear_suspension", CubeListBuilder.create().texOffs(84, 43).mirror().addBox(0.0F, -5.0F, 27.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 43).addBox(-8.0F, -5.0F, 27.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(72, 113).mirror().addBox(3.0F, -5.0F, 23.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(72, 113).addBox(-4.0F, -5.0F, 23.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(76, 113).mirror().addBox(4.0F, -5.0F, 24.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(76, 113).addBox(-5.0F, -5.0F, 24.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(80, 113).mirror().addBox(5.0F, -5.0F, 25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(80, 113).addBox(-6.0F, -5.0F, 25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(114, 21).mirror().addBox(6.0F, -5.0F, 26.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(114, 21).addBox(-7.0F, -5.0F, 26.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rear_wheel = f1_car.addOrReplaceChild("rear_wheel", CubeListBuilder.create().texOffs(40, 65).mirror().addBox(-4.0F, -5.0F, -5.0F, 7.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(40, 65).addBox(-27.0F, -5.0F, -5.0F, 7.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -5.0F, 27.0F));

		PartDefinition rear_wing = f1_car.addOrReplaceChild("rear_wing", CubeListBuilder.create().texOffs(98, 8).mirror().addBox(0.0F, -13.0F, 30.0F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(98, 8).addBox(-8.0F, -13.0F, 30.0F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(80, 62).mirror().addBox(0.0F, -14.0F, 33.0F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(80, 62).addBox(-8.0F, -14.0F, 33.0F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(98, 19).mirror().addBox(0.0F, -15.0F, 35.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(98, 19).addBox(-8.0F, -15.0F, 35.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(100, 62).mirror().addBox(8.0F, -15.0F, 30.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(100, 62).addBox(-9.0F, -15.0F, 30.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(112, 95).mirror().addBox(0.0F, -14.0F, 26.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(112, 95).addBox(-1.0F, -14.0F, 26.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(100, 72).mirror().addBox(0.0F, -15.0F, 27.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(100, 72).addBox(-1.0F, -15.0F, 27.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(104, 26).mirror().addBox(0.0F, -9.0F, 30.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(104, 26).addBox(-2.0F, -9.0F, 30.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(98, 93).mirror().addBox(0.0F, -12.0F, 35.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(98, 93).addBox(-8.0F, -12.0F, 35.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(34, 113).mirror().addBox(0.0F, -11.0F, 35.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(34, 113).addBox(-1.0F, -11.0F, 35.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(94, 110).addBox(-4.0F, -11.0F, 35.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(94, 110).mirror().addBox(1.0F, -11.0F, 35.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front_wing = f1_car.addOrReplaceChild("front_wing", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition first_flap = front_wing.addOrReplaceChild("first_flap", CubeListBuilder.create().texOffs(74, 109).mirror().addBox(0.0F, -2.0F, -39.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(74, 109).addBox(-2.0F, -2.0F, -39.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(14, 105).mirror().addBox(2.0F, -2.0F, -38.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(14, 105).addBox(-5.0F, -2.0F, -38.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(38, 110).mirror().addBox(5.0F, -2.0F, -37.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(38, 110).addBox(-7.0F, -2.0F, -37.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(110, 39).mirror().addBox(7.0F, -2.0F, -36.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(110, 39).addBox(-9.0F, -2.0F, -36.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(104, 35).mirror().addBox(9.0F, -2.0F, -35.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(104, 35).addBox(-13.0F, -2.0F, -35.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition second_flap = front_wing.addOrReplaceChild("second_flap", CubeListBuilder.create().texOffs(84, 110).mirror().addBox(3.0F, -3.0F, -36.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 110).addBox(-5.0F, -3.0F, -36.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(104, 112).mirror().addBox(5.0F, -3.0F, -35.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(104, 112).addBox(-6.0F, -3.0F, -35.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(26, 105).mirror().addBox(6.0F, -3.0F, -34.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(26, 105).addBox(-9.0F, -3.0F, -34.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(38, 106).mirror().addBox(9.0F, -3.0F, -33.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(38, 106).addBox(-12.0F, -3.0F, -33.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(112, 112).mirror().addBox(12.0F, -3.0F, -32.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(112, 112).addBox(-13.0F, -3.0F, -32.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition third_flap = front_wing.addOrReplaceChild("third_flap", CubeListBuilder.create().texOffs(10, 113).mirror().addBox(3.0F, -4.0F, -34.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(10, 113).addBox(-4.0F, -4.0F, -34.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 112).mirror().addBox(4.0F, -4.0F, -33.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 112).addBox(-6.0F, -4.0F, -33.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(14, 109).mirror().addBox(6.0F, -4.0F, -32.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(14, 109).addBox(-9.0F, -4.0F, -32.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(26, 109).mirror().addBox(9.0F, -4.0F, -31.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(26, 109).addBox(-12.0F, -4.0F, -31.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(18, 113).mirror().addBox(12.0F, -4.0F, -30.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(18, 113).addBox(-13.0F, -4.0F, -30.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition fourth_flap = front_wing.addOrReplaceChild("fourth_flap", CubeListBuilder.create().texOffs(26, 113).mirror().addBox(3.0F, -5.0F, -32.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(26, 113).addBox(-4.0F, -5.0F, -32.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(94, 112).mirror().addBox(4.0F, -5.0F, -31.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(94, 112).addBox(-6.0F, -5.0F, -31.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(50, 109).mirror().addBox(6.0F, -5.0F, -30.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(50, 109).addBox(-9.0F, -5.0F, -30.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(104, 57).mirror().addBox(9.0F, -5.0F, -29.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(104, 57).addBox(-13.0F, -5.0F, -29.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition endplate = front_wing.addOrReplaceChild("endplate", CubeListBuilder.create().texOffs(84, 26).mirror().addBox(13.0F, -5.0F, -35.0F, 1.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 26).addBox(-14.0F, -5.0F, -35.0F, 1.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(40, 97).mirror().addBox(13.0F, -6.0F, -34.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(40, 97).addBox(-14.0F, -6.0F, -34.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(104, 43).mirror().addBox(13.0F, -7.0F, -32.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(104, 43).addBox(-14.0F, -7.0F, -32.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		// Check if the car is moving
		double speed = entity.getDeltaMovement().lengthSqr();

		if (speed > 0.001) {
			float rotationSpeed = ageInTicks * 0.5f; // adjust speed

			front_wheel.xRot = rotationSpeed;
			rear_wheel.xRot = rotationSpeed;
		}
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		f1_car.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}
}
