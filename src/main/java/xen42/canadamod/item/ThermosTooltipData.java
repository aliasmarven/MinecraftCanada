package xen42.canadamod.item;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record ThermosTooltipData(ThermosContentsComponent contents) implements TooltipComponent {
    public ThermosTooltipData(ThermosContentsComponent contents) {
        this.contents = contents;
    }

    public ThermosContentsComponent contents() {
        return this.contents;
    }
}
