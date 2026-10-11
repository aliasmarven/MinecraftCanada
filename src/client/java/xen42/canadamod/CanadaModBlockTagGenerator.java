package xen42.canadamod;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class CanadaModBlockTagGenerator extends BlockTagProvider {

    private static ResourceKey<Block> key(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }

    public CanadaModBlockTagGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "CanadaModBlockTagGenerator";
    }

    @Override
    protected void addTags(Provider wrapperLookup) {
        this.tag(BlockTags.LOGS)
            .add(key(CanadaBlocks.MAPLE_LOG), key(CanadaBlocks.STRIPPED_MAPLE_LOG), key(CanadaBlocks.MAPLE_WOOD), key(CanadaBlocks.STRIPPED_MAPLE_WOOD));

        this.tag(BlockTags.LOGS_THAT_BURN)
            .add(key(CanadaBlocks.MAPLE_LOG), key(CanadaBlocks.STRIPPED_MAPLE_LOG), key(CanadaBlocks.MAPLE_WOOD), key(CanadaBlocks.STRIPPED_MAPLE_WOOD));

        this.tag(CanadaTags.BlockTags.MAPLE_LOGS)
            .add(key(CanadaBlocks.MAPLE_LOG), key(CanadaBlocks.STRIPPED_MAPLE_LOG), key(CanadaBlocks.MAPLE_WOOD), key(CanadaBlocks.STRIPPED_MAPLE_WOOD));

        this.tag(BlockTags.WOODEN_BUTTONS)
            .add(key(CanadaBlocks.MAPLE_BUTTON));

        this.tag(BlockTags.WOODEN_DOORS)
            .add(key(CanadaBlocks.MAPLE_DOOR));

        this.tag(BlockTags.WOODEN_PRESSURE_PLATES)
            .add(key(CanadaBlocks.MAPLE_PRESSURE_PLATE));

        this.tag(BlockTags.WOODEN_SLABS)
            .add(key(CanadaBlocks.MAPLE_SLAB));

        this.tag(BlockTags.WOODEN_STAIRS)
            .add(key(CanadaBlocks.MAPLE_STAIRS));

        this.tag(BlockTags.WOODEN_TRAPDOORS)
            .add(key(CanadaBlocks.MAPLE_TRAPDOOR));

        this.tag(BlockTags.FENCES)
            .add(key(CanadaBlocks.MAPLE_FENCE));

        this.tag(BlockTags.FENCE_GATES)
            .add(key(CanadaBlocks.MAPLE_FENCE_GATE));

        this.tag(BlockTags.ALL_SIGNS)
            .add(key(CanadaBlocks.MAPLE_SIGN))
            .add(key(CanadaBlocks.MAPLE_WALL_SIGN));

        this.tag(BlockTags.ALL_HANGING_SIGNS)
            .add(key(CanadaBlocks.MAPLE_HANGING_SIGN))
            .add(key(CanadaBlocks.MAPLE_WALL_HANGING_SIGN));

        this.tag(BlockTags.MINEABLE_WITH_AXE)
            .add(key(CanadaBlocks.MAPLE_PLANKS))
            .add(key(CanadaBlocks.MAPLE_HANGING_SIGN))
            .add(key(CanadaBlocks.MAPLE_WALL_HANGING_SIGN))
            .add(key(CanadaBlocks.MAPLE_SIGN))
            .add(key(CanadaBlocks.MAPLE_WALL_SIGN))
            .add(key(CanadaBlocks.MAPLE_TRAPDOOR))
            .add(key(CanadaBlocks.MAPLE_FENCE));

        this.tag(BlockTags.LEAVES)
            .add(key(CanadaBlocks.MAPLE_LEAVES));

        this.tag(ConventionalBlockTags.FENCES)
            .add(key(CanadaBlocks.MAPLE_FENCE));
        this.tag(ConventionalBlockTags.WOODEN_FENCES)
            .add(key(CanadaBlocks.MAPLE_FENCE));
        this.tag(ConventionalBlockTags.FENCE_GATES)
            .add(key(CanadaBlocks.MAPLE_FENCE_GATE));
        this.tag(ConventionalBlockTags.WOODEN_FENCE_GATES)
            .add(key(CanadaBlocks.MAPLE_FENCE_GATE));
        this.tag(ConventionalBlockTags.STRIPPED_LOGS)
            .add(key(CanadaBlocks.STRIPPED_MAPLE_LOG));
        this.tag(ConventionalBlockTags.STRIPPED_WOODS)
            .add(key(CanadaBlocks.STRIPPED_MAPLE_WOOD));

        this.tag(BlockTags.SAPLINGS).add(key(CanadaBlocks.MAPLE_SAPLING));
        this.tag(BlockTags.FLOWER_POTS).add(key(CanadaBlocks.POTTED_MAPLE_SAPLING));

        this.tag(ConventionalBlockTags.VILLAGER_JOB_SITES).add(key(CanadaBlocks.TREE_TAP));
    }
}