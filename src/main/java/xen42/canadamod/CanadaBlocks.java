package xen42.canadamod;

import java.util.Optional;
import java.util.function.Function;

import net.fabricmc.fabric.api.item.v1.FabricItem.Settings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import xen42.canadamod.block.CookingPotBlock;
import xen42.canadamod.block.MooseSkullBlock;
import xen42.canadamod.block.MooseWallSkullBlock;
import xen42.canadamod.block.RubberBlock;
import xen42.canadamod.block.TreeTapBlock;
import xen42.canadamod.sign.MapleHangingSignBlock;
import xen42.canadamod.sign.MapleHangingSignBlockEntity;
import xen42.canadamod.sign.MapleSignBlock;
import xen42.canadamod.sign.MapleSignEntity;
import xen42.canadamod.sign.MapleWallHangingSignBlock;
import xen42.canadamod.sign.MapleWallSignBlock;
import xen42.canadamod.world.CanadaConfiguredFeatures;

public class CanadaBlocks {
    public static final BlockSetType MAPLE_WOOD_SET = BlockSetType.register(new BlockSetType("maple"));
    public static final WoodType MAPLE_WOOD_TYPE = WoodType.register(new WoodType("maple", MAPLE_WOOD_SET));

	public static Block MAPLE_PLANKS;
    public static Block MAPLE_LOG;
    public static Block MAPLE_LEAVES;
    public static Block STRIPPED_MAPLE_LOG;
    public static Block MAPLE_WOOD;
    public static Block STRIPPED_MAPLE_WOOD;
    public static Block MAPLE_STAIRS;
    public static Block MAPLE_BUTTON;
    public static Block MAPLE_SLAB;
    public static Block MAPLE_DOOR;
    public static Block MAPLE_TRAPDOOR;
    public static Block MAPLE_FENCE;
    public static Block MAPLE_FENCE_GATE;
    public static Block MAPLE_PRESSURE_PLATE;
    public static Block MAPLE_SIGN;
    public static Block MAPLE_WALL_SIGN;
    public static Block MAPLE_HANGING_SIGN;
    public static Block MAPLE_WALL_HANGING_SIGN;

	public static final TreeGrower MAPLE_SAPLING_GENERATED = new TreeGrower("maple", Optional.empty(),
		Optional.of(CanadaConfiguredFeatures.MAPLE_CONFIGURED_FEATURE), Optional.of(CanadaConfiguredFeatures.MAPLE_CONFIGURED_FEATURE));

