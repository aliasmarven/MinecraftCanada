package xen42.canadamod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import xen42.canadamod.CanadaMod;

public class CookingPotBlock extends BaseEntityBlock {

    public CookingPotBlock(Properties settings) {
        super(settings);
        registerDefaultState((this.stateDefinition.any()).setValue(LIT, false));
    }

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { FACING, LIT });
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT))
            return;

        var x = pos.getX() + 0.5f;
        var y = pos.getY();
        var z = pos.getZ() + 0.5f;
        if (random.nextDouble() < 0.1f) {
            world.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }

        var direction = state.getValue(FACING);
        var axis = direction.getAxis();

        double g = 0.52D;
        double h = random.nextDouble() * 0.6D - 0.3D;
        double i = (axis == Direction.Axis.X) ? (direction.getStepX() * 0.52D) : h;
        double j = random.nextDouble() * 6.0D / 16.0D;
        double k = (axis == Direction.Axis.Z) ? (direction.getStepZ() * 0.52D) : h;

        world.addParticle(ParticleTypes.SMOKE, x + i, y + j, z + k, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.FLAME, x + i, y + j, z + k, 0.0D, 0.0D, 0.0D);

        if (random.nextInt(2) == 0) {
            for (int ii = 0; ii < random.nextInt(1) + 2; ii++) {
                world.addParticle(ParticleTypes.SMOKE, x + random.nextFloat() / 2f, y + 1f, z + random.nextFloat() / 2f, 0, 0.005f, 0);
            }
        }

        if (random.nextInt(20) == 0) {
            world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y + 1f, z, 0, 0.025f, 0);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide()) {
            openScreen(world, pos, player);
        }
        return InteractionResult.SUCCESS;
    }

    public Component getTitle() {
        return Component.translatable(getDescriptionId());
    }

    public void openScreen(Level world, BlockPos pos, Player player) {
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CookingPotBlockEntity) {
            player.openMenu((MenuProvider)blockEntity);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CookingPotBlockEntity(pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(world, type, CanadaMod.COOKING_POT_ENTITY);
    }

    protected static <T extends BlockEntity> BlockEntityTicker<T> validateTicker(Level world, BlockEntityType<T> givenType, BlockEntityType<? extends CookingPotBlockEntity> expectedType) {
        if (world instanceof ServerLevel serverWorld) {
            return createTickerHelper(givenType, expectedType, (__, pos, state, blockEntity) -> CookingPotBlockEntity.tick(serverWorld, pos, state, blockEntity));
        }
        else {
            return null;
        }
    }
}
