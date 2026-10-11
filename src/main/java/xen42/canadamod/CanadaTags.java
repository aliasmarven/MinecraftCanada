package xen42.canadamod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class CanadaTags {
	public class BlockTags {
		public static final TagKey<Block> MAPLE_LOGS = ofBlock("maple_logs");
	}

	public class ItemTags {
        public static final TagKey<Item> MAPLE_LOGS = ofItem("maple_logs");

        public static final TagKey<Item> RAW_MEAT = ofItemConventional("foods/raw_meat");
        public static final TagKey<Item> SAP = ofItemConventional("sap");
        public static final TagKey<Item> RUBBER = ofItemConventional("rubber");
        public static final TagKey<Item> CHEESE = ofItemConventional("cheese");
        public static final TagKey<Item> FLOUR = ofItemConventional("flour");

        public static final TagKey<Item> FOODS = ofItemConventional("foods");
	}

	public class StructureTags {
		public static final TagKey<Structure> MAPLE_CABIN = ofStructure("maple_cabin");
	}

	public static TagKey<Block> ofBlock(String name) {
		return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, name));
	}

	public static TagKey<Item> ofItem(String name) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, name));
	}

	public static TagKey<Item> ofItemConventional(String name) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
	}

	public static TagKey<EntityType<?>> ofEntity(String name) {
		return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, name));
	}

	public static TagKey<Structure> ofStructure(String name) {
		return TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, name));
	}
}