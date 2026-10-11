package xen42.canadamod.mixin;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xen42.canadamod.entities.DrownedFleeEntityGoal;

// Have to patch the FleeEntityGoal class to allow setting the final fleeingEntityNavigation field in our custom DrownedFleeEntityGoal class
@Mixin(AvoidEntityGoal.class)
public class FleeEntityGoalMixin {

    @Shadow @Final @Mutable
    protected PathNavigation pathNav;

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        var goal = (AvoidEntityGoal)(Object)this;
        if (goal instanceof DrownedFleeEntityGoal drownedGoal) {
            this.pathNav = drownedGoal.drowned.getNavigation();
        }
    }
}
