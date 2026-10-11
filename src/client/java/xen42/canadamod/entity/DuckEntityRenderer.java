package xen42.canadamod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaModClient;
import xen42.canadamod.entities.DuckEntity;
import xen42.canadamod.entities.GooseEntity;

public class DuckEntityRenderer extends MobRenderer<DuckEntity, ChickenRenderState, DuckEntityModel<DuckEntity>> {

    public DuckEntityRenderer(Context context) {
        super(context, new DuckEntityModel(context.bakeLayer(CanadaModClient.MODEL_DUCK_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(ChickenRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/entity/duck/duck.png");
    }

    @Override
    public ChickenRenderState createRenderState() {
        return new ChickenRenderState();
    }

    @Override
    protected void scale(ChickenRenderState state, PoseStack matrices) {
        if (state.isBaby) {
            matrices.scale(0.5F, 0.5F, 0.5F);
        }
        super.scale(state, matrices);
    }

    public void extractRenderState(DuckEntity chickenEntity, ChickenRenderState chickenEntityRenderState, float f) {
		super.extractRenderState(chickenEntity, chickenEntityRenderState, f);
		chickenEntityRenderState.flap = Mth.lerp(f, chickenEntity.oFlap, chickenEntity.flap);
		chickenEntityRenderState.flapSpeed = Mth.lerp(f, chickenEntity.oFlapSpeed, chickenEntity.flapSpeed);
	}
}
