package xen42.canadamod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.block.MooseSkullBlock;
import xen42.canadamod.block.MooseSkullBlockEntity;

public class MooseSkullBlockEntityRenderer implements BlockEntityRenderer<MooseSkullBlockEntity> {
    public MooseSkullBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(MooseSkullBlockEntity entity, float tickProgress, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {

        if (entity.getBlockState().is(CanadaBlocks.MOOSE_WALL_HEAD)) {
            return;
        }

        var rot = RotationSegment.convertToDegrees((16 - entity.getBlockState().getValue(MooseSkullBlock.ROTATION)) % 16);

        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.mulPose(Axis.YP.rotation((float)Math.toRadians(rot)));
        matrices.translate(-0.5f, -0.5f, -0.5f);

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(CanadaBlocks.MOOSE_HEAD.defaultBlockState().setValue(MooseSkullBlock.WEIRD_HACK, false),
            matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY);
    }
}