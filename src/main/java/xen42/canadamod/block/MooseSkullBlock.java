package xen42.canadamod.block;


import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MooseSkullBlock extends BaseEntityBlock {
    public static final MapCodec<MooseSkullBlock> CODEC = simpleCodec(MooseSkullBlock::new);

    private static final int MAX_ROTATIONS = RotationSegment.getMaxSegmentIndex() + 1;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    // So the weird hack is on by default and makes the block not render
    // This is because the block renders only with a BlockEntityRenderer or with a ArmorEntityRenderer for when its on your head
    // Since these render with the rendering of the block itself it has to be different in the two cases
    // Idk man
    public static final BooleanProperty WEIRD_HACK = BooleanProperty.create("weird_hack");

    public MooseSkullBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(ROTATION, 0).setValue(WEIRD_HACK, true));
    }

    protected VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.empty();
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(ROTATION, RotationSegment.convertToSegment(ctx.getRotation()));
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ROTATION, rotation.rotate((Integer)state.getValue(ROTATION), MAX_ROTATIONS));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror((Integer)state.getValue(ROTATION), MAX_ROTATIONS));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MooseSkullBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Block.column(12.0, 0.0, 8.0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ROTATION_16, WEIRD_HACK);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return state.getValue(WEIRD_HACK) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }
}
