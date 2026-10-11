package xen42.canadamod.entity;

import net.minecraft.client.model.animal.polarbear.PolarBearModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.PolarBearRenderState;
import xen42.canadamod.entities.GrizzlyEntity;

public class GrizzlyEntityModel<T extends GrizzlyEntity> extends PolarBearModel {

    public GrizzlyEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
	public void setupAnim(PolarBearRenderState livingEntityRenderState) {
        super.setupAnim(livingEntityRenderState);
		head.xScale = livingEntityRenderState.isBaby ? 1.2f : 1f;
		head.yScale = livingEntityRenderState.isBaby ? 1.2f : 1f;
		head.zScale = livingEntityRenderState.isBaby ? 1.2f : 1f;
	}

    public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild(
			PartNames.HEAD,
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-3.5F, -3.0F, -3.0F, 7.0F, 7.0F, 7.0F)
				.texOffs(0, 44)
				.addBox(PartNames.MOUTH, -2.5F, 0.0F, -5.0F, 5.0F, 4.0F, 2.0F)
				.texOffs(26, 0)
				.addBox(PartNames.RIGHT_EAR, -4.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F)
				.texOffs(26, 0)
				.mirror()
				.addBox(PartNames.LEFT_EAR, 2.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F),
			PartPose.offset(0.0F, 10.0F, -16.0F)
		);
		modelPartData.addOrReplaceChild(
			PartNames.BODY,
			CubeListBuilder.create().texOffs(0, 19).addBox(-5.0F, -13.0F, -7.0F, 14.0F, 14.0F, 11.0F).texOffs(39, 0).addBox(-4.0F, -25.0F, -7.0F, 12.0F, 12.0F, 10.0F),
			PartPose.offsetAndRotation(-2.0F, 9.0F, 12.0F, (float) (Math.PI / 2), 0.0F, 0.0F)
		);
		int i = 10;
		CubeListBuilder modelPartBuilder = CubeListBuilder.create().texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F);
		modelPartData.addOrReplaceChild(PartNames.RIGHT_HIND_LEG, modelPartBuilder, PartPose.offset(-4.5F, 14.0F, 6.0F));
		modelPartData.addOrReplaceChild(PartNames.LEFT_HIND_LEG, modelPartBuilder, PartPose.offset(4.5F, 14.0F, 6.0F));
		CubeListBuilder modelPartBuilder2 = CubeListBuilder.create().texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F);
		modelPartData.addOrReplaceChild(PartNames.RIGHT_FRONT_LEG, modelPartBuilder2, PartPose.offset(-3.5F, 14.0F, -8.0F));
		modelPartData.addOrReplaceChild(PartNames.LEFT_FRONT_LEG, modelPartBuilder2, PartPose.offset(3.5F, 14.0F, -8.0F));
		return LayerDefinition.create(modelData, 128, 64).apply(MeshTransformer.IDENTITY).apply(MeshTransformer.scaling(1.2F));
	}

}
