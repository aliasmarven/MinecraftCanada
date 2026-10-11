package xen42.canadamod.mixin;

import net.minecraft.world.entity.monster.zombie.Drowned;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.entities.BeaverEntity;
import xen42.canadamod.entities.DrownedFleeEntityGoal;

@Mixin(Drowned.class)
public class DrownedEntityMixin {
    @Inject(at = @At("HEAD"), method = "addBehaviourGoals")
    protected void initCustomGoals(CallbackInfo info) {
        var drowned = (Drowned)(Object)this;
        drowned.goalSelector.addGoal(1, new DrownedFleeEntityGoal(drowned, BeaverEntity.class, 12.0F, 1.0D, 1.2D));
    }
}
