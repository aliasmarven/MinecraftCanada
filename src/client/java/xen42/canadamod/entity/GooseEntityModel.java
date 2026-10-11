package xen42.canadamod.entity;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import xen42.canadamod.entities.GooseEntity;

public class GooseEntityModel<T extends GooseEntity> extends ChickenModel {

    private ModelPart head;

    public GooseEntityModel(ModelPart modelPart) {
        super(modelPart);
        this.head = modelPart.getChild(PartNames.HEAD);
    }

    @Override
	public void setupAnim(ChickenRenderState livingEntityRenderState) {
        super.setupAnim(livingEntityRenderState);
		head.xScale = livingEntityRenderState.isBaby ? 1.5f : 1f;
		head.yScale = livingEntityRenderState.isBaby ? 1.5f : 1f;
		head.zScale = livingEntityRenderState.isBaby ? 1.5f : 1f;
	}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(14, 28).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 1.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(26, 28).addBox(0.0F, 0.0F, -3.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 1.0F));

		PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 16).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 14.0F, 0.0F));

		PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, -3.0F, 1.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-1.0F, -10.0F, -5.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 28).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(32, 8).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}
