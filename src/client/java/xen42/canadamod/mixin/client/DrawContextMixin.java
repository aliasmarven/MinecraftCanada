package xen42.canadamod.mixin.client;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import xen42.canadamod.gui.ThermosTooltipComponent;
import xen42.canadamod.item.ThermosTooltipData;

@Mixin(ClientTooltipComponent.class)
public interface DrawContextMixin {

    @Inject(
        method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void canadamod$createThermosTooltip(
            TooltipComponent data,
            CallbackInfoReturnable<ClientTooltipComponent> cir) {

        if (data instanceof ThermosTooltipData thermosData) {
            cir.setReturnValue(
                new ThermosTooltipComponent(thermosData.contents())
            );
        }
    }
}
