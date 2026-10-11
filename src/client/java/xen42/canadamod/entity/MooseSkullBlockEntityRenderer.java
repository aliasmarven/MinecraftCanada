package xen42.canadamod.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.block.MooseSkullBlock;
import xen42.canadamod.block.MooseSkullBlockEntity;

public class MooseSkullBlockEntityRenderer implements
        BlockEntityRenderer<MooseSkullBlockEntity,
                            MooseSkullBlockEntityRenderer.MooseSkullRenderState> {

    private final BlockModelResolver modelResolver;

    public MooseSkullBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.modelResolver = new BlockModelResolver(
            Minecraft.getInstance().getModelManager()
        );
    }

    public static class MooseSkullRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState model = new BlockModelRenderState();
        public float rotationDegrees;
        public boolean visible;
    }

    @Override
    public MooseSkullRenderState createRenderState() {
        return new MooseSkullRenderState();
    }

    @Override
    public void extractRenderState(
            MooseSkullBlockEntity entity,
            MooseSkullRenderState state,
            float partialTick,
            Vec3 cameraPosition,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {

        BlockEntityRenderState.extractBase(entity, state, crumblingOverlay);

        var blockState = entity.getBlockState();

        state.visible = !blockState.is(CanadaBlocks.MOOSE_WALL_HEAD);

        if (!state.visible) {
            state.model.clear();
            return;
        }

        int rotation = blockState.getValue(MooseSkullBlock.ROTATION);

        state.rotationDegrees = RotationSegment.convertToDegrees(
            (16 - rotation) % 16
        );

        var renderBlockState = CanadaBlocks.MOOSE_HEAD
            .defaultBlockState()
            .setValue(MooseSkullBlock.WEIRD_HACK, false);

        this.modelResolver.update(
            state.model,
            renderBlockState,
            BlockDisplayContext.create()
        );
    }

    @Override
    public void submit(
            MooseSkullRenderState state,
            PoseStack matrices,
            SubmitNodeCollector collector,
            CameraRenderState cameraState) {

        if (!state.visible) {
            return;
        }

        matrices.pushPose();

        matrices.translate(0.5F, 0.5F, 0.5F);

        matrices.mulPose(
            new Matrix4f().rotationY(
                (float) Math.toRadians(state.rotationDegrees)
            )
        );

        matrices.translate(-0.5F, -0.5F, -0.5F);

        state.model.submit(
            matrices,
            collector,
            state.lightCoords,
            OverlayTexture.NO_OVERLAY,
            -1
        );

        matrices.popPose();
    }
}
