package xen42.canadamod.armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MooseHatModel {

    public static LayerDefinition getModel() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition hat = modelPartData.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 37).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left_antler = hat.addOrReplaceChild("left_antler", CubeListBuilder.create().texOffs(0, 1).addBox(-8.0F, -7.0F, -6.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(43, 10).addBox(-8.0F, -10.0F, -5.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 12).addBox(-9.0F, -11.0F, -5.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(41, 1).addBox(-3.0F, -14.0F, -5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(19, 16).addBox(-5.0F, -14.0F, -5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(-7.0F, -13.0F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-4.0F, -13.0F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(21, 14).addBox(-6.0F, -12.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.0F, 5.0F));

		PartDefinition right_antler = hat.addOrReplaceChild("right_antler", CubeListBuilder.create().texOffs(0, 6).addBox(-7.0F, -12.0F, -6.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(47, 2).addBox(-5.0F, -15.0F, -5.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 2).addBox(1.0F, -16.0F, -5.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(35, 8).addBox(-3.0F, -19.0F, -5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 11).addBox(-5.0F, -19.0F, -5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 15).addBox(-1.0F, -18.0F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 18).addBox(-4.0F, -18.0F, -5.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 16).addBox(-2.0F, -17.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, 5.0F, 5.0F));
		return LayerDefinition.create(modelData, 64, 64);
    }
}
