package xen42.canadamod.sign;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import xen42.canadamod.CanadaBlocks;

public class MapleSignBlock extends StandingSignBlock {
    public static final MapCodec<StandingSignBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(WoodType.CODEC.fieldOf("wood_type").forGetter((sign) -> sign.type()), propertiesCodec())
        .apply(instance, (type, settings) -> new MapleSignBlock(settings));
    });

    public MapCodec<StandingSignBlock> codec() {
        return CODEC;
    }

    public MapleSignBlock(Properties settings) {
        super(CanadaBlocks.MAPLE_WOOD_TYPE, settings);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MapleSignEntity(pos, state);
    }
}
