package xen42.canadamod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RubberBlock extends SlimeBlock {

    public RubberBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        // Remove the slowing effect that slime blocks have by default while keeping bounciness
    }
}
