package xen42.canadamod.mixin.client;

import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractSignEditScreen.class)
public class AbstractSignEditScreenMixin {
    @Shadow
	@Mutable
	protected WoodType woodType;

	@Shadow
	@Mutable
	protected SignBlockEntity sign;
}
