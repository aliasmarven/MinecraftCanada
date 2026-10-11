package xen42.canadamod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaModClient;
import xen42.canadamod.entities.MooseEntity;

public class MooseEntityRenderer extends MobRenderer<MooseEntity, MooseEntityRenderState, MooseEntityModel> {

    public MooseEntityRenderer(Context context) {
        super(context, new MooseEntityModel(context.bakeLayer(CanadaModClient.MODEL_MOOSE_LAYER)), 1.0f);
    }

    @Override
    public MooseEntityRenderState createRenderState() {
        return new MooseEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(MooseEntityRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/entity/moose/moose.png");
    }

    @Override
    protected void scale(MooseEntityRenderState state, PoseStack matrices) {
        if (state.isBaby) {
            matrices.scale(0.5F, 0.5F, 0.5F);
        }
        super.scale(state, matrices);
    }

    @Override
    public void extractRenderState(MooseEntity mooseEntity, MooseEntityRenderState mooseEntityRenderState, float f) {
        super.extractRenderState(mooseEntity, mooseEntityRenderState, f);
        mooseEntityRenderState.saddleStack = mooseEntity.getItemBySlot(EquipmentSlot.SADDLE).copy();
        mooseEntityRenderState.leftAntlerMissing = mooseEntity.isLeftAntlerMissing();
        mooseEntityRenderState.rightAntlerMissing = mooseEntity.isRightAntlerMissing();
        mooseEntityRenderState.dashing = mooseEntity.isDashing();

        mooseEntityRenderState.attackAnimationState.copyFrom(mooseEntity.attackAnimationState);
    }
}

