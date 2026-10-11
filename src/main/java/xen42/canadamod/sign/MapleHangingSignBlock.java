package xen42.canadamod.sign;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import xen42.canadamod.CanadaBlocks;

public class MapleHangingSignBlock extends CeilingHangingSignBlock {
    public static final MapCodec<CeilingHangingSignBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(WoodType.CODEC.fieldOf("wood_type").forGetter((sign) -> sign.type()), propertiesCodec())
        .apply(instance, (type, settings) -> new MapleHangingSignBlock(settings));
    });

    public MapCodec<CeilingHangingSignBlock> codec() {
        return CODEC;
    }

    public MapleHangingSignBlock(Properties settings) {
        super(CanadaBlocks.MAPLE_WOOD_TYPE, settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MapleHangingSignBlockEntity(pos, state);
    }
}
