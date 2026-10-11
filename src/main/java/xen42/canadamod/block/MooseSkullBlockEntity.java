package xen42.canadamod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import xen42.canadamod.CanadaMod;

public class MooseSkullBlockEntity extends BlockEntity {

    public MooseSkullBlockEntity(BlockPos pos, BlockState state) {
        super(CanadaMod.MOOSE_HEAD_ENTITY, pos, state);
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return CanadaMod.MOOSE_HEAD_ENTITY.isValid(state);
    }
}