	public static final Block MAPLE_SAPLING = register("maple_sapling",
        settings -> new SaplingBlock(MAPLE_SAPLING_GENERATED, settings),
        BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).mapColor(MapColor.COLOR_RED),
		false);

	public static final Block POTTED_MAPLE_SAPLING = register("potted_maple_sapling",
		settings -> new FlowerPotBlock(MAPLE_SAPLING, settings), Blocks.flowerPotProperties(),
		false);

	public static Block TREE_TAP = register(
			"tree_tap",
			TreeTapBlock::new,
			BlockBehaviour.Properties.of().forceSolidOn().sound(SoundType.CHAIN).noOcclusion().pushReaction(PushReaction.DESTROY).instabreak(),
			false
		);

	public static Block COOKING_POT = register(
		"cooking_pot",
		CookingPotBlock::new,
		BlockBehaviour.Properties.of().lightLevel(Blocks.litBlockEmission(13)).noOcclusion()
			.mapColor(MapColor.STONE).pushReaction(PushReaction.DESTROY).destroyTime(0.3f),
		true
	);

	public static Block RUBBER_BLOCK = register("rubber_block", RubberBlock::new, BlockBehaviour.Properties.of()
		.sound(SoundType.FUNGUS).mapColor(MapColor.COLOR_BLACK).destroyTime(0.1f), true);

	public static Block MOOSE_HEAD = register(
		"moose_head",
		MooseSkullBlock::new,
		BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY),
		false
	);

	public static Block MOOSE_WALL_HEAD = register(
		"moose_head_wall",
		MooseWallSkullBlock::new,
		BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY),
		false
	);

	public static Block[] MAPLE_BLOCKS;

	public static BlockFamily MAPLE;

    public static BlockEntityType<SignBlockEntity> MAPLE_SIGN_BLOCK_ENTITY;
    public static BlockEntityType<HangingSignBlockEntity> MAPLE_HANGING_SIGN_BLOCK_ENTITY;

	public static SimpleParticleType MAPLE_LEAF_PARTICLE = FabricParticleTypes.simple();

	public static void initialize() {
		MAPLE_PLANKS = register(
			"maple_planks",
			Block::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS),
			true
		);
		MAPLE_LOG = register(
			"maple_log",
			RotatedPillarBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG),
			true
		);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE,
					ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "maple"),
					MAPLE_LEAF_PARTICLE);
		MAPLE_LEAVES = register(
			"maple_leaves",
			(settings) -> new UntintedParticleLeavesBlock(0.01F, MAPLE_LEAF_PARTICLE, settings),
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES),
			true
		);
		STRIPPED_MAPLE_LOG = register(
			"stripped_maple_log",
			RotatedPillarBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG),
			true
		);
		MAPLE_WOOD = register(
			"maple_wood",
			RotatedPillarBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD),
			true
		);
		STRIPPED_MAPLE_WOOD = register(
			"stripped_maple_wood",
			RotatedPillarBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD),
			true
		);
		MAPLE_STAIRS = register(
			"maple_stairs",
			(settings) -> new StairBlock(MAPLE_PLANKS.defaultBlockState(), settings),
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS),
			true
		);
		MAPLE_BUTTON = register(
			"maple_button",
			(settings) -> new ButtonBlock(MAPLE_WOOD_SET, 30, settings),
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON),
			true
		);
		MAPLE_SLAB = register(
			"maple_slab",
			SlabBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB),
			true
		);
		MAPLE_DOOR = register(
			"maple_door",
			(settings) -> new DoorBlock(MAPLE_WOOD_SET, settings),
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR),
			true
		);
		MAPLE_TRAPDOOR = register(
			"maple_trapdoor",
			(settings) -> new TrapDoorBlock(MAPLE_WOOD_SET, settings),
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR),
			true
		);
		MAPLE_FENCE = register(
			"maple_fence",
			FenceBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE),
			true
		);
		MAPLE_FENCE_GATE = register(
			"maple_fence_gate",
			(settings) -> new FenceGateBlock(MAPLE_WOOD_TYPE, settings),
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE),
			true
		);
		MAPLE_PRESSURE_PLATE = register(
			"maple_pressure_plate",
			(settings) -> new PressurePlateBlock(MAPLE_WOOD_SET, settings),
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE),
			true
		);
		MAPLE_SIGN = register(
			"maple_sign",
			MapleSignBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN),
			false
		);
		MAPLE_WALL_SIGN = register(
			"maple_wall_sign",
			MapleWallSignBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN),
			false
		);
		MAPLE_HANGING_SIGN = register(
			"maple_hanging_sign",
			MapleHangingSignBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN),
			false
		);
		MAPLE_WALL_HANGING_SIGN = register(
			"maple_hanging_wall_sign",
			MapleWallHangingSignBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN),
			false
		);

		MAPLE = BlockFamilies.familyBuilder(MAPLE_PLANKS)
			.button(MAPLE_BUTTON)
			.door(MAPLE_DOOR)
			.fence(MAPLE_FENCE)
			.fenceGate(MAPLE_FENCE_GATE)
			.pressurePlate(MAPLE_PRESSURE_PLATE)
			.trapdoor(MAPLE_TRAPDOOR)
			.slab(MAPLE_SLAB)
			.stairs(MAPLE_STAIRS)
			.sign(MAPLE_SIGN, MAPLE_WALL_SIGN)
			.recipeGroupPrefix("wooden")
			.recipeUnlockedBy("has_planks")
			.getFamily();

		MAPLE_BLOCKS = new Block[] {
			MAPLE_LOG, MAPLE_WOOD, STRIPPED_MAPLE_LOG, STRIPPED_MAPLE_WOOD, MAPLE_PLANKS, MAPLE_STAIRS,
			MAPLE_SLAB, MAPLE_FENCE, MAPLE_FENCE_GATE, MAPLE_DOOR, MAPLE_TRAPDOOR, MAPLE_PRESSURE_PLATE, MAPLE_BUTTON
		};

		StrippableBlockRegistry.register(CanadaBlocks.MAPLE_LOG, CanadaBlocks.STRIPPED_MAPLE_LOG);
		StrippableBlockRegistry.register(CanadaBlocks.MAPLE_WOOD, CanadaBlocks.STRIPPED_MAPLE_WOOD);

		FuelRegistryEvents.BUILD.register((builder, context) -> {
            int time = context.baseSmeltTime();
			for (var woodItem : new Block[] { MAPLE_LOG, MAPLE_WOOD, STRIPPED_MAPLE_LOG, STRIPPED_MAPLE_WOOD, MAPLE_PLANKS,
												MAPLE_TRAPDOOR, MAPLE_FENCE_GATE, MAPLE_FENCE }) {
				builder.add(woodItem, time);
			}
			for (var woodItem : new Block[] { MAPLE_STAIRS, MAPLE_SLAB, MAPLE_PRESSURE_PLATE }) {
				builder.add(woodItem, time / 2);
			}
			for (var woodItem : new Block[] { MAPLE_DOOR, MAPLE_SIGN, MAPLE_HANGING_SIGN }) {
				builder.add(woodItem, time * 2 / 3);
			}
			builder.add(MAPLE_BUTTON, time / 3);
        });

        MAPLE_SIGN_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "maple_sign"),
			FabricBlockEntityTypeBuilder.<SignBlockEntity>create(MapleSignEntity::new, MAPLE_SIGN, MAPLE_WALL_SIGN).build());

		MAPLE_HANGING_SIGN_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "maple_hanging_sign"),
			FabricBlockEntityTypeBuilder.<HangingSignBlockEntity>create(MapleHangingSignBlockEntity::new, MAPLE_HANGING_SIGN, MAPLE_WALL_HANGING_SIGN).build());
	}

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
		// Create a registry key for the block
		ResourceKey<Block> blockKey = keyOfBlock(name);
		// Create the block instance
		Block block = blockFactory.apply(settings.setId(blockKey));

		// Sometimes, you may not want to register an item for the block.
		// Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
		if (shouldRegisterItem) {
			// Items need to be registered with a different type of registry key, but the ID
			// can be the same.
			ResourceKey<Item> itemKey = keyOfItem(name);

			BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
			Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
		}

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	private static ResourceKey<Block> keyOfBlock(String name) {
		return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, name));
	}

	private static ResourceKey<Item> keyOfItem(String name) {
		return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, name));
	}
}