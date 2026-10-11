package xen42.canadamod.sign;

import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.LootParams;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.CanadaItems;

public class MapleWallHangingSignBlock extends WallHangingSignBlock {
   public static final MapCodec<WallHangingSignBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
      return instance.group(WoodType.CODEC.fieldOf("wood_type").forGetter((sign) -> CanadaBlocks.MAPLE_WOOD_TYPE), propertiesCodec())
        .apply(instance, (type, settings) -> new MapleWallHangingSignBlock(settings));
   });

   public MapCodec<WallHangingSignBlock> codec() {
      return CODEC;
   }

    public MapleWallHangingSignBlock(Properties settings) {
        super(CanadaBlocks.MAPLE_WOOD_TYPE, settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MapleHangingSignBlockEntity(pos, state);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Loot table stuff is weird and doesn't work, have to do this
        return Collections.singletonList(new ItemStack(CanadaItems.MAPLE_HANGING_SIGN_ITEM));
    }
}
