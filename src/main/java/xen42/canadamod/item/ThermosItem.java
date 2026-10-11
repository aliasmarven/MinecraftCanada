package xen42.canadamod.item;

import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;

import org.jetbrains.annotations.Nullable;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.item.ThermosContentsComponent.Builder;

public class ThermosItem extends Item {
    @Nullable
    private ItemStack currentFoodStack;

    @Nullable
    private Consumable currentConsumableComponent;

    private int selectedIndex = -1;

    public ThermosItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        selectFirstFood(itemStack, user);

        if (currentConsumableComponent != null) {
            return currentConsumableComponent.startConsuming(user, itemStack, hand);
        }

        return InteractionResult.PASS;
    }

    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (currentConsumableComponent != null) {
            var finishedConsumption = currentConsumableComponent.onConsume(world, user, currentFoodStack.copy());

            // If it has a remainder give that instead
            var remainder = currentFoodStack.copy().finishUsingItem(world, user);
            if (remainder != currentFoodStack) {
                finishedConsumption = remainder;
            }

            ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.get(CanadaItems.THERMOS_CONTENTS);
            ThermosContentsComponent.Builder builder = thermosContentsComponent.new Builder(thermosContentsComponent);

            builder.setSelectedStackIndex(selectedIndex);
            builder.removeSelected();

            builder.add(finishedConsumption);
            stack.set(CanadaItems.THERMOS_CONTENTS, builder.build());

            currentConsumableComponent = null;
            currentFoodStack = null;
            selectedIndex = -1;
        }
        return stack;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        if (currentConsumableComponent != null) {
            return currentConsumableComponent.animation();
        }
        else {
            return ItemUseAnimation.NONE;
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        if (currentConsumableComponent != null) {
            return currentConsumableComponent.consumeTicks();
        } else {
            return 0;
        }
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        this.currentConsumableComponent = null;
        currentFoodStack = null;
        selectedIndex = -1;
        return false;
    }

    private void selectFirstFood(ItemStack stack, Player player) {
        var thermosContentsComponent = stack.get(CanadaItems.THERMOS_CONTENTS);
        var builder = thermosContentsComponent.new Builder(thermosContentsComponent);

        for (int index = 0; index < 64; index++) {
            builder.setSelectedStackIndex(index);
            var itemStack = builder.removeSelected();

            if (itemStack != null && itemStack.get(DataComponents.CONSUMABLE) != null) {
                currentFoodStack = itemStack;
                currentConsumableComponent = itemStack.get(DataComponents.CONSUMABLE);
                selectedIndex = index;
                return;
            }
        }

        currentFoodStack = null;
        currentConsumableComponent = null;
        selectedIndex = -1;
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (currentConsumableComponent != null && currentConsumableComponent.shouldEmitParticlesAndSounds(remainingUseTicks)) {
            currentConsumableComponent.emitParticlesAndSounds(user.getRandom(), user, currentFoodStack, 5);
        }
    }

    /// BELOW IS PURE BUNDLE ITEM

    public static final int TOOLTIP_STACKS_COLUMNS = 4;
    public static final int TOOLTIP_STACKS_ROWS = 3;
    public static final int MAX_TOOLTIP_STACKS_SHOWN = 12;
    public static final int MAX_TOOLTIP_STACKS_SHOWN_WHEN_TOO_MANY_TYPES = 11;
    private static final int FULL_ITEM_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    private static final int ITEM_BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);

    public static float getAmountFilled(ItemStack stack) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.getOrDefault(CanadaItems.THERMOS_CONTENTS, ThermosContentsComponent.DEFAULT);
        return thermosContentsComponent.getOccupancy().floatValue();
    }

    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.get(CanadaItems.THERMOS_CONTENTS);
        if (thermosContentsComponent == null) {
            return false;
        } else {
            ItemStack itemStack = slot.getItem();
            ThermosContentsComponent.Builder builder = thermosContentsComponent.new Builder(thermosContentsComponent);
            if (clickType == ClickAction.PRIMARY && !itemStack.isEmpty()) {
                if (builder.add(slot, player) > 0) {
                    playInsertSound(player);
                } else {
                    playInsertFailSound(player);
                }

                stack.set(CanadaItems.THERMOS_CONTENTS, builder.build());
                this.onContentChanged(player);
                return true;
            } else if (clickType == ClickAction.SECONDARY && itemStack.isEmpty()) {
                ItemStack itemStack2 = builder.removeSelected();
                if (itemStack2 != null) {
                    ItemStack itemStack3 = slot.safeInsert(itemStack2);
                    if (itemStack3.getCount() > 0) {
                        builder.add(itemStack3);
                    } else {
                        playRemoveOneSound(player);
                    }
                }

                stack.set(CanadaItems.THERMOS_CONTENTS, builder.build());
                this.onContentChanged(player);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
        if (clickType == ClickAction.PRIMARY && otherStack.isEmpty()) {
            setSelectedStackIndex(stack, -1);
            return false;
        } else {
            ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.get(CanadaItems.THERMOS_CONTENTS);
            if (thermosContentsComponent == null) {
                return false;
            } else {
                ThermosContentsComponent.Builder builder = thermosContentsComponent.new Builder(thermosContentsComponent);
                if (clickType == ClickAction.PRIMARY && !otherStack.isEmpty()) {
                    if (slot.allowModification(player) && builder.add(otherStack) > 0) {
                        playInsertSound(player);
                    } else {
                        playInsertFailSound(player);
                    }

                    stack.set(CanadaItems.THERMOS_CONTENTS, builder.build());
                    this.onContentChanged(player);
                    return true;
                } else if (clickType == ClickAction.SECONDARY && otherStack.isEmpty()) {
                    if (slot.allowModification(player)) {
                        ItemStack itemStack = builder.removeSelected();
                        if (itemStack != null) {
                            playRemoveOneSound(player);
                            cursorStackReference.set(itemStack);
                        }
                    }

                    stack.set(CanadaItems.THERMOS_CONTENTS, builder.build());
                    this.onContentChanged(player);
                    return true;
                } else {
                    setSelectedStackIndex(stack, -1);
                    return false;
                }
            }
        }
    }

    public boolean isBarVisible(ItemStack stack) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.getOrDefault(CanadaItems.THERMOS_CONTENTS, ThermosContentsComponent.DEFAULT);
        return thermosContentsComponent.getOccupancy().compareTo(Fraction.ZERO) > 0;
    }

    public int getBarWidth(ItemStack stack) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.getOrDefault(CanadaItems.THERMOS_CONTENTS, ThermosContentsComponent.DEFAULT);
        return Math.min(1 + Mth.mulAndTruncate(thermosContentsComponent.getOccupancy(), 12), 13);
    }

    public int getBarColor(ItemStack stack) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.getOrDefault(CanadaItems.THERMOS_CONTENTS, ThermosContentsComponent.DEFAULT);
        return thermosContentsComponent.getOccupancy().compareTo(Fraction.ONE) >= 0 ? FULL_ITEM_BAR_COLOR : ITEM_BAR_COLOR;
    }

    public static void setSelectedStackIndex(ItemStack stack, int selectedStackIndex) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.get(CanadaItems.THERMOS_CONTENTS);
        if (thermosContentsComponent != null) {
            ThermosContentsComponent.Builder builder = thermosContentsComponent.new Builder(thermosContentsComponent);
            builder.setSelectedStackIndex(selectedStackIndex);
            stack.set(CanadaItems.THERMOS_CONTENTS, builder.build());
        }
    }

    public static boolean hasSelectedStack(ItemStack stack) {
        ThermosContentsComponent ThermosContentsComponent = (ThermosContentsComponent)stack.get(CanadaItems.THERMOS_CONTENTS);
        return ThermosContentsComponent != null && ThermosContentsComponent.getSelectedStackIndex() != -1;
    }

    public static int getSelectedStackIndex(ItemStack stack) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.getOrDefault(CanadaItems.THERMOS_CONTENTS, ThermosContentsComponent.DEFAULT);
        return thermosContentsComponent.getSelectedStackIndex();
    }

    public static ItemStack getSelectedStack(ItemStack stack) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.get(CanadaItems.THERMOS_CONTENTS);
        return thermosContentsComponent != null && thermosContentsComponent.getSelectedStackIndex() != -1 ? thermosContentsComponent.get(thermosContentsComponent.getSelectedStackIndex()) : ItemStack.EMPTY;
    }

    public static int getNumberOfStacksShown(ItemStack stack) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)stack.getOrDefault(CanadaItems.THERMOS_CONTENTS, ThermosContentsComponent.DEFAULT);
        return thermosContentsComponent.getNumberOfStacksShown();
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        TooltipDisplay tooltipDisplayComponent = (TooltipDisplay)stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        return !tooltipDisplayComponent.shows(CanadaItems.THERMOS_CONTENTS) ? Optional.empty() : Optional.ofNullable((ThermosContentsComponent)stack.get(CanadaItems.THERMOS_CONTENTS)).map(ThermosTooltipData::new);
    }

    public void onDestroyed(ItemEntity entity) {
        ThermosContentsComponent thermosContentsComponent = (ThermosContentsComponent)entity.getItem().get(CanadaItems.THERMOS_CONTENTS);
        if (thermosContentsComponent != null) {
            entity.getItem().set(CanadaItems.THERMOS_CONTENTS, ThermosContentsComponent.DEFAULT);
            ItemUtils.onContainerDestroyed(entity, java.util.stream.StreamSupport.stream(thermosContentsComponent.iterateCopy().spliterator(), false));
        }
    }

    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertFailSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
    }

    private void onContentChanged(Player user) {
        AbstractContainerMenu screenHandler = user.containerMenu;
        if (screenHandler != null) {
            screenHandler.slotsChanged(user.getInventory());
        }

    }
}
