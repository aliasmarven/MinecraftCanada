package xen42.canadamod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaModClient;
import xen42.canadamod.entities.GooseEntity;

public class GooseEntityRenderer extends MobRenderer<GooseEntity, ChickenRenderState, GooseEntityModel<GooseEntity>> {

    public GooseEntityRenderer(Context context) {
        super(context, new GooseEntityModel(context.bakeLayer(CanadaModClient.MODEL_GOOSE_LAYER)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(ChickenRenderState state) {
        return Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/entity/goose/goose.png");
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

    public void extractRenderState(GooseEntity chickenEntity, ChickenRenderState chickenEntityRenderState, float f) {
		super.extractRenderState(chickenEntity, chickenEntityRenderState, f);
		chickenEntityRenderState.flap = Mth.lerp(f, chickenEntity.oFlap, chickenEntity.flap);
		chickenEntityRenderState.flapSpeed = Mth.lerp(f, chickenEntity.oFlapSpeed, chickenEntity.flapSpeed);
	}
}

