package xen42.canadamod;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import xen42.canadamod.recipe.CookingPotRecipeJsonBuilder;

public class CanadaModRecipeGenerator extends FabricRecipeProvider {
    public CanadaModRecipeGenerator(FabricPackOutput generator, CompletableFuture<Provider> registriesFuture) {
        super(generator, registriesFuture);
    }

    @Override
    public String getName() {
        return "CanadaModRecipeGenerator";
    }

    @Override
    public Identifier getRecipeIdentifier(Identifier identifier) {
        return Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, identifier.getPath());
    }

    @Override
    protected RecipeProvider createRecipeProvider(
            Provider registryLookup,
            net.minecraft.data.worldgen.BootstrapContext<net.minecraft.world.item.crafting.Recipe<?>> recipes,
            net.minecraft.data.worldgen.BootstrapContext<net.minecraft.advancements.Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.MISC, Items.LEATHER)
                    .pattern("XX")
                    .pattern("XX")
                    .define('X', CanadaItems.PELT)
                    .unlockedBy(getHasName(CanadaItems.PELT), has(CanadaItems.PELT))
                    .save(output);

                shaped(RecipeCategory.TOOLS, CanadaItems.TREE_TAP)
                    .pattern("XX")
                    .pattern("Y ")
                    .define('X', Items.COPPER_INGOT)
                    .define('Y', Items.BUCKET)
                    .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                    .unlockedBy(getHasName(Items.BUCKET), has(Items.BUCKET))
                    .save(output);

                shapeless(RecipeCategory.BUILDING_BLOCKS, CanadaBlocks.MAPLE_PLANKS, 4)
                    .requires(CanadaTags.ItemTags.MAPLE_LOGS)
                    .unlockedBy(hasTag(CanadaTags.ItemTags.MAPLE_LOGS), has(CanadaTags.ItemTags.MAPLE_LOGS))
                    .save(output);

                shaped(RecipeCategory.TOOLS, CanadaBlocks.COOKING_POT)
                    .pattern("X")
                    .pattern("Y")
                    .define('X', Items.CAULDRON)
                    .define('Y', Items.FURNACE)
                    .unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON))
                    .unlockedBy(getHasName(Items.FURNACE), has(Items.FURNACE))
                    .save(output);

                stairBuilder(CanadaBlocks.MAPLE_STAIRS.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .save(output);

                buttonBuilder(CanadaBlocks.MAPLE_BUTTON.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .save(output);

                slabBuilder(RecipeCategory.BUILDING_BLOCKS, CanadaBlocks.MAPLE_SLAB.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .save(output);

                doorBuilder(CanadaBlocks.MAPLE_DOOR.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .save(output);

                fenceGateBuilder(CanadaBlocks.MAPLE_FENCE_GATE.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .save(output);

                fenceBuilder(CanadaBlocks.MAPLE_FENCE.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .save(output);

                pressurePlateBuilder(RecipeCategory.BUILDING_BLOCKS, CanadaBlocks.MAPLE_PRESSURE_PLATE.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .save(output);

                signBuilder(CanadaBlocks.MAPLE_SIGN.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                    .save(output);

                trapdoorBuilder(CanadaBlocks.MAPLE_TRAPDOOR.asItem(), Ingredient.of(CanadaBlocks.MAPLE_PLANKS.asItem()))
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_PLANKS), has(CanadaBlocks.MAPLE_PLANKS))
                    .save(output);

                shaped(RecipeCategory.MISC, CanadaBlocks.MAPLE_HANGING_SIGN)
                    .pattern("X X")
                    .pattern("YYY")
                    .pattern("YYY")
                    .define('X', Items.IRON_CHAIN)
                    .define('Y', CanadaTags.ItemTags.MAPLE_LOGS)
                    .unlockedBy(hasTag(CanadaTags.ItemTags.MAPLE_LOGS), has(CanadaTags.ItemTags.MAPLE_LOGS))
                    .unlockedBy(getHasName(Items.IRON_CHAIN), has(Items.IRON_CHAIN))
                    .save(output);

                woodenBoat(CanadaItems.MAPLE_BOAT, CanadaBlocks.MAPLE_PLANKS);
                chestBoat(CanadaItems.MAPLE_CHEST_BOAT, CanadaBlocks.MAPLE_PLANKS);

                shaped(RecipeCategory.BUILDING_BLOCKS, CanadaBlocks.MAPLE_WOOD, 3)
                    .pattern("XX")
                    .pattern("XX")
                    .define('X', CanadaBlocks.MAPLE_LOG)
                    .unlockedBy(getHasName(CanadaBlocks.MAPLE_LOG), has(CanadaBlocks.MAPLE_LOG))
                    .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, CanadaBlocks.STRIPPED_MAPLE_WOOD, 3)
                    .pattern("XX")
                    .pattern("XX")
                    .define('X', CanadaBlocks.STRIPPED_MAPLE_LOG)
                    .unlockedBy(getHasName(CanadaBlocks.STRIPPED_MAPLE_LOG), has(CanadaBlocks.STRIPPED_MAPLE_LOG))
                    .save(output);

                shapeless(RecipeCategory.FOOD, CanadaItems.FLOUR)
                    .requires(Items.WHEAT)
                    .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                    .save(output);

                shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 3)
                    .requires(CanadaItems.ANTLERS)
                    .unlockedBy(getHasName(CanadaItems.ANTLERS), has(CanadaItems.ANTLERS))
                    .save(output);

                createCookingPotRecipe(registryLookup, Items.RABBIT_STEW, 1)
                    .input(Items.RABBIT, this)
                    .input(ConventionalItemTags.VEGETABLE_FOODS, this)
                    .requiresBowl()
                    .save(output);

                createCookingPotRecipe(registryLookup, Items.MUSHROOM_STEW, 1)
                    .input(ConventionalItemTags.MUSHROOMS, this)
                    .requiresBowl()
                    .save(output);

                createCookingPotRecipe(registryLookup, Items.BEETROOT_SOUP, 1)
                    .input(Items.BEETROOT, this)
                    .input(Items.BEETROOT, this)
                    .input(Items.BEETROOT, this)
                    .input(Items.BEETROOT, this)
                    .requiresBowl()
                    .save(output);

                createCookingPotRecipe(registryLookup, Items.CAKE, 1)
                    .input(Items.MILK_BUCKET, this)
                    .input(ItemTags.EGGS, this)
                    .input(Items.SUGAR, this)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .save(output);

                createCookingPotRecipe(registryLookup, Items.PUMPKIN_PIE, 3)
                    .input(Blocks.PUMPKIN, this)
                    .input(ItemTags.EGGS, this)
                    .input(Items.SUGAR, this)
                    .save(output);

                createCookingPotRecipe(registryLookup, Items.BREAD, 3)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .input(Items.WATER_BUCKET, this)
                    .save(output);

                createCookingPotRecipe(registryLookup, Items.SLIME_BALL, 1)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .input(CanadaTags.ItemTags.SAP, this)
                    .save(output);

                createCookingPotRecipe(registryLookup, CanadaItems.GRAVY, 1)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .input(CanadaTags.ItemTags.RAW_MEAT, this)
                    .requiresBottle()
                    .save(output);

                createCookingPotRecipe(registryLookup, CanadaItems.CHEESE_CURD, 3)
                    .input(Items.MILK_BUCKET, this)
                    .input(Items.FERMENTED_SPIDER_EYE, this)
                    .save(output);

                createCookingPotRecipe(registryLookup, CanadaItems.RUBBER, 4)
                    .input(CanadaTags.ItemTags.SAP, this)
                    .input(CanadaTags.ItemTags.SAP, this)
                    .input(CanadaTags.ItemTags.SAP, this)
                    .input(Items.FERMENTED_SPIDER_EYE, this)
                    .save(output);

                createCookingPotRecipe(registryLookup, CanadaItems.MAPLE_SYRUP_BOTTLE, 1)
                    .input(CanadaItems.MAPLE_SAP, this)
                    .requiresBottle()
                    .save(output);

                createCookingPotRecipe(registryLookup, CanadaItems.POUTINE, 1)
                    .input(CanadaItems.GRAVY, this)
                    .input(Items.POTATO, this)
                    .input(CanadaItems.CHEESE_CURD, this)
                    .requiresBowl()
                    .save(output);

                createCookingPotRecipe(registryLookup, CanadaItems.DONAIR, 2)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .input(Items.BEEF, this)
                    .input(Items.SUGAR, this)
                    .save(output);

                createCookingPotRecipe(registryLookup, CanadaItems.PIEROGI, 2)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .input(CanadaTags.ItemTags.CHEESE, this)
                    .input(Items.POTATO, this)
                    .save(output);

                createCookingPotRecipe(registryLookup, CanadaItems.BUTTER_TART, 4)
                    .input(CanadaTags.ItemTags.FLOUR, this)
                    .input(Items.MILK_BUCKET, this)
                    .input(ItemTags.EGGS, this)
                    .input(CanadaItems.MAPLE_SYRUP_BOTTLE, this)
                    .save(output);

                shaped(RecipeCategory.FOOD, CanadaItems.THERMOS)
                    .pattern(" X ")
                    .pattern("Y Y")
                    .pattern("XXX")
                    .define('X', Items.IRON_INGOT)
                    .define('Y', CanadaTags.ItemTags.RUBBER)
                    .unlockedBy(getHasName(CanadaItems.RUBBER), has(CanadaItems.RUBBER))
                    .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, CanadaBlocks.RUBBER_BLOCK)
                    .pattern("XX")
                    .pattern("XX")
                    .define('X', CanadaTags.ItemTags.RUBBER)
                    .unlockedBy(getHasName(CanadaItems.RUBBER), has(CanadaItems.RUBBER))
                    .save(output);

                shaped(RecipeCategory.COMBAT, CanadaItems.BEAVER_HELMET)
                    .pattern(" Y ")
                    .pattern("XXX")
                    .pattern("X X")
                    .define('X', CanadaItems.PELT)
                    .define('Y', CanadaTags.ItemTags.SAP)
                    .unlockedBy(getHasName(CanadaItems.PELT), has(CanadaItems.PELT))
                    .save(output);

                shaped(RecipeCategory.COMBAT, CanadaItems.MOOSE_HELMET)
                    .pattern("ZYZ")
                    .pattern("XXX")
                    .pattern("X X")
                    .define('X', Items.LEATHER)
                    .define('Y', CanadaTags.ItemTags.SAP)
                    .define('Z', CanadaItems.ANTLERS)
                    .unlockedBy(getHasName(CanadaItems.ANTLERS), has(CanadaItems.ANTLERS))
                    .save(output);

                oreSmelting(List.of(CanadaItems.VENISON), RecipeCategory.FOOD, net.minecraft.world.item.crafting.CookingBookCategory.FOOD, CanadaItems.COOKED_VENISON, 0.35f, 200, net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(CanadaItems.VENISON).getPath());
                oreSmelting(List.of(CanadaItems.WATERFOWL), RecipeCategory.FOOD, net.minecraft.world.item.crafting.CookingBookCategory.FOOD, CanadaItems.COOKED_WATERFOWL, 0.35f, 200, net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(CanadaItems.WATERFOWL).getPath());
            }

            public static CookingPotRecipeJsonBuilder createCookingPotRecipe(Provider registryLookup, ItemLike output, int count) {
                return new CookingPotRecipeJsonBuilder(registryLookup.lookupOrThrow(Registries.ITEM), output, count);
            }
        };
    }

    private String hasTag(TagKey<Item> tag) {
        return "has_" + tag.getName();
    }
}
