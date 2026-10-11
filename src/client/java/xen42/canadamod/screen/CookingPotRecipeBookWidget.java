package xen42.canadamod.screen;

import java.util.List;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.recipe.CookingPotRecipeDisplay;

public class CookingPotRecipeBookWidget extends RecipeBookComponent<CookingPotScreenHandler> {
	private static final Component TOGGLE_CRAFTABLE_TEXT = Component.translatable("gui.recipebook.toggleRecipes.craftable");

	private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
		new RecipeBookComponent.TabInfo(CanadaBlocks.COOKING_POT.asItem(), CanadaMod.COOKING_POT_RECIPE_BOOK_CATEGORY)
	);

	private static final WidgetSprites TEXTURES = new WidgetSprites(
		Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
		Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
		Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"),
		Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted")
	);

    public CookingPotRecipeBookWidget(CookingPotScreenHandler craftingScreenHandler) {
        super(craftingScreenHandler, TABS);
    }

    @Override
    protected Component getRecipeFilterName() {
		return TOGGLE_CRAFTABLE_TEXT;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
		return this.menu.getOutputSlot() == slot || this.menu.getInputSlots().contains(slot) ||
        this.menu.getContainerSlot() == slot || this.menu.getFuelSlot() == slot;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection recipeResultCollection, StackedItemContents recipeFinder) {
		recipeResultCollection.selectRecipes(recipeFinder, this::canDisplay);
    }

    private boolean canDisplay(RecipeDisplay display) {
		return true;
	}

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return TEXTURES;
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostRecipe, RecipeDisplay display, ContextMap context) {
		var cookingPotDisplay = (CookingPotRecipeDisplay)display;
		ghostRecipe.setResult(this.menu.getOutputSlot(), context, cookingPotDisplay.result());

		List<Slot> inputSlots = this.menu.getInputSlots();
		List<SlotDisplay> ingredients = cookingPotDisplay.ingredients();
		for (int i = 0; i < inputSlots.size(); i++) {
			if (i >= ingredients.size()) {
				continue;
			}
			ghostRecipe.setInput(inputSlots.get(i), context, ingredients.get(i));
		}

		ghostRecipe.setInput(this.menu.getContainerSlot(), context, cookingPotDisplay.containers());
    }
}
