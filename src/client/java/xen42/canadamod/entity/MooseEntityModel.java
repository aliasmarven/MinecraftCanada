package xen42.canadamod.entity;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MooseEntityModel extends QuadrupedModel<MooseEntityRenderState> {
	private ModelPart leftAntler, rightAntler, saddle, harness;
    protected MooseEntityModel(ModelPart root) {
        super(root);
		this.leftAntler = head.getChild("oops_the_head_is_backwards").getChild("left_antler");
		this.rightAntler = head.getChild("oops_the_head_is_backwards").getChild("right_antler");
		this.harness = head.getChild("oops_the_head_is_backwards").getChild("harness");
		this.saddle = body.getChild("saddle");
    }

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -15.0F));

		PartDefinition oops_the_head_is_backwards = head.addOrReplaceChild("oops_the_head_is_backwards", CubeListBuilder.create().texOffs(84, 24).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(30, 109).addBox(-5.0F, -7.924F, -9.8682F, 10.0F, 8.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(36, 95).addBox(-3.0F, -5.924F, 1.1318F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(57, 46).addBox(-4.0F, -9.924F, -4.8682F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(57, 46).addBox(2.0F, -9.924F, -4.8682F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, -10.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition left_antler = oops_the_head_is_backwards.addOrReplaceChild("left_antler", CubeListBuilder.create().texOffs(0, 13).addBox(-8.0F, -7.0F, -4.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 19).addBox(-8.0F, -10.0F, -5.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(25, 30).addBox(-9.0F, -11.0F, -5.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(-3.0F, -14.0F, -5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 30).addBox(-5.0F, -14.0F, -5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(-7.0F, -13.0F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-4.0F, -13.0F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(21, 14).addBox(-6.0F, -12.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 1.0F));

		PartDefinition right_antler = oops_the_head_is_backwards.addOrReplaceChild("right_antler", CubeListBuilder.create().texOffs(0, 13).addBox(-7.0F, -12.0F, -4.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 19).addBox(-5.0F, -15.0F, -5.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(1.0F, -16.0F, -5.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(35, 15).addBox(-3.0F, -19.0F, -5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(49, 29).addBox(-5.0F, -19.0F, -5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 22).addBox(-1.0F, -18.0F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 31).addBox(-4.0F, -18.0F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 23).addBox(-2.0F, -17.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, 5.0F, 1.0F));

		PartDefinition harness = oops_the_head_is_backwards.addOrReplaceChild("harness", CubeListBuilder.create().texOffs(156, 35).addBox(-5.0F, -6.0F, -8.0F, 10.0F, 8.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(148, 16).addBox(-5.0F, -2.0F, -8.0F, 10.0F, 1.0F, 8.0F, new CubeDeformation(0.14F))
		.texOffs(164, 2).addBox(-3.0F, -2.0F, -1.0F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.14F))
		.texOffs(134, 5).addBox(-3.0F, -4.0F, 3.0F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -2.0F, 1.0F));

		PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 56).addBox(-9.0F, -20.0F, -1.0F, 18.0F, 20.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(86, 49).addBox(-7.0F, -18.0F, 10.0F, 14.0F, 16.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(62, 69).addBox(-8.0F, -17.0F, -18.0F, 16.0F, 17.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -4.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition saddle = body.addOrReplaceChild("saddle", CubeListBuilder.create().texOffs(139, 66).addBox(-8.0F, -38.0F, -12.0F, 16.0F, 2.0F, 11.0F, new CubeDeformation(0.1F))
		.texOffs(146, 83).addBox(-8.0F, -36.0F, -8.0F, 16.0F, 8.0F, 3.0F, new CubeDeformation(0.09F)), PartPose.offset(0.0F, 21.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(32, 48).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, -18.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition left_front_leg = modelPartData.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 102).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 21.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.5F, 3.0F, -10.5F));

		PartDefinition right_front_leg = modelPartData.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 102).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 21.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 3.0F, -10.5F));

		PartDefinition left_hind_leg = modelPartData.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 102).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 21.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.5F, 3.0F, 10.5F));

		PartDefinition right_hind_leg = modelPartData.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 102).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 21.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 3.0F, 10.5F));
		return LayerDefinition.create(modelData, 192, 128);
	}

	@Override
	public void setupAnim(MooseEntityRenderState livingEntityRenderState) {
		super.setupAnim(livingEntityRenderState);

		this.animate(livingEntityRenderState.attackAnimationState, MooseEntityAnimation.ATTACK, livingEntityRenderState.ageInTicks, 1f);

		leftAntler.skipDraw = livingEntityRenderState.isBaby || livingEntityRenderState.leftAntlerMissing;
		rightAntler.skipDraw = livingEntityRenderState.isBaby || livingEntityRenderState.rightAntlerMissing;

		saddle.skipDraw = livingEntityRenderState.saddleStack.isEmpty();
		harness.skipDraw = livingEntityRenderState.saddleStack.isEmpty();

		head.xScale = livingEntityRenderState.isBaby ? 1.5f : 1f;
		head.yScale = livingEntityRenderState.isBaby ? 1.5f : 1f;
		head.zScale = livingEntityRenderState.isBaby ? 1.5f : 1f;

		head.xRot = livingEntityRenderState.dashing ? (float)Math.toRadians(20f) : 0f;
	}
}
