package xen42.canadamod.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class BeaverEntityRenderState extends LivingEntityRenderState {
    public final AnimationState chopAnimationState = new AnimationState();
    public boolean isFatigued;
    public boolean isFrenzied;

    public BeaverEntityRenderState() {
        super();
    }
}
