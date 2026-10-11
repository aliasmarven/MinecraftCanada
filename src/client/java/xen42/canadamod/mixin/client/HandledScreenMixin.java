package xen42.canadamod.mixin.client;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xen42.canadamod.CanadaItems;

@Mixin(AbstractContainerScreen.class)
public class HandledScreenMixin {
    @Inject(at = @At("HEAD"),
        method = "showTooltipWithItemInHand",
        cancellable = true)
	private void isItemTooltipSticky(ItemStack item, CallbackInfoReturnable<Boolean> info) {
        if (item.is(CanadaItems.THERMOS)) {
            info.setReturnValue(true);
            info.cancel();
        }
	}
}