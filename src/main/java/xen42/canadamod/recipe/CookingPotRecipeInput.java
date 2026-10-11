package xen42.canadamod.recipe;

import java.util.List;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class CookingPotRecipeInput implements RecipeInput {

    public final List<ItemStack> stacks;
    private final StackedItemContents matcher = new StackedItemContents();
    private final int stackCount;

	public CookingPotRecipeInput(List<ItemStack> stacks) {
		this.stacks = stacks;
		int i = 0;

		for (ItemStack itemStack : stacks) {
			if (!itemStack.isEmpty()) {
				i++;
				this.matcher.accountStack(itemStack, 1);
			}
		}

		this.stackCount = i;
	}

    public static CookingPotRecipeInput create(List<ItemStack> stacks) {
		return new CookingPotRecipeInput(stacks);
	}

	@Override
	public String toString() {
		var str = "";
		for (var stack : stacks) {
			str += stack.toString() + " ";
		}
		return str;
	}

	@Override
	public ItemStack getItem(int slot) {
		return (ItemStack)this.stacks.get(slot);
	}

	@Override
	public int size() {
		return this.stacks.size();
	}

    @Override
	public boolean isEmpty() {
		return this.stackCount == 0;
	}
}
