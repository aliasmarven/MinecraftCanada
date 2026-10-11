package xen42.canadamod.mixin.client;

import java.util.List;
import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xen42.canadamod.gui.ThermosTooltipComponent;
import xen42.canadamod.item.ThermosTooltipData;

// Have to patch all uses of TooltipComponent::of (for TooltipData)

@Mixin(GuiGraphics.class)
public class DrawContextMixin {
    @Inject(at = @At("HEAD"),
        method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/resources/ResourceLocation;)V",
        cancellable = true)
	private void drawTooltip(Font textRenderer, List<Component> text, Optional<TooltipComponent> data, int x, int y, @Nullable ResourceLocation texture, CallbackInfo info) {
        if (data.isPresent() && data.get() instanceof ThermosTooltipData thermosTooltipData) {
            var list = (List)text.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).collect(Util.toMutableList());

            list.add(list.isEmpty() ? 0 : 1, new ThermosTooltipComponent(thermosTooltipData.contents()));

            var drawContext = (GuiGraphics)(Object)this;
            drawContext.renderTooltipInternal(textRenderer, list, x, y, DefaultTooltipPositioner.INSTANCE, texture);

            info.cancel();
        }
	}
}
