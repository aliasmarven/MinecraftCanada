package xen42.canadamod.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.ItemStack;

public class MooseEntityRenderState extends LivingEntityRenderState {
    public ItemStack saddleStack;
    public boolean leftAntlerMissing, rightAntlerMissing, dashing;

    public final AnimationState attackAnimationState = new AnimationState();

    public MooseEntityRenderState() {
        super();
        saddleStack = ItemStack.EMPTY;
        leftAntlerMissing = false;
        rightAntlerMissing = false;
        dashing = false;
    }
}
