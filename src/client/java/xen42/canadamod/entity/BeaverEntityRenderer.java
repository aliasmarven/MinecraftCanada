package xen42.canadamod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaModClient;
import xen42.canadamod.entities.BeaverEntity;

public class BeaverEntityRenderer extends MobRenderer<BeaverEntity, BeaverEntityRenderState, BeaverEntityModel> {

    public BeaverEntityRenderer(Context context) {
        super(context, new BeaverEntityModel(context.bakeLayer(CanadaModClient.MODEL_BEAVER_LAYER)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(BeaverEntityRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/entity/beaver/beaver.png");
    }

    @Override
    public BeaverEntityRenderState createRenderState() {
        return new BeaverEntityRenderState();
    }

    @Override
    protected void scale(BeaverEntityRenderState state, PoseStack matrices) {
        if (state.isBaby) {
            matrices.scale(0.5F, 0.5F, 0.5F);
        }
        super.scale(state, matrices);
    }

    @Override
    public void extractRenderState(BeaverEntity beaverEntity, BeaverEntityRenderState beaverEntityRenderState, float f) {
        super.extractRenderState(beaverEntity, beaverEntityRenderState, f);

        beaverEntityRenderState.chopAnimationState.copyFrom(beaverEntity.chopAnimationState);
        beaverEntityRenderState.isFrenzied = beaverEntity.isFrenzied();
        beaverEntityRenderState.isFatigued = beaverEntity.isFatigued();
    }
}
