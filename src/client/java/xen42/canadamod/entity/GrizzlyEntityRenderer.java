package xen42.canadamod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.PolarBearRenderState;
import net.minecraft.resources.Identifier;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaModClient;
import xen42.canadamod.entities.GrizzlyEntity;

public class GrizzlyEntityRenderer extends MobRenderer<GrizzlyEntity, PolarBearRenderState, GrizzlyEntityModel<GrizzlyEntity>> {

    public GrizzlyEntityRenderer(Context context) {
        super(context, new GrizzlyEntityModel(context.bakeLayer(CanadaModClient.MODEL_GRIZZLY_LAYER)), 1f);
    }

    @Override
    public Identifier getTextureLocation(PolarBearRenderState state) {
        return Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/entity/grizzly/grizzly.png");
    }

    @Override
    public PolarBearRenderState createRenderState() {
        return new PolarBearRenderState();
    }

    @Override
    protected void scale(PolarBearRenderState state, PoseStack matrices) {
        if (state.isBaby) {
            matrices.scale(0.5F, 0.5F, 0.5F);
        }
        super.scale(state, matrices);
    }

}
