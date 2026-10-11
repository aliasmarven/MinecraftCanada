package xen42.canadamod.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {
    @Shadow
    protected abstract boolean isEquipped(Item item);

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        var player = (Player)(Object)this;

        var doBeaver = isEquipped(CanadaItems.BEAVER_HELMET) && player.getMainHandItem().is(ItemTags.AXES);
        var doMoose = isEquipped(CanadaItems.MOOSE_HELMET);

        if (doMoose) {
            player.addEffect(new MobEffectInstance(CanadaMod.MOOSE_EFFECT, 110, 0, false, false, true));
        }
        if (doBeaver) {
            player.addEffect(new MobEffectInstance(CanadaMod.BEAVER_EFFECT, 110, 0, false, false, true));
        }
    }
}
