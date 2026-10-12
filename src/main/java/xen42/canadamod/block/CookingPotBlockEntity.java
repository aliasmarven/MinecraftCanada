package xen42.canadamod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.recipe.CookingPotRecipe;
import xen42.canadamod.recipe.CookingPotRecipeInput;
import xen42.canadamod.screen.CookingPotScreenHandler;

public class CookingPotBlockEntity extends BaseContainerBlockEntity implements StackedContentsCompatible, WorldlyContainer {
    protected NonNullList<ItemStack> inventory;
    private BlockState _blockState;

    public int cookTimeRemaining;
    public int cookTimeTotal;

    public int burnTimeRemaining;
    public int burnTimeTotal;

    public  ResourceKey<Recipe<?>> currentRecipeID;

    private final RecipeManager.CachedCheck<CookingPotRecipeInput, ? extends CookingPotRecipe> matchGetter;

    protected final ContainerData propertyDelegate;

    public CookingPotBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CanadaMod.COOKING_POT_ENTITY, blockPos, blockState);
        this.propertyDelegate = new SimpleContainerData(4);

        this.inventory = NonNullList.withSize(8, ItemStack.EMPTY);
        _blockState = blockState;
        this.matchGetter = RecipeManager.createCheck(CanadaMod.COOKING_POT_RECIPE_TYPE);
    }

    private CookingPotScreenHandler _handler;

    public boolean isCooking() {
        return cookTimeRemaining > 0;
    }

    public boolean hasFuel() {
        return burnTimeRemaining > 0;
    }

    @Override
    public int getContainerSize() {
        return 8;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        _handler = new CookingPotScreenHandler(syncId, playerInventory, ContainerLevelAccess.NULL, this, this.propertyDelegate);
        return _handler;
    }

    @Override
    protected Component getDefaultName() {
        return ((CookingPotBlock)(this._blockState.getBlock())).getTitle();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return CanadaMod.COOKING_POT_ENTITY.isValid(state);
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput nbt) {
        super.loadAdditional(nbt);
        this.inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory);
        this.cookTimeRemaining = nbt.getShortOr("cookTimeRemaining", (short)0);
        this.cookTimeTotal = nbt.getShortOr("cookTimeTotal", (short)0);
        this.burnTimeRemaining = nbt.getShortOr("burnTimeRemaining", (short)0);
        this.burnTimeTotal = nbt.getShortOr("burnTimeTotal", (short)0);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        setChanged();
    }

    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, this.inventory);
        nbt.putShort("cookTimeRemaining", (short)this.cookTimeRemaining);
        nbt.putShort("cookTimeTotal", (short)this.cookTimeTotal);
        nbt.putShort("burnTimeRemaining", (short)this.burnTimeRemaining);
        nbt.putShort("burnTimeTotal", (short)this.burnTimeTotal);
    }

    private static void consumeWithRemainder(
            ServerLevel world, CookingPotBlockEntity blockEntity, int slotIndex) {
        ItemStack stack = blockEntity.inventory.get(slotIndex);
        if (stack.isEmpty()) {
            return;
        }

        var remainder = stack.getItem().getCraftingRemainder();
        stack.shrink(1);

        if (remainder != null) {
            ItemStack returned = remainder.create();

            if (stack.isEmpty()) {
                blockEntity.inventory.set(slotIndex, returned);
            } else {
                BlockPos pos = blockEntity.getBlockPos();
                net.minecraft.world.Containers.dropItemStack(
                    world, pos.getX() + 0.5, pos.getY() + 0.5,
                    pos.getZ() + 0.5, returned);
            }
        }

        blockEntity.setChanged();
    }

    public static void tick(ServerLevel world, BlockPos pos, BlockState state, CookingPotBlockEntity blockEntity) {
        var wasBurning = blockEntity.isCooking();

        // Todo: only check this when the inventory actually changed
        var inputRecipe = getRecipeFromInputs(blockEntity, world);
        var activeRecipe = getCurrentRecipe(blockEntity, world);
        var currentOutput = blockEntity.getItem(CookingPotScreenHandler.OUTPUT_SLOT);
        var currentRecipeResult = inputRecipe == null ? ItemStack.EMPTY : inputRecipe.value().result.create();

        var canAcceptOutput = blockEntity.canAcceptOutput(currentOutput, currentRecipeResult);

        if (currentRecipeResult == ItemStack.EMPTY) {
            blockEntity.cookTimeRemaining = 0;
            blockEntity.cookTimeTotal = 0;
        }

        // Only progress or use fuel if we actually can create an output
        if (canAcceptOutput && currentRecipeResult != ItemStack.EMPTY) {
            var fuelStack = blockEntity.getItem(CookingPotScreenHandler.FUEL_SLOT);
            if (!blockEntity.hasFuel() && !fuelStack.isEmpty()) {
                int burnDuration =
                    net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.getFromItem(
                        fuelStack,
                        net.minecraft.core.component.DataComponents.COOKING_FUEL,
                        net.minecraft.world.item.component.CookingFuel::burnTime,
                        blockEntity.getLootContext(world),
                        0
                    );

                if (burnDuration > 0) {
                    blockEntity.burnTimeRemaining = burnDuration;
                    blockEntity.burnTimeTotal = burnDuration;
                    consumeWithRemainder(world, blockEntity, CookingPotScreenHandler.FUEL_SLOT);
                }
            }

            if (blockEntity.isCooking() && blockEntity.hasFuel()) {
                blockEntity.cookTimeRemaining--;
                blockEntity.burnTimeRemaining-=5;
            }
        }

        var didUpdate = false;

        // Just finished crafting
        if (wasBurning && !blockEntity.isCooking()) {
            didUpdate = craftRecipe(blockEntity, world);
        }

        var shouldBeCooking = inputRecipe != null;
        var isCooking = blockEntity.isCooking() && activeRecipe == inputRecipe;

        if (shouldBeCooking && !isCooking) {
            // Debug: 2 seconds to craft
            blockEntity.cookTimeRemaining = 40;
            blockEntity.cookTimeTotal = 40;
            setCurrentRecipe(inputRecipe, blockEntity, world);
        }
        else if (!shouldBeCooking && isCooking) {
            blockEntity.cookTimeRemaining = 0;
            setCurrentRecipe(null, blockEntity, world);
        }

        var isLit = state.getValue(CookingPotBlock.LIT);
        var shouldBeLit = blockEntity.isCooking() && blockEntity.hasFuel() && canAcceptOutput;
        if (isLit != shouldBeLit) {
            world.setBlockAndUpdate(pos, state.setValue(CookingPotBlock.LIT, shouldBeLit));
            didUpdate = true;
        }

        if (didUpdate) {
            setChanged(world, pos, state);
        }

        blockEntity.propertyDelegate.set(0, blockEntity.burnTimeRemaining);
        blockEntity.propertyDelegate.set(1, blockEntity.burnTimeTotal);
        blockEntity.propertyDelegate.set(2, blockEntity.cookTimeRemaining);
        blockEntity.propertyDelegate.set(3, blockEntity.cookTimeTotal);
    }

    @SuppressWarnings("unchecked")
    private static RecipeHolder<CookingPotRecipe> getRecipeFromInputs(CookingPotBlockEntity blockEntity, ServerLevel world) {
        var input = new CookingPotRecipeInput(blockEntity.inventory);
        var recipeEntry = blockEntity.matchGetter.getRecipeFor(input, world).orElse(null);
        return (RecipeHolder<CookingPotRecipe>)recipeEntry;
    }

    private static void setCurrentRecipe(RecipeHolder<CookingPotRecipe> recipe, CookingPotBlockEntity blockEntity, ServerLevel world) {
        blockEntity.currentRecipeID = recipe == null ? null : recipe.id();
    }

    @SuppressWarnings("unchecked")
    private static RecipeHolder<CookingPotRecipe> getCurrentRecipe(CookingPotBlockEntity blockEntity, ServerLevel world) {
        var recipe = world.recipeAccess().byKey(blockEntity.currentRecipeID).orElse(null);
        if (recipe == null) {
            return null;
        }
        else {
            return (RecipeHolder<CookingPotRecipe>)recipe;
        }
    }

    private boolean canAcceptOutput(ItemStack currentOutput, ItemStack resultItem) {
        return currentOutput.isEmpty() ||
                (currentOutput.is(resultItem.getItem()) && currentOutput.getMaxStackSize() > currentOutput.getCount() + resultItem.getCount());
    }

    /// Called when crafting bar time is up
    private static boolean craftRecipe(CookingPotBlockEntity blockEntity, ServerLevel world) {
        var recipeEntry = getRecipeFromInputs(blockEntity, world);
        if (recipeEntry == null) {
            return false;
        }
        var recipe = recipeEntry.value();

        if (recipe != null) {
            var resultItem = recipe.result.create();

            var currentOutput = blockEntity.getItem(CookingPotScreenHandler.OUTPUT_SLOT);

            if (blockEntity.canAcceptOutput(currentOutput, resultItem)) {
                var finalResult = resultItem.copy();
                if (!currentOutput.isEmpty()) {
                    finalResult = new ItemStack(finalResult.getItem(), finalResult.getCount() + currentOutput.getCount());
                }

                blockEntity.setItem(CookingPotScreenHandler.OUTPUT_SLOT, finalResult);
                for (var slotIndex : new int[] { 2, 3, 4, 5, 6 }) {
                    var slot = blockEntity.inventory.get(slotIndex);

                    if (!slot.isEmpty()) {
                        consumeWithRemainder(world, blockEntity, slotIndex);
                    }
                }

                setCurrentRecipe(null, blockEntity, world);

                return true;
            }
        }
        return false;
    }

    @Override
    public void fillStackedContents(StackedItemContents finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.accountStack(itemStack);
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return (slot == CookingPotScreenHandler.OUTPUT_SLOT) || stack.is(Items.BUCKET);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        if (CookingPotScreenHandler.isContainer(stack)) {
            return slot == CookingPotScreenHandler.CONTAINER_SLOT;
        }
        else if (stack.has(net.minecraft.core.component.DataComponents.COOKING_FUEL)) {
            return slot == CookingPotScreenHandler.FUEL_SLOT;
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return new int[] { CookingPotScreenHandler.OUTPUT_SLOT, 3, 4, 5, 6 };
        }
        else {
            return new int[] { CookingPotScreenHandler.CONTAINER_SLOT, CookingPotScreenHandler.FUEL_SLOT };
        }
    }
}
