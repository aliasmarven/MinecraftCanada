package xen42.canadamod.armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BeaverHatModel {

    public static LayerDefinition getModel() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition hat = modelPartData.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 15).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.1F))
		.texOffs(0, 0).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
    }
}
