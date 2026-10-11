package xen42.canadamod.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xen42.canadamod.entities.DuckEntity;
import xen42.canadamod.entities.GooseEntity;

@Mixin(Chicken.class)
public abstract class ChickenEntityMixin extends Animal{

    protected ChickenEntityMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
	public void tickMovementOverride(CallbackInfo info) {
        if ((Object)this instanceof DuckEntity || (Object)this instanceof GooseEntity) {
            // Only call super
            super.aiStep();
            info.cancel();
        }

    }

}
