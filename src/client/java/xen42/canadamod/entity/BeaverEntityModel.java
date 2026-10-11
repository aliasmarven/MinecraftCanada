package xen42.canadamod.entity;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class BeaverEntityModel extends QuadrupedModel<BeaverEntityRenderState> {
	private ModelPart tail;

	public BeaverEntityModel(ModelPart root) {
								super(root);
							this.tail = root.getChild("tail");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(28, 21).addBox(-2.0F, -1.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 31).addBox(-1.0F, 1.0F, -4.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(2.0F, -4.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(-3.0F, -4.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, -5.0F));

		PartDefinition tail = modelPartData.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 17).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 6.0F));

		PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leg_front_left = modelPartData.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(28, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 21.0F, -3.0F));

		PartDefinition leg_front_right = modelPartData.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(28, 25).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 21.0F, -3.0F));

		PartDefinition leg_hind_left = modelPartData.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(28, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 21.0F, 5.0F));

		PartDefinition leg_hind_right = modelPartData.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(28, 25).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 21.0F, 5.0F));

		return LayerDefinition.create(modelData, 64, 64);
	}

	@Override
	public void setupAnim(BeaverEntityRenderState livingEntityRenderState) {
		this.resetPose();
		super.setupAnim(livingEntityRenderState);

		this.animate(livingEntityRenderState.chopAnimationState, BeaverEntityAnimation.CHOP, livingEntityRenderState.ageInTicks, 1f);

		if (livingEntityRenderState.isFrenzied) {
			this.tail.xRot = 30 / Mth.RAD_TO_DEG;
		}
		else if (livingEntityRenderState.isFatigued) {
			this.tail.xRot = -20 / Mth.RAD_TO_DEG;
		}

		float f = livingEntityRenderState.walkAnimationPos;
		float g = livingEntityRenderState.walkAnimationSpeed;
		this.tail.xRot += Mth.cos(f * 0.6662F) * 0.2F * g;

		head.xScale = livingEntityRenderState.isBaby ? 1.5f : 1f;
		head.yScale = livingEntityRenderState.isBaby ? 1.5f : 1f;
		head.zScale = livingEntityRenderState.isBaby ? 1.5f : 1f;
	}
}
