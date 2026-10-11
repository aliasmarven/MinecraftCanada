package xen42.canadamod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.CanadaItems;

public class TreeTapBlock extends HorizontalDirectionalBlock {
    public static int MAX_SAP = 4;
    public static IntegerProperty SAP_LEVEL = IntegerProperty.create("sap_level", 0, MAX_SAP);

    public final VoxelShape northShape;
    public final VoxelShape southShape;
    public final VoxelShape eastShape;
    public final VoxelShape westShape;

    public TreeTapBlock(Properties settings) {
        super(settings);
        registerDefaultState((this.stateDefinition.any()).setValue(SAP_LEVEL, 0));

        var width = 12;
        this.southShape = Block.box(0, 0, 0, 16f, 16f, width);
        this.northShape = Block.box(0, 0, 16 - width, 16f, 16f, 16f);
        this.eastShape = Block.box(0, 0, 0, width, 16f, 16f);
        this.westShape = Block.box(16 - width, 0, 0, 16f, 16f, 16f);
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return northShape;
            case SOUTH:
                return southShape;
            case EAST:
                return eastShape;
            case WEST:
                return westShape;
            default:
                break;
        }
        return westShape;
    }

    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        var direction = state.getValue(FACING);
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return false;
        }
        else {
            var blockPos = pos.relative(direction.getOpposite());
            return world.getBlockState(blockPos).isFaceSturdy((BlockGetter)world, blockPos, direction);
        }
    }

    public Item getReturnItem(BlockState state, Level world, BlockPos pos) {
        var tappedBlock = world.getBlockState(pos.offset(state.getValue(FACING).getOpposite().getUnitVec3i()));

        if (tappedBlock.is(Blocks.PALE_OAK_LOG)) {
            return Items.RESIN_CLUMP;
        }
        else if (tappedBlock.is(CanadaBlocks.MAPLE_LOG)) {
            return CanadaItems.MAPLE_SAP;
        }
        else if (tappedBlock.is(BlockTags.LOGS)) {
            return CanadaItems.SAP;
        }
        else {
            return null;
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        var pair = getReturnItem(state, world, pos);

        if (pair != null && !isFull(state)) {
            world.setBlockAndUpdate(pos, state.setValue(SAP_LEVEL, state.getValue(SAP_LEVEL) + 1));
            world.playPlayerSound(SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON, SoundSource.BLOCKS, 1f, 1f);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        }
    }

    public boolean isFull(BlockState state) {
        return state.getValue(SAP_LEVEL) == MAX_SAP;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !isFull(state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        var returnItem = getReturnItem(state, world, pos);

        if (returnItem == null) {
            return InteractionResult.FAIL;
        }

        if(state.getValue(SAP_LEVEL) > 0) {
            world.setBlockAndUpdate(pos, state.setValue(SAP_LEVEL, state.getValue(SAP_LEVEL) - 1));
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

            player.handleExtraItemsCreatedOnUse(new ItemStack(returnItem));

            return InteractionResult.CONSUME;
        }
        else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { SAP_LEVEL, FACING });
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos,
                                        Direction direction) {
        return state.getValue(SAP_LEVEL);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == (state.getValue(FACING)).getOpposite() && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else {
            return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var dir = ctx.getClickedFace();
        // Should never happen but want to avoid a crash
        if (dir == Direction.UP || dir == Direction.DOWN) {
            dir = Direction.NORTH;
        }
        return defaultBlockState().setValue(SAP_LEVEL, 0).setValue(FACING, dir);
    }
}
