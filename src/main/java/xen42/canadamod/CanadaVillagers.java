package xen42.canadamod;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.npc.VillagerTrades.TreasureMapForEmeralds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

public class CanadaVillagers {
				public static final ResourceKey<PoiType> LUMBERJACK_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "lumberjack_poi"));
				public static final PoiType LUMBERJACK_POI = registerNewPOI("lumberjack_poi", CanadaBlocks.TREE_TAP);

				public static final ResourceKey<VillagerProfession> SUGAR_SHACK_VILLAGER_KEY = ResourceKey.create(Registries.VILLAGER_PROFESSION, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "sugar_shack"));
				public static final VillagerProfession SUGAR_SHACK_VILLAGER = registerNewProfession(SUGAR_SHACK_VILLAGER_KEY, LUMBERJACK_POI_KEY, SoundEvents.VILLAGER_WORK_BUTCHER);

				public static VillagerProfession registerNewProfession(ResourceKey<VillagerProfession> key, ResourceKey<PoiType> poi, SoundEvent sound) {
								var profession = new VillagerProfession(
																Component.translatable("entity." + key.location().getNamespace() + ".villager." + key.location().getPath()),
																entry -> entry.is(poi),
																entry -> entry.is(poi),
																ImmutableSet.of(),
																ImmutableSet.of(),
																sound
												);
								return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, key.location(), profession);
				}

				public static PoiType registerNewPOI(String name, Block block) {
								return PointOfInterestHelper.register(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, name), 1, 1, block);
				}

				private static MerchantOffer SellMap(Entity entity, RandomSource random, int price, TagKey<Structure> structure, Holder<MapDecorationType> decoration, int maxUses, int experience) {
					return new TreasureMapForEmeralds(price, structure,
						"filled_map." + structure.location().getNamespace() + "." + structure.location().getPath(),
						decoration, maxUses, experience).getOffer(entity, random);
				}

				public static void initialize() {
								// CUSTOM VILLAGER
								TradeOfferHelper.registerVillagerOffers(SUGAR_SHACK_VILLAGER_KEY, 1, factories -> {
												factories.add((entity, random) -> new MerchantOffer(
				new ItemCost(CanadaItems.SAP, 4),
				new ItemStack(Items.EMERALD, 1), 16, 2, 0.05f));
												factories.add((entity, random) -> new MerchantOffer(
				new ItemCost(CanadaItems.MAPLE_SAP, 4),
				new ItemStack(Items.EMERALD, 2), 16, 2, 0.05f));
		});

								TradeOfferHelper.registerVillagerOffers(SUGAR_SHACK_VILLAGER_KEY, 2, factories -> {
												factories.add((entity, random) -> new MerchantOffer(
				new ItemCost(CanadaItems.PELT, 9),
				new ItemStack(Items.EMERALD, 1), 12, 5, 0.05f));
												factories.add((entity, random) -> new MerchantOffer(
				new ItemCost(Items.EMERALD, 2),
				new ItemStack(CanadaItems.MAPLE_SAPLING, 4), 12, 5, 0.05f));
		});

								TradeOfferHelper.registerVillagerOffers(SUGAR_SHACK_VILLAGER_KEY, 3, factories -> {
												factories.add((entity, random) -> new MerchantOffer(
				new ItemCost(CanadaItems.ANTLERS, 4),
				new ItemStack(Items.EMERALD, 1), 12, 5, 0.05f));
												factories.add((entity, random) -> new MerchantOffer(
																new ItemCost(Items.EMERALD, 2),
																new ItemStack(CanadaItems.CHEESE_CURD, 3), 12, 5, 0.05f));
		});

								TradeOfferHelper.registerVillagerOffers(SUGAR_SHACK_VILLAGER_KEY, 4, factories -> {
												factories.add((entity, random) -> new MerchantOffer(
																new ItemCost(Items.EMERALD, 3),
																new ItemStack(CanadaItems.MAPLE_SYRUP_BOTTLE, 1), 12, 5, 0.05f));
												factories.add((entity, random) -> new MerchantOffer(
																new ItemCost(Items.EMERALD, 3),
																new ItemStack(CanadaItems.POUTINE, 1), 12, 5, 0.05f));
		});

								TradeOfferHelper.registerVillagerOffers(SUGAR_SHACK_VILLAGER_KEY, 5, factories -> {
												factories.add((entity, random) -> new MerchantOffer(
																new ItemCost(Items.EMERALD, 12),
																new ItemStack(CanadaItems.MOOSE_HELMET, 1), 12, 5, 0.05f));
												factories.add((entity, random) -> new MerchantOffer(
																new ItemCost(Items.EMERALD, 12),
																new ItemStack(CanadaItems.BEAVER_HELMET, 1), 12, 5, 0.05f));
		});

		TradeOfferHelper.registerWanderingTraderOffers(builder -> {
			builder.addOffersToPool(
				TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL,
				new TradeOfferFactory(new MerchantOffer(
					new ItemCost(Items.EMERALD, 2),
					new ItemStack(CanadaItems.MAPLE_SAPLING, 1), 12, 20, 0.05f)
				),
																new TradeOfferFactory(new MerchantOffer(
					new ItemCost(Items.EMERALD, 4),
					new ItemStack(CanadaItems.MOOSE_HEAD, 1), 12, 20, 0.05f)
				)
			);
		});

								TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 3, factories -> {
			// Map to sugar shack
			factories.add((entity, random) -> SellMap(entity, random, 10,
				CanadaTags.StructureTags.MAPLE_CABIN, MapDecorationTypes.TARGET_X, 4, 15));
		});
				}

				private static class TradeOfferFactory implements ItemListing {
		private MerchantOffer _tradeOffer;

		public TradeOfferFactory(MerchantOffer tradeOffer) {
			_tradeOffer = tradeOffer;
		}

		@Override
		public MerchantOffer getOffer(Entity arg0, RandomSource arg1) {
			return _tradeOffer.copy();
		}
	}
}
