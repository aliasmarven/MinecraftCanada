package xen42.canadamod.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.recipe.CookingPotRecipe;

public class CookingPotScreenHandler extends RecipeBookMenu {

    public final static int OUTPUT_SLOT = 0;
    public final static int FUEL_SLOT = 1;
    public final static int CONTAINER_SLOT = 2;
    public final static int INPUT_SLOTS_START = 3;
    public final static int INPUT_SLOTS_END = 6;

    public static final int INVENTORY_SLOTS_START = 7;
    public static final int INVENTORY_SLOTS_END = 33;
    public static final int HOTBAR_SLOTS_START = 34;
    public static final int HOTBAR_SLOTS_END = 42;

    public static final int MAX_WIDTH_AND_HEIGHT = 2;

    private final ContainerData propertyDelegate;

    public Container inventory;

    public ContainerLevelAccess context;

    private final Player player;

    private Slot[] _slots;
    private Slot _fuelSlot;
    private Slot _containerSlot;
    private Slot _outputSlot;

    public List<Slot> getInputSlots() {
        return Arrays.asList(_slots);
    }

    public Slot getFuelSlot() {
        return _fuelSlot;
    }

    public Slot getContainerSlot() {
        return _containerSlot;
    }

    public Slot getOutputSlot() {
        return _outputSlot;
    }

