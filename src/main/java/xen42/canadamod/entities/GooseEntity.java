package xen42.canadamod.entities;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
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
import org.jetbrains.annotations.Nullable;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaSounds;

public class GooseEntity extends Chicken implements NeutralMob {

	private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
	private int angerTime;
	@Nullable
	private UUID angryAt;

	private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(GooseEntity.class, EntityDataSerializers.BOOLEAN);

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ANGRY, false);
	}

	public GooseEntity(EntityType<? extends Chicken> entityType, Level world) {
        super(entityType, world);
        // Override that chickens don't like water
        this.setPathfindingMalus(PathType.WATER, 0f);
    }

    public static AttributeSupplier.Builder createGooseAttributes() {
        return Animal.createAnimalAttributes()
            .add(Attributes.MAX_HEALTH, 8.0f)
            .add(Attributes.MOVEMENT_SPEED, 0.25f)
            .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 2f)
            .add(Attributes.OXYGEN_BONUS)
			.add(Attributes.ATTACK_DAMAGE, 0.5);
    }

	@Override
	protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
		super.usePlayerItem(player, hand, stack);
		this.setHealth(this.getMaxHealth());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new GooseEntity.AttackGoal());
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
		this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Player.class, 8.0F, 1.6, 1.4,
			entity -> this.getHealth() <= 4 && !((Player)entity).isHolding(Items.BREAD)));
		this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Fox.class, 8.0F, 1.6, 1.4, entity -> !this.isAngry()));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, stack -> stack.is(Items.BREAD), false));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(1, new GooseEntity.GooseRevengeGoal());
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
		this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
	}

    @Override
	public Chicken getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
		Chicken chickenEntity = CanadaMod.GOOSE_ENTITY.create(serverWorld, EntitySpawnReason.BREEDING);

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

		var shouldFlap = (!this.onGround() && !this.isInWater()) || this.isAngry();

		this.flapSpeed = this.flapSpeed + (!shouldFlap ? -1.0F : 4.0F) * 0.3F;
		this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
		if (shouldFlap && this.flapping < 1.0F) {
			this.flapping = 1.0F;
		}

		this.flapping *= 0.9F;
		Vec3 vec3d = this.getDeltaMovement();
		if (!this.onGround() && vec3d.y < 0.0) {
			this.setDeltaMovement(vec3d.multiply(1.0, 0.6, 1.0));
		}

		this.flap = this.flap + this.flapping * 2.0F;
		if (this.level() instanceof ServerLevel serverWorld && this.isAlive() && !this.isBaby() && !this.isChickenJockey() && --this.eggTime <= 0) {
            var item = this.spawnAtLocation(serverWorld, CanadaItems.GOOSE_EGG);
            item.setPos(this.position());

            this.playSound(CanadaSounds.SOUND_GOOSE_PLOP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
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

    // TODO: Get duck sounds
    @Override
	protected SoundEvent getAmbientSound() {
		return CanadaSounds.SOUND_GOOSE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return CanadaSounds.SOUND_GOOSE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CanadaSounds.SOUND_GOOSE_DEATH;
	}

	public SoundEvent getWarningSound() {
		return CanadaSounds.SOUND_GOOSE_WARNING;
	}

    @Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(CanadaSounds.SOUND_DUCK_FOOTSTEP, 0.15F, 1.0F);
	}

	@Override
    public ItemStack getPickResult() {
        return new ItemStack(CanadaItems.GOOSE_SPAWN_EGG);
    }

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide) {
			this.updatePersistentAnger((ServerLevel)this.level(), false);
			// Make them give up if hurt too much
			if (this.getHealth() < 2 && this.isAngry()) {
				this.stopBeingAngry();
			}
		}
	}

	@Override
	public void stopBeingAngry() {
		this.entityData.set(ANGRY, false);
		NeutralMob.super.stopBeingAngry();
	}

	@Override
	public void updatePersistentAnger(ServerLevel world, boolean angerPersistent) {
		if (this.getHealth() <= 4) {
			this.stopBeingAngry();
		}
		else {
			NeutralMob.super.updatePersistentAnger(world, angerPersistent);
		}
	}

	@Override
	public boolean isAngryAt(LivingEntity entity, ServerLevel world) {
		if (this.getHealth() <= 4) {
			return false;
		}
		else {
			return NeutralMob.super.isAngryAt(entity, world);
		}
	}

	public boolean isAngry() {
		return this.entityData.get(ANGRY);
	}

	class AttackGoal extends MeleeAttackGoal {
		public AttackGoal() {
			super(GooseEntity.this, 1.25, true);
		}

		@Override
		public void tick() {
			super.tick();
			if (GooseEntity.this.getHealth() <= 4) {
				this.stop();
			}
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity target) {
			if (this.canPerformAttack(target)) {
				this.resetAttackCooldown();
				this.mob.doHurtTarget(getServerLevel(this.mob), target);
			} else if (this.mob.distanceToSqr(target) < (target.getBbWidth() + 4.0F) * (target.getBbWidth() + 4.0F)) {
				if (this.isTimeToAttack()) {
					this.resetAttackCooldown();
				}

				if (this.getTicksUntilNextAttack() <= 5 && this.mob.getRandom().nextInt(2) == 0) {
					GooseEntity.this.makeSound(GooseEntity.this.getWarningSound());
				}
			} else {
				this.resetAttackCooldown();
			}
		}
	}

	class GooseRevengeGoal extends HurtByTargetGoal {
		public GooseRevengeGoal() {
			super(GooseEntity.this);
		}

		@Override
		public void start() {
			super.start();
			if (GooseEntity.this.isBaby()) {
				this.alertOthers();
				this.stop();
			}
		}

		@Override
		protected void alertOther(Mob mob, LivingEntity target) {
			if (mob instanceof GooseEntity && !mob.isBaby()) {
				super.alertOther(mob, target);
			}
		}

		@Override
		public void tick() {
			super.tick();
			if (GooseEntity.this.getHealth() <= 4) {
				this.stop();
			}
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.readPersistentAngerSaveData(this.level(), nbt);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		this.addPersistentAngerSaveData(nbt);
	}

	@Override
	public void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
	}

	@Override
	public void setRemainingPersistentAngerTime(int angerTime) {
		this.angerTime = angerTime;
		if (angerTime > 0) {
			this.entityData.set(ANGRY, true);
		}
	}

	@Override
	public int getRemainingPersistentAngerTime() {
		return this.angerTime;
	}

	@Override
	public void setPersistentAngerTarget(@Nullable UUID angryAt) {
		this.angryAt = angryAt;
		if (angryAt != null) {
			this.entityData.set(ANGRY, true);
		}
	}

	@Nullable
	@Override
	public UUID getPersistentAngerTarget() {
		return this.angryAt;
	}
}
