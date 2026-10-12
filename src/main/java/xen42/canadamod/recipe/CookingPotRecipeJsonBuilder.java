package xen42.canadamod.recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import xen42.canadamod.CanadaMod;

public class CookingPotRecipeJsonBuilder implements RecipeBuilder {

	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<String, Criterion<?>>();
	private final ItemStackTemplate result;
    @Nullable
    private String group;
    private final ArrayList<Ingredient> _inputsList;
    private boolean _requiresBowl;
    private boolean _requiresBottle;
    private final HolderGetter<Item> _registryLookup;

    public CookingPotRecipeJsonBuilder(HolderGetter<Item> registryLookup, ItemLike result, int count) {
        var itemKey = BuiltInRegistries.ITEM.getResourceKey(result.asItem())
            .orElseThrow(() -> new IllegalStateException(
                "Unregistered Cooking Pot recipe result: " + result
            ));

        this.result = new ItemStackTemplate(
            registryLookup.getOrThrow(itemKey),
            count,
            DataComponentPatch.EMPTY
        );

        this._registryLookup = registryLookup;
        _inputsList = new ArrayList<>();
    }

    public CookingPotRecipeJsonBuilder requiresBowl() {
        this._requiresBowl = true;
        this._requiresBottle = false;
        return this;
    }

    public CookingPotRecipeJsonBuilder requiresBottle() {
        this._requiresBottle = true;
        this._requiresBowl = false;
        return this;
    }

	public CookingPotRecipeJsonBuilder input(ItemLike item, RecipeProvider recipeGenerator) {
		this._inputsList.add(Ingredient.of(item));
        return this.unlockedBy(RecipeProvider.getHasName(item), recipeGenerator.has(item));
	}

	public CookingPotRecipeJsonBuilder input(TagKey<Item> tag, RecipeProvider recipeGenerator) {
		this._inputsList.add(Ingredient.of(_registryLookup.getOrThrow(tag)));
        return this.unlockedBy("has_" + tag.getName(), recipeGenerator.has(tag));
	}

    @Override
    public CookingPotRecipeJsonBuilder unlockedBy(String name, Criterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return ResourceKey.create(
            Registries.RECIPE,
            getDefaultRecipeId(this.getResult())
        );
    }

    public Item getResult() {
        return this.result.item().value();
    }

    @Override
    public CookingPotRecipeJsonBuilder group(@Nullable String group) {
		this.group = group;
		return this;
    }

    @Override
    public void save(RecipeOutput exporter, ResourceKey<Recipe<?>> recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeKey.identifier());
        }

        if (_inputsList.size() > 4) {
            throw new IllegalStateException("Too many inputs!");
        }

        if (group == null) {
            group = "";
        }

        var recipe = new CookingPotRecipe(this.group, this.result, this._inputsList, this._requiresBowl, this._requiresBottle);

		Advancement.Builder builder = exporter.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(
                exporter.lookup(Registries.RECIPE).getOrThrow(recipeKey)
            ))
			.rewards(AdvancementRewards.Builder.recipe(recipeKey))
			.requirements(AdvancementRequirements.Strategy.OR);

		this.criteria.forEach(builder::addCriterion);

		exporter.accept(recipeKey, recipe, builder.build(recipeKey.identifier().withPrefix("recipes/cooking_pot/")));
    }

    @Override
    public void save(RecipeOutput exporter) {
		this.save(exporter, ResourceKey.create(Registries.RECIPE, getDefaultRecipeId(this.getResult())));
    }

    @Override
	public void save(RecipeOutput exporter, String recipePath) {
		Identifier identifier = getDefaultRecipeId(this.getResult());
		Identifier identifier2 = Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, recipePath);
		if (identifier2.equals(identifier)) {
			throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
		} else {
			this.save(exporter, ResourceKey.create(Registries.RECIPE, identifier2));
		}
	}

    public static Identifier getDefaultRecipeId(ItemLike item) {
		return Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, BuiltInRegistries.ITEM.getKey(item.asItem()).getPath());
	}
}
