package xen42.canadamod.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaSounds;

public class GrizzlyEntity extends PolarBear {

    public GrizzlyEntity(EntityType<? extends PolarBear> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createGrizzlyAttributes() {
		return Animal.createAnimalAttributes()
			.add(Attributes.MAX_HEALTH, 30.0)
			.add(Attributes.FOLLOW_RANGE, 20.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Override
	public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		return CanadaMod.GRIZZLY_ENTITY.create(world, EntitySpawnReason.BREEDING);
	}

	@Override
    public ItemStack getPickResult() {
        return new ItemStack(CanadaItems.GRIZZLY_SPAWN_EGG);
    }

        @Override
	protected SoundEvent getAmbientSound() {
		return this.isBaby() ? CanadaSounds.ENTITY_GRIZZLY_BEAR_AMBIENT_BABY : CanadaSounds.ENTITY_GRIZZLY_BEAR_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return CanadaSounds.ENTITY_GRIZZLY_BEAR_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CanadaSounds.ENTITY_GRIZZLY_BEAR_DEATH;
	}

    private int nextGrizzlyWarningTick = 0;

    @Override
    protected void playWarningSound() {
        if (this.tickCount >= this.nextGrizzlyWarningTick) {
            this.makeSound(CanadaSounds.ENTITY_GRIZZLY_BEAR_WARNING);
            this.nextGrizzlyWarningTick = this.tickCount + 40;
        }
    }
}