    public CookingPotScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL, null, new SimpleContainerData(4));
    }

    public CookingPotScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context, Container inventory, ContainerData delegate) {
        super(CanadaMod.COOKING_POT_SCREEN_HANDLER_TYPE, syncId);
        this.inventory = inventory == null ? new CookingPotSimpleInventory(this, 8) : inventory;
        this.context = context;
        this.player = playerInventory.player;

        checkContainerSize(this.inventory, 8);
        this.propertyDelegate = delegate;

        _outputSlot = this.addSlot(new OutputSlot(this, this.inventory, OUTPUT_SLOT, 141, 35));
        _fuelSlot = this.addSlot(new FuelSlot(this, this.inventory, FUEL_SLOT, 80, 61));
        _containerSlot = this.addSlot(new ContainerSlot(this, this.inventory, CONTAINER_SLOT, 23, 35));

        _slots = new Slot[] {
            this.addSlot(new CustomSlot(this, this.inventory, INPUT_SLOTS_START, 71, 8)),
            this.addSlot(new CustomSlot(this, this.inventory, INPUT_SLOTS_START+1, 89, 8)),
            this.addSlot(new CustomSlot(this, this.inventory, INPUT_SLOTS_START+2, 71, 26)),
            this.addSlot(new CustomSlot(this, this.inventory, INPUT_SLOTS_START+3, 89, 26))
        };

        this.addStandardInventorySlots(playerInventory, 8, 84);

        this.addDataSlots(this.propertyDelegate);
    }

    private int getSlotWithIngredient(Ingredient ingredient) {
        for (int i = 0; i < slots.size(); i++) {
            if (Ingredient.testOptionalIngredient(Optional.of(ingredient), this.slots.get(i).getItem())) {
                return i;
            }
        }
        return -1;
    }

    private int[] getSlotsWithIngredient(Ingredient ingredient) {
        var matches = new ArrayList<Integer>();
        for (int i = INVENTORY_SLOTS_START; i < slots.size(); i++) {
            if (Ingredient.testOptionalIngredient(Optional.of(ingredient), this.slots.get(i).getItem())) {
                matches.add(i);
            }
        }
        return matches.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public PostPlaceAction handlePlacement(boolean craftAll, boolean creative, RecipeHolder<?> recipeEntry, ServerLevel world, Inventory inventory) {
        var recipe = (CookingPotRecipe)recipeEntry.value();

        // Clear stuff
        // If we can't clear, don't show ghost recipe
        this.quickMoveStack(player, CONTAINER_SLOT);
        for (int i = INPUT_SLOTS_START; i <= INPUT_SLOTS_END; i++) {
            if (this.slots.get(i).getItem().isEmpty()) {
                continue;
            }
            if (this.quickMoveStack(player, i).isEmpty()) {
                return RecipeBookMenu.PostPlaceAction.NOTHING;
            }
        }

        var outputSlot = getSlot(OUTPUT_SLOT);
        if (!outputSlot.getItem().isEmpty() && !outputSlot.getItem().is(recipe.result.item().value())) {
            this.quickMoveStack(player, OUTPUT_SLOT);
        }

        var bottleSlot = getSlotWithIngredient(Ingredient.of(Items.GLASS_BOTTLE));
        var bowlSlot = getSlotWithIngredient(Ingredient.of(Items.BOWL));
        var ingredientSlots = new int[recipe.ingredients.size()];

        for (int j = 0; j < recipe.ingredients.size(); j++) {
            ingredientSlots[j] = getSlotWithIngredient(recipe.ingredients.get(j));
        }

        if ((recipe.requiresBottle && bottleSlot == -1) || (recipe.requiresBowl && bowlSlot == -1) || Arrays.stream(ingredientSlots).anyMatch(x -> x == -1)) {
            return RecipeBookMenu.PostPlaceAction.PLACE_GHOST_RECIPE;
        }
        else {
            // Surely theres a better way
            if (recipe.requiresBottle) {
                for (int slot : getSlotsWithIngredient(Ingredient.of(Items.GLASS_BOTTLE))) {
                    this.moveItemStackTo(this.slots.get(slot).getItem(), CONTAINER_SLOT, CONTAINER_SLOT + 1, false);
                }
            }
            if (recipe.requiresBowl) {
                for (int slot : getSlotsWithIngredient(Ingredient.of(Items.BOWL))) {
                    this.moveItemStackTo(this.slots.get(slot).getItem(), CONTAINER_SLOT, CONTAINER_SLOT + 1, false);
                }
            }

            var ingredientCounts = recipe.ingredients.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
            int start = INPUT_SLOTS_START;
            for (var entry : ingredientCounts.entrySet()) {
                var ingredient = entry.getKey();
                var count = entry.getValue();

                var slots = getSlotsWithIngredient(ingredient);
                var end = start + (int)(long)count;
                for (int slot : slots) {
                    this.moveItemStackTo(this.slots.get(slot).getItem(), start, end, false);
                }
                if (count > 1) {
                    // Equalize them
                    var stacks = IntStream.range(start, end).mapToObj(x -> this.slots.get(x)).collect(Collectors.toList());
                    equalizeStacks(stacks);
                }
                start = end;
            }

            return RecipeBookMenu.PostPlaceAction.NOTHING;
        }
    }

    public static void equalizeStacks(List<Slot> slots) {
        var totalItems = slots.stream().mapToInt(slot -> slot.getItem().getCount()).sum();
        var numStacks = slots.size();
        var itemsPerStack = totalItems / numStacks;
        var remainder = totalItems % numStacks;
        var item = slots.get(0).getItem().getItem();

        for (int i = 0; i < numStacks; i++) {
            int count = itemsPerStack + (i < remainder ? 1 : 0);
            slots.get(i).setByPlayer(new ItemStack(item, count));
        }
    }

    public boolean isCooking() {
        return this.getCookProgress() > 0f;
    }

    public float getFuelProgress() {
        return (float)this.propertyDelegate.get(0) / (float)this.propertyDelegate.get(1);
    }

    public float getCookProgress() {
        return 1f - ((float)this.propertyDelegate.get(2) / (float)this.propertyDelegate.get(3));
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING; // Return crafting because making a RecipeBookType is impossible.
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents finder) {
        if (this.inventory instanceof StackedContentsCompatible recipeInputProvider) {
            recipeInputProvider.fillStackedContents(finder);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.context, player, CanadaBlocks.COOKING_POT);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slotAtIndex = this.slots.get(slot);
        if (slotAtIndex != null && slotAtIndex.hasItem() && slotAtIndex.mayPickup(player)) {
            ItemStack itemStackAtIndex = slotAtIndex.getItem();
            itemStack = itemStackAtIndex.copy();
            if (slot == OUTPUT_SLOT) {
                itemStackAtIndex.getItem().onCraftedBy(itemStackAtIndex, player);
                if (!this.moveItemStackTo(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END, true)) {
                    return ItemStack.EMPTY;
                }

                slotAtIndex.onQuickCraft(itemStackAtIndex, itemStack);
            } else if (slot >= INVENTORY_SLOTS_START && slot < HOTBAR_SLOTS_END) {
                if (isContainer(itemStackAtIndex)) {
                    this.moveItemStackTo(itemStackAtIndex, CONTAINER_SLOT, CONTAINER_SLOT + 1, false);
                }
                else if (isFuel(itemStackAtIndex)) {
                    this.moveItemStackTo(itemStackAtIndex, FUEL_SLOT, FUEL_SLOT + 1, false);
                }
                else {
                    if (!this.moveItemStackTo(itemStackAtIndex, INPUT_SLOTS_START, INVENTORY_SLOTS_START, false)) {
                        if (slot < HOTBAR_SLOTS_START) {
                            if (!this.moveItemStackTo(itemStackAtIndex, HOTBAR_SLOTS_START, HOTBAR_SLOTS_END, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (!this.moveItemStackTo(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_START, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else if (!this.moveItemStackTo(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStackAtIndex.isEmpty()) {
                slotAtIndex.setByPlayer(ItemStack.EMPTY);
            } else {
                slotAtIndex.setChanged();
            }

            if (itemStackAtIndex.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slotAtIndex.onTake(player, itemStackAtIndex);
            if (slot == OUTPUT_SLOT) {
                player.drop(itemStackAtIndex, false, net.minecraft.util.Prediction.SERVER_ONLY);
            }
        }

        return itemStack;
    }

    public boolean isFuel(ItemStack item) {
        return item.has(net.minecraft.core.component.DataComponents.COOKING_FUEL);
    }

    public static boolean isContainer(ItemStack item) {
        return item.is(Items.BOWL) || item.is(Items.GLASS_BOTTLE);
    }

    private class CookingPotSimpleInventory extends SimpleContainer implements CraftingContainer {
        private AbstractContainerMenu _screen;

        CookingPotSimpleInventory(CookingPotScreenHandler screen, int size) {
            super(size);
            _screen = screen;
        }

        public void setChanged() {
            _screen.slotsChanged(this);
            super.setChanged();
        }

        @Override
        public int getWidth() {
            return MAX_WIDTH_AND_HEIGHT;
        }

        @Override
        public int getHeight() {
            return MAX_WIDTH_AND_HEIGHT;
        }
    }

    private class CustomSlot extends Slot {
        protected CookingPotScreenHandler _handler;
        public CustomSlot(CookingPotScreenHandler handler, Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            _handler = handler;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            _handler.slotsChanged(_handler.inventory);
            _handler.inventory.setChanged();
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !_handler.isContainer(stack);
        }
    }

    private class ContainerSlot extends CustomSlot {
        public ContainerSlot(CookingPotScreenHandler handler, Container inventory, int index, int x, int y) {
            super(handler, inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return _handler.isContainer(stack);
        }
    }

    private class OutputSlot extends CustomSlot {
        public OutputSlot(CookingPotScreenHandler handler, Container inventory, int index, int x, int y) {
            super(handler, inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    private class FuelSlot extends CustomSlot {
        public FuelSlot(CookingPotScreenHandler handler, Container inventory, int index, int x, int y) {
            super(handler, inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return _handler.isFuel(stack);
        }
    }
}
