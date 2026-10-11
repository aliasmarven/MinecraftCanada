package xen42.canadamod.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay.ItemStackSlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.VisibleForTesting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.CanadaMod;

public class CookingPotRecipe implements Recipe<CookingPotRecipeInput> {
    public final ItemStack result;
    public final String group;
	public final List<Ingredient> ingredients;
    public final boolean requiresBowl;
    public final boolean requiresBottle;

    public CookingPotRecipe(String group, ItemStack result, List<Ingredient> ingredients, boolean requiresBowl, boolean requiresBottle) {
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.requiresBowl = requiresBowl;
        this.requiresBottle = requiresBottle;
    }

    @Override
    public ItemStack assemble(CookingPotRecipeInput input, Provider registries) {
        return this.result.copy();
    }

	@VisibleForTesting
	public List<Optional<Ingredient>> getIngredients() {
		return this.ingredients.stream().map(x -> Optional.of(x)).collect(Collectors.toList());
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.create(this.ingredients);
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return CanadaMod.COOKING_POT_RECIPE_BOOK_CATEGORY;
	}

	@Override
	public RecipeSerializer<? extends CookingPotRecipe> getSerializer() {
		return CanadaMod.COOKING_POT_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<CookingPotRecipe> getType() {
		return CanadaMod.COOKING_POT_RECIPE_TYPE;
	}

    @Override
	public boolean isSpecial() {
		return false;
	}

    @Override
	public boolean showNotification() {
		return true;
	}

    @Override
	public String group() {
		return this.group;
	}

	@Override
	public List<RecipeDisplay> display() {
        var containerItem = this.requiresBottle ? Items.GLASS_BOTTLE : (this.requiresBowl ? Items.BOWL : null);
        var containerDisplay = containerItem == null ? SlotDisplay.Empty.INSTANCE : new SlotDisplay.ItemSlotDisplay(containerItem);
        var ingredientDisplays = this.ingredients.stream().map(Ingredient::display).toList();
        var cookingPotItem = CanadaBlocks.COOKING_POT.asItem();
		return List.of(
            new CookingPotRecipeDisplay(ingredientDisplays, containerDisplay,
                new ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(cookingPotItem))
        );
	}

    @Override
    public boolean matches(CookingPotRecipeInput input, Level world) {
        var flagHasBottle = false;
        var flagHasBowl = false;
        // Skip output and fuel slots
        for (int i = 2; i < input.stacks.size(); i++) {
            var stack = input.stacks.get(i);
            if (stack.is(Items.BOWL)) {
                flagHasBowl = true;
            }
            else if (stack.is(Items.GLASS_BOTTLE)) {
                flagHasBottle = true;
            }
            else {
                // Check that all ingredients match an input
                if (!ingredients.stream().anyMatch(ingredient -> stack.isEmpty() || Ingredient.testOptionalIngredient(Optional.of(ingredient), stack))) {
                    return false;
                }
            }

        }

        // Check that all inputs match an ingredient
        for (var ingredient : ingredients) {
            if (!input.stacks.stream().anyMatch(stack -> Ingredient.testOptionalIngredient(Optional.of(ingredient), stack))) {
                return false;
            }
        }

        if (ingredients.size() != input.stacks.subList(3, input.stacks.size()).stream().filter(x -> !x.isEmpty()).count()) {
            return false;
        }

        if (this.requiresBottle != flagHasBottle) {
            return false;
        }
        if (this.requiresBowl != flagHasBowl) {
            return false;
        }

        return true;
    }

    public static class Serializer implements RecipeSerializer<CookingPotRecipe> {
		public static final MapCodec<CookingPotRecipe> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
					ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					Ingredient.CODEC.listOf(1, 4).fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                    Codec.BOOL.optionalFieldOf("requiresBowl", false).forGetter(recipe -> recipe.requiresBowl),
                    Codec.BOOL.optionalFieldOf("requiresBottle", false).forGetter(recipe -> recipe.requiresBottle)
				)
				.apply(instance, CookingPotRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipe> PACKET_CODEC = StreamCodec.of(
				CookingPotRecipe.Serializer::write, CookingPotRecipe.Serializer::read
		);

		@Override
		public MapCodec<CookingPotRecipe> codec() {
			return CODEC;
		}

		@Deprecated
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipe> streamCodec() {
			return PACKET_CODEC;
		}

		private static CookingPotRecipe read(RegistryFriendlyByteBuf buf) {
			var string = buf.readUtf();
			var result = ItemStack.STREAM_CODEC.decode(buf);
            var ingredientsCount = buf.readInt();
            var ingredients = new ArrayList<Ingredient>();
            for (int i = 0; i < ingredientsCount; i++) {
			    ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }
            var requiresBottle = buf.readBoolean();
            var requiresBowl = buf.readBoolean();

			return new CookingPotRecipe(string, result, ingredients, requiresBottle, requiresBowl);
		}

		private static void write(RegistryFriendlyByteBuf buf, CookingPotRecipe recipe) {
			buf.writeUtf(recipe.group);
			ItemStack.STREAM_CODEC.encode(buf, recipe.result);
            buf.writeInt(recipe.ingredients.size());
            for (var ingredient : recipe.ingredients) {
			    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
            buf.writeBoolean(recipe.requiresBottle);
            buf.writeBoolean(recipe.requiresBowl);
		}
	}
}
