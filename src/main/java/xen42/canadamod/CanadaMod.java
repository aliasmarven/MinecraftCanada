package xen42.canadamod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import xen42.canadamod.block.CookingPotBlockEntity;
import xen42.canadamod.block.MooseSkullBlockEntity;
import xen42.canadamod.entities.BeaverChopTreeEffectPayload;
import xen42.canadamod.entities.BeaverEntity;
import xen42.canadamod.entities.DuckEntity;
import xen42.canadamod.entities.GooseEntity;
import xen42.canadamod.entities.GrizzlyEntity;
import xen42.canadamod.entities.MapleBoatEntity;
import xen42.canadamod.entities.MooseEntity;
import xen42.canadamod.entities.eggs.CustomEggEntity;
import xen42.canadamod.entities.eggs.DuckEggEntity;
import xen42.canadamod.entities.eggs.GooseEggEntity;
import xen42.canadamod.recipe.CookingPotRecipe;
import xen42.canadamod.recipe.CookingPotRecipeDisplay;
import xen42.canadamod.screen.CookingPotScreenHandler;
import xen42.canadamod.world.CanadaConfiguredFeatures;
import xen42.canadamod.world.CanadaPlacedFeatures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CanadaMod implements ModInitializer {
	public static final String MOD_ID = "canadamod";

	public static final ResourceKey<Biome> MAPLE_BIOME_KEY = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "maple_forest"));

	public static final ResourceKey<RecipeType<?>> COOKING_POT_RECIPE_TYPE_KEY = ResourceKey.create(Registries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "cooking_pot"));
	public static final RecipeType<CookingPotRecipe> COOKING_POT_RECIPE_TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "cooking_pot"), new RecipeType<CookingPotRecipe>() {
		public String toString() {
			return "cooking_pot";
		}
	});
	public static final RecipeSerializer<CookingPotRecipe> COOKING_POT_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(MOD_ID, "cooking_pot"), new RecipeSerializer<>(CookingPotRecipe.Serializer.CODEC, CookingPotRecipe.Serializer.PACKET_CODEC));
	public static final RecipeDisplay.Type<CookingPotRecipeDisplay> COOKING_POT_RECIPE_DISPLAY = Registry.register(BuiltInRegistries.RECIPE_DISPLAY, Identifier.fromNamespaceAndPath(MOD_ID, "cooking_pot"), CookingPotRecipeDisplay.SERIALIZER);
	public static final RecipeBookCategory COOKING_POT_RECIPE_BOOK_CATEGORY = Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY,
		Identifier.fromNamespaceAndPath(MOD_ID, "cooking_pot"), new RecipeBookCategory() {
		public String toString() {
			return "COOKING_POT";
		}
	});

	public static final MenuType<CookingPotScreenHandler> COOKING_POT_SCREEN_HANDLER_TYPE = Registry.register(
		BuiltInRegistries.MENU,
		Identifier.fromNamespaceAndPath(MOD_ID, "cooking_pot"),
		new MenuType<CookingPotScreenHandler>(CookingPotScreenHandler::new, null));

	public static final BlockEntityType<CookingPotBlockEntity> COOKING_POT_ENTITY = registerBlockEntityType(
		"cooking_pot_entity",
		FabricBlockEntityTypeBuilder.create(CookingPotBlockEntity::new,
			new Block[] { CanadaBlocks.COOKING_POT }).build()
	);
	public static final BlockEntityType<MooseSkullBlockEntity> MOOSE_HEAD_ENTITY = registerBlockEntityType(
		"moose_head_entity",
		FabricBlockEntityTypeBuilder.create(MooseSkullBlockEntity::new,
			new Block[] { CanadaBlocks.MOOSE_HEAD, CanadaBlocks.MOOSE_WALL_HEAD }).build()
	);
	public static <T extends BlockEntityType<?>> T registerBlockEntityType(String path, T blockEntityType) {
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, path), blockEntityType);
	}

	public static final ResourceKey<EntityType<?>> BEAVER_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID,"beaver"));
	public static final EntityType<BeaverEntity> BEAVER_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "beaver"),
		EntityType.Builder.of(BeaverEntity::new, MobCategory.CREATURE).sized(0.5f, 0.5f).build(BEAVER_ENTITY_KEY));

	public static final ResourceKey<EntityType<?>> MOOSE_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID,"moose"));
	public static final EntityType<MooseEntity> MOOSE_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "moose"),
		EntityType.Builder.of(MooseEntity::new, MobCategory.CREATURE).sized(1.75f, 2.5f).build(MOOSE_ENTITY_KEY));

	public static final ResourceKey<EntityType<?>> DUCK_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID,"duck"));
	public static final EntityType<DuckEntity> DUCK_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "duck"),
		EntityType.Builder.of(DuckEntity::new, MobCategory.CREATURE).sized(0.4f, 0.7f).build(DUCK_ENTITY_KEY));

	public static final ResourceKey<EntityType<?>> GOOSE_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID,"goose"));
	public static final EntityType<GooseEntity> GOOSE_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "goose"),
		EntityType.Builder.of(GooseEntity::new, MobCategory.CREATURE).sized(0.4f, 0.7f).build(GOOSE_ENTITY_KEY));

	public static final ResourceKey<EntityType<?>> GRIZZLY_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID,"grizzly"));
	public static final EntityType<GrizzlyEntity> GRIZZLY_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "grizzly"),
		EntityType.Builder.of(GrizzlyEntity::new, MobCategory.CREATURE).sized(1.4f, 1.4f).build(GRIZZLY_ENTITY_KEY));

	public static final ResourceKey<EntityType<?>> DUCK_EGG_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID,"duck_egg_entity"));
	public static final EntityType<CustomEggEntity> DUCK_EGG_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "duck_egg_entity"),
		EntityType.Builder.<CustomEggEntity>of(DuckEggEntity::new, MobCategory.MISC).sized(0.0f, 0.0f).build(DUCK_EGG_ENTITY_KEY));

	public static final ResourceKey<EntityType<?>> GOOSE_EGG_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID,"goose_egg_entity"));
	public static final EntityType<CustomEggEntity> GOOSE_EGG_ENTITY = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath(MOD_ID, "goose_egg_entity"),
		EntityType.Builder.<CustomEggEntity>of(GooseEggEntity::new, MobCategory.MISC).sized(0.0f, 0.0f).build(GOOSE_EGG_ENTITY_KEY));

	private static Holder<MobEffect> registerStatusEffect(String id, MobEffect statusEffect) {
		return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(MOD_ID, id), statusEffect);
	}
	public static Holder<MobEffect> BEAVER_EFFECT;
	public static Holder<MobEffect> MOOSE_EFFECT;

	public static final ResourceKey<Structure> MAPLE_CABIN_KEY = ResourceKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath(MOD_ID, "maple_cabin"));
	public static final StructureType<JigsawStructure> MAPLE_CABIN_TYPE_KEY = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "maple_cabin"),
		() -> JigsawStructure.CODEC);

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Loading Canada mod!");

		FabricDefaultAttributeRegistry.register(BEAVER_ENTITY, BeaverEntity.createBeaverAttributes());
		FabricDefaultAttributeRegistry.register(MOOSE_ENTITY, MooseEntity.createMooseAttributes());
		FabricDefaultAttributeRegistry.register(DUCK_ENTITY, DuckEntity.createDuckAttributes());
		FabricDefaultAttributeRegistry.register(GOOSE_ENTITY, GooseEntity.createGooseAttributes());
		FabricDefaultAttributeRegistry.register(GRIZZLY_ENTITY, GrizzlyEntity.createGrizzlyAttributes());

		BEAVER_EFFECT = registerStatusEffect("beaver_effect",
			(new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 10187841))
			.addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, Identifier.fromNamespaceAndPath(MOD_ID, "effect.beaver_effect"), 3f, Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.SUBMERGED_MINING_SPEED, Identifier.fromNamespaceAndPath(MOD_ID, "effect.beaver_effect"), 2f, Operation.ADD_MULTIPLIED_TOTAL)
		);
		MOOSE_EFFECT = registerStatusEffect("moose_effect",
			(new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 7079970))
			.addAttributeModifier(Attributes.MAX_HEALTH, Identifier.fromNamespaceAndPath(MOD_ID, "effect.moose_effect"), 6.0, Operation.ADD_VALUE)
			.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, Identifier.fromNamespaceAndPath(MOD_ID, "effect.moose_effect"), 2f, Operation.ADD_MULTIPLIED_TOTAL)
		);

		CanadaBlocks.initialize();
		CanadaConfiguredFeatures.onInitialize();
		CanadaItems.initialize();
		MapleBoatEntity.initialize();
		CanadaVillagers.initialize();
		CanadaPlacedFeatures.onInitialize();

		BiomeModifications.addFeature(BiomeSelectors.includeByKey(MAPLE_BIOME_KEY), GenerationStep.Decoration.VEGETAL_DECORATION, CanadaPlacedFeatures.MAPLE_FOREST_VEGETATION);

		SpawnPlacements.register(BEAVER_ENTITY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CanadaMod::canSpawn);
		SpawnPlacements.register(MOOSE_ENTITY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CanadaMod::canSpawn);
		SpawnPlacements.register(DUCK_ENTITY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CanadaMod::canSpawn);
		SpawnPlacements.register(GRIZZLY_ENTITY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CanadaMod::canSpawn);
		SpawnPlacements.register(GOOSE_ENTITY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CanadaMod::canSpawn);

		var mooseBiomes = BiomeSelectors.includeByKey(MAPLE_BIOME_KEY)
			.or(BiomeSelectors.includeByKey(Biomes.TAIGA))
			.or(BiomeSelectors.includeByKey(Biomes.OLD_GROWTH_SPRUCE_TAIGA))
			.or(BiomeSelectors.includeByKey(Biomes.OLD_GROWTH_PINE_TAIGA))
			.or(BiomeSelectors.includeByKey(Biomes.SNOWY_TAIGA));
		BiomeModifications.addSpawn(mooseBiomes, MobCategory.CREATURE, MOOSE_ENTITY, 60, 2, 2);
		BiomeModifications.addSpawn(mooseBiomes, MobCategory.CREATURE, GRIZZLY_ENTITY, 60, 1, 2);

		var beaverBiomes = BiomeSelectors.includeByKey(MAPLE_BIOME_KEY)
			.or(BiomeSelectors.includeByKey(Biomes.RIVER))
			.or(BiomeSelectors.includeByKey(Biomes.SWAMP));
		BiomeModifications.addSpawn(beaverBiomes, MobCategory.CREATURE, BEAVER_ENTITY, 100, 2, 3);
		BiomeModifications.addSpawn(beaverBiomes, MobCategory.CREATURE, DUCK_ENTITY, 100, 2, 4);
		BiomeModifications.addSpawn(beaverBiomes, MobCategory.CREATURE, GOOSE_ENTITY, 80, 1, 2);

		PayloadTypeRegistry.playS2C().register(BeaverChopTreeEffectPayload.PAYLOAD_ID, BeaverChopTreeEffectPayload.CODEC);

		CanadaSounds.onInit();
	}

	private static boolean canSpawn(EntityType type, ServerLevelAccessor access, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		var ground = access.getBlockState(pos.below());
		var goodGround = ground.is(BlockTags.DIRT) || ground.is(Blocks.GRASS_BLOCK) || ground.is(Blocks.PODZOL) || ground.is(Blocks.SNOW);
		var goodLight = access.getMaxLocalRawBrightness(pos) > 8;
		return goodGround && goodLight;
	}
}