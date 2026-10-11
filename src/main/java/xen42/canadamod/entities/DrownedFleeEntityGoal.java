package xen42.canadamod.entities;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.zombie.Drowned;

// This class exists solely to allow patching the tick method to update the nagiation field which is final (can modify only in a mixin)
public class DrownedFleeEntityGoal extends AvoidEntityGoal {

    public Drowned drowned;

    public DrownedFleeEntityGoal(Drowned mob, Class fleeFromType, float distance, double slowSpeed, double fastSpeed) {
        super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
        drowned = mob;
    }
}
