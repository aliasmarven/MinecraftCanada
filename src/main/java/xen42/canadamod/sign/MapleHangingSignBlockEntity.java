package xen42.canadamod.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import xen42.canadamod.CanadaBlocks;

public class MapleHangingSignBlockEntity extends HangingSignBlockEntity {
    public MapleHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
        this.type = CanadaBlocks.MAPLE_HANGING_SIGN_BLOCK_ENTITY;
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return CanadaBlocks.MAPLE_HANGING_SIGN_BLOCK_ENTITY.isValid(state);
    }
}
