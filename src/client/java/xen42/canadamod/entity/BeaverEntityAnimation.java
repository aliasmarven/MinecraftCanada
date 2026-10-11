package xen42.canadamod.entity;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class BeaverEntityAnimation {
    public static final AnimationDefinition CHOP = AnimationDefinition.Builder.withLength(4.0F)
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.25F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.25F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.75F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();
}
