package xen42.canadamod;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup.Provider;

public class CanadaModBlockLootTableGenerator extends FabricBlockLootSubProvider {

    protected CanadaModBlockLootTableGenerator(FabricPackOutput dataOutput,
            CompletableFuture<Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(CanadaBlocks.MAPLE_BUTTON);
        add(CanadaBlocks.MAPLE_DOOR, block -> createDoorTable(block));
        dropSelf(CanadaBlocks.MAPLE_FENCE);
        dropSelf(CanadaBlocks.MAPLE_FENCE_GATE);
        dropSelf(CanadaBlocks.MAPLE_LOG);
        dropSelf(CanadaBlocks.MAPLE_PLANKS);
        dropSelf(CanadaBlocks.MAPLE_PRESSURE_PLATE);
        dropSelf(CanadaBlocks.MAPLE_SAPLING);
        dropSelf(CanadaBlocks.MAPLE_STAIRS);
        add(CanadaBlocks.MAPLE_SLAB, block -> createSlabItemTable(block));
        dropSelf(CanadaBlocks.MAPLE_TRAPDOOR);

        add(CanadaBlocks.MAPLE_HANGING_SIGN, block -> createSingleItemTable(CanadaItems.MAPLE_HANGING_SIGN_ITEM));
        add(CanadaBlocks.MAPLE_SIGN, block -> createSingleItemTable(CanadaItems.MAPLE_SIGN_ITEM));
        // for some reason these lines make oak signs drop maple?
        // addDrop(CanadaBlocks.MAPLE_WALL_HANGING_SIGN, block -> createSingleItemTable(CanadaItems.MAPLE_HANGING_SIGN_ITEM));
        // addDrop(CanadaBlocks.MAPLE_WALL_SIGN, block -> createSingleItemTable(CanadaItems.MAPLE_SIGN_ITEM));

        dropSelf(CanadaBlocks.MAPLE_WOOD);
        add(CanadaBlocks.MAPLE_LEAVES, block -> createLeavesDrops(block, CanadaBlocks.MAPLE_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(CanadaBlocks.STRIPPED_MAPLE_LOG);
        dropSelf(CanadaBlocks.STRIPPED_MAPLE_WOOD);
        add(CanadaBlocks.TREE_TAP, block -> createSingleItemTable(CanadaItems.TREE_TAP));
        dropSelf(CanadaBlocks.COOKING_POT);
        dropSelf(CanadaBlocks.RUBBER_BLOCK);
        add(CanadaBlocks.POTTED_MAPLE_SAPLING, block -> createPotFlowerItemTable(CanadaItems.MAPLE_SAPLING));
        dropOther(CanadaBlocks.MOOSE_HEAD, CanadaItems.MOOSE_HEAD);
        dropOther(CanadaBlocks.MOOSE_WALL_HEAD, CanadaItems.MOOSE_HEAD);
    }
}
