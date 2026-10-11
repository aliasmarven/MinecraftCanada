package xen42.canadamod.sign;

import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.LootParams;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.CanadaItems;

public class MapleWallSignBlock extends WallSignBlock {




    public MapleWallSignBlock(Properties settings) {
        super(CanadaBlocks.MAPLE_WOOD_TYPE, settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MapleSignEntity(pos, state);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Loot table stuff is weird and doesn't work, have to do this
        return Collections.singletonList(new ItemStack(CanadaItems.MAPLE_SIGN_ITEM));
    }
}
