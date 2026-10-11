package xen42.canadamod.mixin.client;

import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.sign.MapleHangingSignBlockEntity;

import javax.swing.text.html.HTMLDocument.BlockElement;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HangingSignEditScreen.class)
public class HangingSignEditScreenMixin {
	@Shadow
	@Mutable
	private ResourceLocation texture;

	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(CallbackInfo info) {
								var superThis = (AbstractSignEditScreenMixin)(Object)this;

		if (superThis.sign instanceof MapleHangingSignBlockEntity) {
			superThis.woodType = CanadaBlocks.MAPLE_WOOD_TYPE;
									this.texture = ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/gui/hanging_signs/maple.png");
		}
	}
}