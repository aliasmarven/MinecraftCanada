package xen42.canadamod.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import xen42.canadamod.CanadaBlocks;

public class MapleSignEntity extends SignBlockEntity {
    public MapleSignEntity(BlockPos blockPos, BlockState blockState) {
        super(CanadaBlocks.MAPLE_SIGN_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return CanadaBlocks.MAPLE_SIGN_BLOCK_ENTITY.isValid(state);
    }
}
