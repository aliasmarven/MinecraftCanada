package xen42.canadamod.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaSounds;

public class DuckEntity extends Chicken {

    public DuckEntity(EntityType<? extends Chicken> entityType, Level world) {
        super(entityType, world);
        // Override that chickens don't like water
        this.setPathfindingMalus(PathType.WATER, 0f);
    }

    public static AttributeSupplier.Builder createDuckAttributes() {
        return Animal.createAnimalAttributes()
            .add(Attributes.MAX_HEALTH, 4.0f)
            .add(Attributes.MOVEMENT_SPEED, 0.25f)
            .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 2f)
            .add(Attributes.OXYGEN_BONUS);
    }

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
		this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Fox.class, 8.0F, 1.6, 1.4, entity -> true));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, stack -> stack.is(Items.BREAD), false));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
	}

    @Override
	public Chicken getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
		Chicken chickenEntity = CanadaMod.DUCK_ENTITY.create(serverWorld, EntitySpawnReason.BREEDING);

		return chickenEntity;
	}

    @Override
	public boolean isFood(ItemStack stack) {
		return stack.is(Items.BREAD);
	}

    @Override
	public void aiStep() {
		super.aiStep();
		this.oFlap = this.flap;
		this.oFlapSpeed = this.flapSpeed;
		this.flapSpeed = this.flapSpeed + (this.onGround() && !this.isInWater() ? -1.0F : 4.0F) * 0.3F;
		this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
		if (!this.onGround() && this.flapping < 1.0F && !this.isInWater()) {
			this.flapping = 1.0F;
		}

		this.flapping *= 0.9F;
		Vec3 vec3d = this.getDeltaMovement();
		if (!this.onGround() && vec3d.y < 0.0) {
			this.setDeltaMovement(vec3d.multiply(1.0, 0.6, 1.0));
		}

		this.flap = this.flap + this.flapping * 2.0F;
		if (this.level() instanceof ServerLevel serverWorld && this.isAlive() && !this.isBaby() && !this.isChickenJockey() && --this.eggTime <= 0) {
            var item = this.spawnAtLocation(serverWorld, CanadaItems.DUCK_EGG);
            item.setPos(this.position());

            this.playSound(CanadaSounds.SOUND_DUCK_PLOP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.gameEvent(GameEvent.ENTITY_PLACE);

			this.eggTime = this.random.nextInt(6000) + 6000;
		}
	}

	@Override
	public void travel(Vec3 movementInput) {
		super.travel(movementInput);
		if (this.isInWater()) {
			this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.03f, this.getDeltaMovement().z);
		}
	}

    @Override
	protected SoundEvent getAmbientSound() {
		return CanadaSounds.SOUND_DUCK_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return CanadaSounds.SOUND_DUCK_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CanadaSounds.SOUND_DUCK_DEATH;
	}

    @Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(CanadaSounds.SOUND_DUCK_FOOTSTEP, 0.15F, 1.0F);
	}

	@Override
    public ItemStack getPickResult() {
        return new ItemStack(CanadaItems.DUCK_SPAWN_EGG);
    }
}
