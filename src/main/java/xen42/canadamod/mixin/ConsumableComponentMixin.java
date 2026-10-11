package xen42.canadamod.mixin;

import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xen42.canadamod.item.DurabilityFoodItem;

@Mixin(Consumable.class)
public abstract class ConsumableComponentMixin {
    @Shadow
    public abstract List<ConsumeEffect> onConsumeEffects();

    @Shadow
    public abstract ItemUseAnimation animation();

    @Inject(at = @At("HEAD"), method = "onConsume", cancellable = true)
    public void finishConsumption(Level world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        if (stack.getItem() instanceof DurabilityFoodItem) {
            var thisConsumable = (Consumable)(Object)this;

            var random = user.getRandom();
            thisConsumable.emitParticlesAndSounds(random, user, stack, 16);
            if (user instanceof ServerPlayer serverPlayerEntity) {
                serverPlayerEntity.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            }

            stack.getAllOfType(ConsumableListener.class).forEach((consumable) -> {
                consumable.onConsume(world, user, stack, thisConsumable);
            });
            if (!world.isClientSide()) {
                this.onConsumeEffects().forEach((effect) -> {
                    effect.apply(world, stack, user);
                });
            }

            user.gameEvent(this.animation() == ItemUseAnimation.DRINK ? GameEvent.DRINK : GameEvent.EAT);
            if (!user.hasInfiniteMaterials() && user instanceof Player player) {
                stack.hurtWithoutBreaking(1, player);
            }
            if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
                stack.consume(1, user);
            }
            info.setReturnValue(stack);
            info.cancel();
        }
    }
}
