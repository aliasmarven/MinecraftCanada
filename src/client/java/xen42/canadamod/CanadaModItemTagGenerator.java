package xen42.canadamod;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.ItemTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class CanadaModItemTagGenerator extends ItemTagProvider {

    private static ResourceKey<Item> key(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }

	public CanadaModItemTagGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture,
			CanadaModBlockTagGenerator blockTagProvider) {
		super(output, registriesFuture, blockTagProvider);
	}

	@Override
	public String getName() {
		return "CanadaModItemTagGenerator";
	}

	@Override
	protected void addTags(Provider wrapperLookup) {
        this.tag(CanadaTags.ItemTags.MAPLE_LOGS)
            .add(key(CanadaBlocks.MAPLE_LOG.asItem()), key(CanadaBlocks.STRIPPED_MAPLE_LOG.asItem()),
				key(CanadaBlocks.MAPLE_WOOD.asItem()), key(CanadaBlocks.STRIPPED_MAPLE_WOOD.asItem()));

		this.tag(ItemTags.SAPLINGS).add(key(CanadaBlocks.MAPLE_SAPLING.asItem()));

		this.tag(ItemTags.CHEST_BOATS).add(key(CanadaItems.MAPLE_CHEST_BOAT));
		this.tag(ItemTags.BOATS).add(key(CanadaItems.MAPLE_BOAT));

		this.tag(ItemTags.PLANKS).add(key(CanadaBlocks.MAPLE_PLANKS.asItem()));
		this.tag(ItemTags.LEAVES).add(key(CanadaBlocks.MAPLE_LEAVES.asItem()));
		this.tag(ItemTags.LOGS).add(key(CanadaBlocks.MAPLE_LOG.asItem()),
			key(CanadaBlocks.MAPLE_WOOD.asItem()), key(CanadaBlocks.STRIPPED_MAPLE_LOG.asItem()), key(CanadaBlocks.STRIPPED_MAPLE_WOOD.asItem()));
		this.tag(ItemTags.LOGS_THAT_BURN).add(key(CanadaBlocks.MAPLE_LOG.asItem()),
			key(CanadaBlocks.MAPLE_WOOD.asItem()), key(CanadaBlocks.STRIPPED_MAPLE_LOG.asItem()), key(CanadaBlocks.STRIPPED_MAPLE_WOOD.asItem()));
		this.tag(ItemTags.WOODEN_BUTTONS).add(key(CanadaBlocks.MAPLE_BUTTON.asItem()));
		this.tag(ItemTags.WOODEN_DOORS).add(key(CanadaBlocks.MAPLE_DOOR.asItem()));
		this.tag(ItemTags.WOODEN_FENCES).add(key(CanadaBlocks.MAPLE_FENCE.asItem()));
		this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(key(CanadaBlocks.MAPLE_PRESSURE_PLATE.asItem()));
		this.tag(ItemTags.WOODEN_SLABS).add(key(CanadaBlocks.MAPLE_SLAB.asItem()));
		this.tag(ItemTags.WOODEN_STAIRS).add(key(CanadaBlocks.MAPLE_STAIRS.asItem()));
		this.tag(ItemTags.WOODEN_TRAPDOORS).add(key(CanadaBlocks.MAPLE_TRAPDOOR.asItem()));

		this.tag(CanadaTags.ItemTags.RAW_MEAT).add(key(CanadaItems.VENISON), key(CanadaItems.WATERFOWL));
		this.tag(CanadaTags.ItemTags.SAP).add(key(CanadaItems.SAP), key(CanadaItems.MAPLE_SAP));
		this.tag(CanadaTags.ItemTags.RUBBER).add(key(CanadaItems.RUBBER));
		this.tag(CanadaTags.ItemTags.CHEESE).add(key(CanadaItems.CHEESE_CURD));
		this.tag(CanadaTags.ItemTags.FLOUR).add(key(CanadaItems.FLOUR));
		this.tag(CanadaTags.ItemTags.FOODS)
			.add(key(CanadaItems.COOKED_VENISON), key(CanadaItems.DONAIR), key(CanadaItems.PIEROGI), key(CanadaItems.GRAVY),
				key(CanadaItems.CHEESE_CURD), key(CanadaItems.POUTINE), key(CanadaItems.MAPLE_SYRUP_BOTTLE), key(CanadaItems.COOKED_WATERFOWL), key(CanadaItems.BUTTER_TART));

		this.tag(ItemTags.HEAD_ARMOR).add(key(CanadaItems.BEAVER_HELMET), key(CanadaItems.MOOSE_HELMET));
		this.tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(key(CanadaItems.BEAVER_HELMET), key(CanadaItems.MOOSE_HELMET));

		this.tag(ItemTags.SKULLS).add(key(CanadaItems.MOOSE_HEAD));

		this.tag(ItemTags.EGGS).add(key(CanadaItems.DUCK_EGG), key(CanadaItems.GOOSE_EGG));

	}
}