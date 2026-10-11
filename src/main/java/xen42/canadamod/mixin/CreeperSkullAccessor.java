package xen42.canadamod.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public interface CreeperSkullAccessor {

    @Accessor("droppedSkulls")
    boolean canadamod$getDroppedSkulls();

    @Accessor("droppedSkulls")
    void canadamod$setDroppedSkulls(boolean droppedSkulls);
}
