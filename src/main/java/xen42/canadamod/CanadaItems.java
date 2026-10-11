package xen42.canadamod;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import xen42.canadamod.entities.MapleBoatEntity;
import xen42.canadamod.entities.eggs.DuckEggEntity;
import xen42.canadamod.item.DispensibleSpawnEggItem;
import xen42.canadamod.item.DurabilityFoodItem;
import xen42.canadamod.item.ThermosContentsComponent;
import xen42.canadamod.item.ThermosItem;
import xen42.canadamod.item.eggs.CustomEggItem;
import xen42.canadamod.item.eggs.DuckEggItem;
import xen42.canadamod.item.eggs.GooseEggItem;

public class CanadaItems {
    public static final Item PELT = register("pelt", Item::new, new Item.Properties());
    public static final Item FLOUR = register("flour", Item::new, new Item.Properties());
    public static final Item CHEESE_CURD = register("cheese_curd", Item::new, new Item.Properties()
        .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1f).build()));
    public static final Item POUTINE = register("poutine", DurabilityFoodItem::new, new Item.Properties().rarity(Rarity.UNCOMMON)
        .stacksTo(1).durability(8).craftRemainder(Items.BOWL).usingConvertsTo(Items.BOWL)
        .food(new FoodProperties.Builder().nutrition(10).saturationModifier(2f).build()));
    public static final Item DONAIR = register("donair", Item::new, new Item.Properties().rarity(Rarity.UNCOMMON)
        .food(new FoodProperties.Builder().nutrition(6).saturationModifier(2f).build()));
    public static final Item PIEROGI = register("pierogi", Item::new, new Item.Properties().rarity(Rarity.UNCOMMON)
        .food(new FoodProperties.Builder().nutrition(6).saturationModifier(2f).build()));
    public static final Item BUTTER_TART = register("butter_tart", Item::new, new Item.Properties().food(Foods.PUMPKIN_PIE));

    public static final Item BEAVER_SPAWN_EGG = register("beaver_spawn_egg", (settings) ->
        new DispensibleSpawnEggItem(CanadaMod.BEAVER_ENTITY, settings), new Item.Properties());

    public static final Item MOOSE_SPAWN_EGG = register("moose_spawn_egg", (settings) ->
        new DispensibleSpawnEggItem(CanadaMod.MOOSE_ENTITY, settings), new Item.Properties());

    public static final Item DUCK_SPAWN_EGG = register("duck_spawn_egg", (settings) ->
        new DispensibleSpawnEggItem(CanadaMod.DUCK_ENTITY, settings), new Item.Properties());

    public static final Item GOOSE_SPAWN_EGG = register("goose_spawn_egg", (settings) ->
        new DispensibleSpawnEggItem(CanadaMod.GOOSE_ENTITY, settings), new Item.Properties());

    public static final Item GRIZZLY_SPAWN_EGG = register("grizzly_spawn_egg", (settings) ->
        new DispensibleSpawnEggItem(CanadaMod.GRIZZLY_ENTITY, settings), new Item.Properties());

    public static final Item TREE_TAP = register("tree_tap", (settings) -> new BlockItem(CanadaBlocks.TREE_TAP, settings), new Item.Properties());
    public static final Item MAPLE_SAPLING = register("maple_sapling", (settings) -> new BlockItem(CanadaBlocks.MAPLE_SAPLING, settings),
        new Item.Properties().compostable(ResourceKey.create(
            Registries.CONTEXT_INT_PROVIDER,
            Identifier.fromNamespaceAndPath("minecraft", "compostable/low")
        )));

    public static final FoodProperties MAPLE_SYRUP_FOOD = new FoodProperties.Builder().nutrition(4).saturationModifier(1.4F).alwaysEdible().build();
    public static final Consumable MAPLE_SYRUP_CONSUME = Consumables.defaultDrink().consumeSeconds(2.0F)
        .sound(SoundEvents.HONEY_DRINK).onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
        new MobEffectInstance(MobEffects.HASTE, 600, 1)
    ))).build();

    public static final FoodProperties SAP_FOOD = new FoodProperties.Builder().nutrition(3).saturationModifier(0.1F).alwaysEdible().build();
    public static final Consumable SAP_CONSUME = Consumables.defaultDrink().consumeSeconds(2.0F)
        .sound(SoundEvents.HONEY_DRINK).onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
        new MobEffectInstance(MobEffects.SLOWNESS, 200, 1),
        new MobEffectInstance(MobEffects.NAUSEA, 200, 1)
    ))).build();

    public static final FoodProperties GRAVY_FOOD = new FoodProperties.Builder().nutrition(4).saturationModifier(1.0F).alwaysEdible().build();
    public static final Consumable GRAVY_CONSUME = Consumables.defaultDrink().consumeSeconds(2.0F)
        .sound(SoundEvents.HONEY_DRINK).build();

    public static final Item MAPLE_SYRUP_BOTTLE = register("maple_syrup_bottle", Item::new, new Item.Properties()
        .craftRemainder(Items.GLASS_BOTTLE).food(MAPLE_SYRUP_FOOD, MAPLE_SYRUP_CONSUME)
        .usingConvertsTo(Items.GLASS_BOTTLE).stacksTo(1));


    public static final Item MAPLE_SAP = register("maple_sap", Item::new, new Item.Properties().food(SAP_FOOD));
    public static final Item SAP = register("sap", Item::new, new Item.Properties().food(SAP_FOOD, SAP_CONSUME));

    public static final Item RUBBER = register("rubber", Item::new, new Item.Properties());

    private static Map<ArmorType, Integer> NO_DEFENSE_MAP = Maps.newEnumMap(Map.of(ArmorType.BOOTS, 0, ArmorType.LEGGINGS, 0, ArmorType.CHESTPLATE, 0, ArmorType.HELMET, 0, ArmorType.BODY, 0));
    public static final ArmorMaterial NO_ARMOR = new ArmorMaterial(5, NO_DEFENSE_MAP, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, ItemTags.REPAIRS_LEATHER_ARMOR, EquipmentAssets.LEATHER);

    public static final Item MOOSE_HEAD = register("moose_head", (settings) -> {
        return new StandingAndWallBlockItem(CanadaBlocks.MOOSE_HEAD, CanadaBlocks.MOOSE_WALL_HEAD, Direction.DOWN, settings);
    }, new Item.Properties().humanoidArmor(NO_ARMOR, ArmorType.HELMET).rarity(Rarity.UNCOMMON));

    public static final Item GRAVY = register("gravy", Item::new, new Item.Properties()
        .craftRemainder(Items.GLASS_BOTTLE).food(GRAVY_FOOD, GRAVY_CONSUME)
        .usingConvertsTo(Items.GLASS_BOTTLE).stacksTo(1));

    public static final Item VENISON = register("venison", Item::new, (new Item.Properties()).food(Foods.MUTTON));
    public static final Item COOKED_VENISON = register("cooked_venison", Item::new, (new Item.Properties()).food(Foods.COOKED_MUTTON));
    public static final Item ANTLERS = register("antlers", Item::new, new Item.Properties());

    public static final Item WATERFOWL = register("waterfowl", Item::new, (new Item.Properties()).food(Foods.CHICKEN));
    public static final Item COOKED_WATERFOWL = register("cooked_waterfowl", Item::new, (new Item.Properties()).food(Foods.COOKED_CHICKEN));

    public static final Item MAPLE_BOAT = register("maple_boat", settings ->
        new BoatItem(MapleBoatEntity.MAPLE_BOAT, settings), (new Item.Properties()).stacksTo(1));
    public static final Item MAPLE_CHEST_BOAT = register("maple_chest_boat", settings ->
        new BoatItem(MapleBoatEntity.MAPLE_CHEST_BOAT, settings), (new Item.Properties()).stacksTo(1));

    private static <T> DataComponentType<T> registerContentsComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builderOperator.apply(DataComponentType.builder()).build());
    }
    public static final DataComponentType<ThermosContentsComponent> THERMOS_CONTENTS = registerContentsComponent("thermos_contents", (builder) -> {
        return builder.persistent(ThermosContentsComponent.CODEC).networkSynchronized(ThermosContentsComponent.PACKET_CODEC).cacheEncoding();
    });
    public static final Item THERMOS = register("thermos",
        settings -> new ThermosItem(settings), (new Item.Properties()).stacksTo(1).component(THERMOS_CONTENTS, ThermosContentsComponent.DEFAULT));

    public static final Item BEAVER_HELMET = register("beaver_helmet", Item::new,
        new Item.Properties().humanoidArmor(NO_ARMOR, ArmorType.HELMET).rarity(Rarity.UNCOMMON));
    public static final Item MOOSE_HELMET = register("moose_helmet", Item::new,
        new Item.Properties().humanoidArmor(NO_ARMOR, ArmorType.HELMET).rarity(Rarity.UNCOMMON));

    public static Item MAPLE_HANGING_SIGN_ITEM, MAPLE_SIGN_ITEM;

    public static final Item DUCK_EGG = register("duck_egg", DuckEggItem::new, new Item.Properties().stacksTo(16));
    public static final Item GOOSE_EGG = register("goose_egg", GooseEggItem::new, new Item.Properties().stacksTo(16));

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, name));

		// Create the item instance.
		Item item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

    public static void initialize() {
        MAPLE_HANGING_SIGN_ITEM = register("maple_hanging_sign", settings ->
            new HangingSignItem(CanadaBlocks.MAPLE_HANGING_SIGN, CanadaBlocks.MAPLE_WALL_HANGING_SIGN, settings),
            new Item.Properties().stacksTo(16).cookingFuel(ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "cooking/maple_two_thirds"))));

        MAPLE_SIGN_ITEM = register("maple_sign", settings ->
            new StandingAndWallBlockItem(CanadaBlocks.MAPLE_SIGN, CanadaBlocks.MAPLE_WALL_SIGN, Direction.DOWN, settings),
            new Item.Properties().stacksTo(16).cookingFuel(ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "cooking/maple_two_thirds"))));

        // Add custom items to groups
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> {
            itemGroup.accept(PELT);
            itemGroup.accept(ANTLERS);
            itemGroup.accept(FLOUR);
            itemGroup.accept(SAP);
            itemGroup.accept(MAPLE_SAP);
            itemGroup.accept(RUBBER);
            itemGroup.accept(DUCK_EGG);
            itemGroup.accept(GOOSE_EGG);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((itemGroup) -> {
            for (var block : CanadaBlocks.MAPLE_BLOCKS) {
                itemGroup.accept(block.asItem());
            }
            itemGroup.accept(CanadaBlocks.RUBBER_BLOCK);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((itemGroup) -> {
            itemGroup.accept(CanadaBlocks.MAPLE_LEAVES.asItem());
            itemGroup.accept(CanadaBlocks.MAPLE_SAPLING.asItem());
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((itemGroup) -> {
            itemGroup.accept(MAPLE_SIGN_ITEM);
            itemGroup.accept(MAPLE_HANGING_SIGN_ITEM);
            itemGroup.accept(TREE_TAP);
            itemGroup.accept(CanadaBlocks.COOKING_POT.asItem());
            itemGroup.accept(MOOSE_HEAD);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((itemGroup) -> {
            itemGroup.accept(MAPLE_BOAT);
            itemGroup.accept(MAPLE_CHEST_BOAT);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register((itemGroup) -> {
            itemGroup.accept(MAPLE_SYRUP_BOTTLE);
            itemGroup.accept(GRAVY);
            itemGroup.accept(CHEESE_CURD);
            itemGroup.accept(POUTINE);
            itemGroup.accept(DONAIR);
            itemGroup.accept(PIEROGI);
            itemGroup.accept(BUTTER_TART);
            itemGroup.accept(VENISON);
            itemGroup.accept(COOKED_VENISON);
            itemGroup.accept(WATERFOWL);
            itemGroup.accept(COOKED_WATERFOWL);
            itemGroup.accept(THERMOS);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register((itemGroup) -> {
            itemGroup.accept(BEAVER_SPAWN_EGG);
            itemGroup.accept(MOOSE_SPAWN_EGG);
            itemGroup.accept(DUCK_SPAWN_EGG);
            itemGroup.accept(GRIZZLY_SPAWN_EGG);
            itemGroup.accept(GOOSE_SPAWN_EGG);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> {
            itemGroup.accept(BEAVER_HELMET);
            itemGroup.accept(MOOSE_HELMET);
        });

    }
}
