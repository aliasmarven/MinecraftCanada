package xen42.canadamod.entity;

import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import xen42.canadamod.entities.DuckEntity;

public class DuckEntityModel<T extends DuckEntity> extends ChickenModel {

    private ModelPart head;

    public DuckEntityModel(ModelPart modelPart) {
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
}
