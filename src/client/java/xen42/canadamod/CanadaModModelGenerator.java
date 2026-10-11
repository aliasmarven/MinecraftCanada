package xen42.canadamod;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.BlockModelGenerators.PlantType;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class CanadaModModelGenerator extends FabricModelProvider {
    public CanadaModModelGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(CanadaItems.PELT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.FLOUR, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.MAPLE_BOAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.MAPLE_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.MAPLE_SAPLING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.TREE_TAP, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.MAPLE_SYRUP_BOTTLE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.SAP, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.MAPLE_SAP, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.RUBBER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.GRAVY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.CHEESE_CURD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.POUTINE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.BEAVER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.MOOSE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.DUCK_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.GRIZZLY_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.GOOSE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.VENISON, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.COOKED_VENISON, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.WATERFOWL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.COOKED_WATERFOWL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.ANTLERS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.THERMOS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.PIEROGI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.BUTTER_TART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.DONAIR, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.DUCK_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(CanadaItems.GOOSE_EGG, ModelTemplates.FLAT_ITEM);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.family(CanadaBlocks.MAPLE_PLANKS).generateFor(CanadaBlocks.MAPLE);
        blockStateModelGenerator.woodProvider(CanadaBlocks.MAPLE_LOG).log(CanadaBlocks.MAPLE_LOG).wood(CanadaBlocks.MAPLE_WOOD);
        blockStateModelGenerator.woodProvider(CanadaBlocks.STRIPPED_MAPLE_LOG).log(CanadaBlocks.STRIPPED_MAPLE_LOG).wood(CanadaBlocks.STRIPPED_MAPLE_WOOD);
        blockStateModelGenerator.createTrivialCube(CanadaBlocks.MAPLE_LEAVES);
        // Maple sign models and all orientations are supplied as 26.3 JSON resources in src/main/generated/assets/canadamod.
        blockStateModelGenerator.createPlant(CanadaBlocks.MAPLE_SAPLING, CanadaBlocks.POTTED_MAPLE_SAPLING, PlantType.NOT_TINTED);

        blockStateModelGenerator.createTrivialCube(CanadaBlocks.RUBBER_BLOCK);
    }

    @Override
    public String getName() {
        return "CanadaModModelGenerator";
    }
}
