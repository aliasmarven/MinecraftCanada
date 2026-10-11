package xen42.canadamod.entities;

import java.util.UUID;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaSounds;

public class MooseEntity extends AbstractHorse implements NeutralMob {
    private static final EntityDataAccessor<Boolean> LEFT_ANTLER_MISSING = SynchedEntityData.defineId(MooseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RIGHT_ANTLER_MISSING = SynchedEntityData.defineId(MooseEntity.class, EntityDataSerializers.BOOLEAN);

	public static final EntityDataAccessor<Boolean> DASHING = SynchedEntityData.defineId(MooseEntity.class, EntityDataSerializers.BOOLEAN);

    public int dashCooldown;

    public final AnimationState attackAnimationState;
    private int attackAnimationEnd;

    public MooseEntity(EntityType<? extends AbstractHorse> entityType, Level world) {
        super(entityType, world);
        var mobNavigation = (GroundPathNavigation)this.getNavigation();
        mobNavigation.setCanFloat(true);
        mobNavigation.setCanWalkOverFences(true);
        attackAnimationState = new AnimationState();
    }

    private static final EntityDimensions BABY_BASE_DIMENSIONS = CanadaMod.MOOSE_ENTITY.getDimensions().scale(0.5F).withEyeHeight(0.665F);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
		builder.define(LEFT_ANTLER_MISSING, false);
		builder.define(RIGHT_ANTLER_MISSING, false);
		builder.define(DASHING, false);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return CanadaSounds.SOUND_MOOSE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CanadaSounds.SOUND_MOOSE_DEATH;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return CanadaSounds.SOUND_MOOSE_AMBIENT;
    }

    public boolean isLeftAntlerMissing() {
        return this.getEntityData().get(LEFT_ANTLER_MISSING);
    }

    public boolean isRightAntlerMissing() {
        return this.getEntityData().get(RIGHT_ANTLER_MISSING);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new OcelotAttackGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D, moose -> moose.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new RunAroundLikeCrazyGoal(this, 1.2));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, stack -> this.isFood(stack), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(this, Player.class, 10, true, false, this::isAngryAt));
    }

    public static AttributeSupplier.Builder createMooseAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 25.0f).add(Attributes.MOVEMENT_SPEED, 0.25f)
            .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 2f)
            .add(Attributes.ATTACK_DAMAGE, 3.0D)
            .add(Attributes.OXYGEN_BONUS)
            .add(Attributes.JUMP_STRENGTH, 0.6D)
            .add(Attributes.STEP_HEIGHT, 1.5D)
            .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    public void updateAnimations() {
        if (attackAnimationState.isStarted() && tickCount > attackAnimationEnd) {
            attackAnimationState.stop();
        }
    }

    @Override
    public void customServerAiStep(ServerLevel world) {
        super.customServerAiStep(world);
        updatePersistentAnger(world, true);
        tryRegenAntler(true, false);
        tryRegenAntler(false, false);
        tryShedAntler(true, false);
        tryShedAntler(false, false);
    }

    private boolean wasSprinting = false;

    @Override
    protected void tickRidden(Player controllingPlayer, Vec3 movementInput) {
        super.tickRidden(controllingPlayer, movementInput);

        if (controllingPlayer.isSprinting() && !wasSprinting && this.dashCooldown <= 0) {
            this.dashCooldown = 20;
            this.setDashing(true);
        }
        wasSprinting = controllingPlayer.isSprinting();
    }

    @Override
    public boolean canSprint() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        updateAnimations();

        if(this.isDashing()) {
            var dir = this.getControllingPassenger() == null ? this.getViewVector(1.0F) : this.getControllingPassenger().getViewVector(1.0F);
            var forward = dir.normalize();
            this.setDeltaMovement(forward.x * 1.5, this.getDeltaMovement().y, forward.z * 1.5);

            var list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2f).expandTowards(forward.scale(2f)),
                (entity) -> entity instanceof Enemy || entity instanceof Player);

            for (LivingEntity livingEntity : list) {
                knockBack(livingEntity);
            }

            this.makeMad();
        }

        if (this.isDashing() && this.dashCooldown < 15) {
			this.setDashing(false);
		}

        if (this.dashCooldown > 0) {
			this.dashCooldown--;
			if (this.dashCooldown == 0) {
				this.level().playSound(null, this.blockPosition(), SoundEvents.CAMEL_DASH_READY, SoundSource.NEUTRAL, 1.0F, 1.0F);
			}
		}
    }

    public boolean isDashing() {
		return this.entityData.get(DASHING);
	}

	public void setDashing(boolean dashing) {
		this.entityData.set(DASHING, dashing);
        this.playJumpSound();
	}

    @Override
    public boolean isStanding() {
        // This is horse anger not attacking anger which we override
        return false;
    }

    private void tryRegenAntler(boolean left, boolean force) {
        if (left ? this.isLeftAntlerMissing() : this.isRightAntlerMissing()) {
            // Average length 5 minutes to regen antlers
            if (force || this.random.nextFloat() < 1f / (float)(5 * 60 * 20)) {
                this.getEntityData().set(left ? LEFT_ANTLER_MISSING : RIGHT_ANTLER_MISSING, false);
                this.playSound(CanadaSounds.SOUND_MOOSE_GROW_ANTLER, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
            }
        }
    }

    private void tryShedAntler(boolean left, boolean force) {
        if (this.level().isClientSide()) {
            return;
        }

        // Average length 20 minutes to shed antlers
        if (force || this.random.nextFloat() < 1f / (float)(20 * 60 * 20)) {
            if (left ? !this.isLeftAntlerMissing() : !this.isRightAntlerMissing()) {
                var yaw = (float)(this.yBodyRot * (Math.PI / 180));
                var offset = new Vec3(left ? 0.4f : -0.4f, 0f, -1.5f);
                var antlerPos = new Vec3(this.getX(), this.getEyeY(), this.getZ()).add(offset.yRot(yaw));

                var item = this.spawnAtLocation((ServerLevel)this.level(), CanadaItems.ANTLERS);
                item.setPos(antlerPos);
                this.getEntityData().set(left ? LEFT_ANTLER_MISSING : RIGHT_ANTLER_MISSING, true);

                this.playSound(CanadaSounds.SOUND_MOOSE_STRIP_ANTLER, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
            }
        }
    }

    private void knockBack(Entity entity) {
        if (this.level() instanceof ServerLevel serverWorld) {
            double d = entity.getX() - getX();
            double e = entity.getZ() - getZ();
            double f = Math.max(d * d + e * e, 0.001D);
            entity.push(d / f * 5.0D, 0.2D, e / f * 5.0D);
            entity.hurtServer(serverWorld, damageSources().mobAttack(this.getControllingPassenger() == null ? this : this.getControllingPassenger()), 2);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.SEAGRASS) || stack.is(ItemTags.LEAVES);
    }

    @Override
    public boolean canMate(Animal other) {
        if (other != this && other instanceof MooseEntity otherMoose) {
            return canParent() && otherMoose.canParent();
        }
        return false;
    }

    @Override
    protected boolean canParent() {
        return !this.isVehicle() && !this.isPassenger() && !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return (MooseEntity)CanadaMod.MOOSE_ENTITY.create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return isBaby() ? BABY_BASE_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    @Override
    public void aiStep() {
        if (this.getControllingPassenger() != null) {
            if (this.isUnderWater()) {
                this.jumpInLiquid(FluidTags.WATER);
            }
            if ((this.isInWater() && this.allowStandSliding)) {
                this.setDeltaMovement(this.getDeltaMovement().x, 0.5, this.getDeltaMovement().z);
            }
        }
        super.aiStep();
    }

    @Override
    public int modifyTemper(int difference) {
        if (difference > 0) {
            this.playAttackAnimation();
        }
        return super.modifyTemper(difference);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // If the moose wants to kill something it should not let u do anything
        if (attacking != null || this.attackingPlayer != null || this.getTarget() != null) {
            return InteractionResult.FAIL;
        }

        var itemStack = player.getItemInHand(hand);

        // No idea why but its using the wrong hand here
        var mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (isFood(mainHandStack)) {
            hand = InteractionHand.MAIN_HAND;
            itemStack = mainHandStack;
        }
        var offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);
        if (isFood(offHandStack)) {
            hand = InteractionHand.OFF_HAND;
            itemStack = offHandStack;
        }

        var isBreedingItem = isFood(itemStack);

        var canBreed = this.getHealth() == this.getMaxHealth() && !this.isLeftAntlerMissing() && !this.isRightAntlerMissing() && !isBaby();

        // does healing interaction stuff
        if (isBreedingItem) {
            if (!canBreed) {
                return fedFood(player, itemStack);
            }
            // does breeding
            else if (this.getAge() == 0 && this.canFallInLove()) {
                this.setInLoveTime(600);

                if (!this.level().isClientSide() ) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.setInLove(player);
                    this.playEatingSound();
                    return InteractionResult.SUCCESS_SERVER;
                }

                if (this.level().isClientSide()) {
                    return InteractionResult.CONSUME;
                }
            }
        }

        // If not giving food, make untame guy angry and refuse ur item
        if (!isBreedingItem && !itemStack.isEmpty() && !this.isTamed()) {
            this.makeMad();
            return InteractionResult.SUCCESS;
        }

        // Not hungry
        if (isBreedingItem) {
            return InteractionResult.FAIL;
        }

        // Does saddling and stuff
        return super.mobInteract(player, hand);
    }

    protected void dropCustomDeathLoot(ServerLevel world, DamageSource source, boolean causedByPlayer) {
        super.dropCustomDeathLoot(world, source, causedByPlayer);
        Entity entity = source.getEntity();
        if (entity instanceof Creeper creeperEntity) {
            var skullAccessor =
                (xen42.canadamod.mixin.CreeperSkullAccessor) creeperEntity;

            if (creeperEntity.isPowered()
                    && !skullAccessor.canadamod$getDroppedSkulls()) {
                ItemStack itemStack = this.getSkull();

                if (!itemStack.isEmpty()) {
                    skullAccessor.canadamod$setDroppedSkulls(true);
                    this.spawnAtLocation(world, itemStack);
                }
            }
        }

    }

    protected ItemStack getSkull() {
        return new ItemStack(CanadaItems.MOOSE_HEAD);
    }

    @Override
    protected boolean handleEating(Player player, ItemStack item) {
        if (!isFood(item)) {
            return false;
        }

        boolean isHurt = (getHealth() < getMaxHealth());
        if (isHurt) {
            heal(2.0F);
        }

        if (isLeftAntlerMissing()) {
            tryRegenAntler(true, true);
        }
        else if (isRightAntlerMissing()) {
            tryRegenAntler(false, true);
        }

        boolean isBaby = isBaby();
        if (isBaby) {
            level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
            if (!(level()).isClientSide()) {
                ageUp(10);
            }
        }

        boolean canLove = (getAge() == 0 && canFallInLove());
        if (canLove) {
            setInLove(player);
        }

        if (isHurt || canLove || isBaby) {
            if (!isSilent()) {
                SoundEvent soundEvent = getEatingSound();
                if (soundEvent != null) {
                    level().playSound(null, getX(), getY(), getZ(), soundEvent, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                }
            }

            gameEvent(GameEvent.EAT);
            return true;
        }

        return false;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return CanadaSounds.SOUND_MOOSE_ANGRY;
    }

    @Override
    public void makeMad() {
        this.makeSound(this.getAngrySound());
    }

    @Override
    public boolean isTamed() {
        return super.isTamed() && attacking == null;
    }

    @Override
    public void setLastHurtMob(Entity target) {
        super.setLastHurtMob(target);
        playAttackAnimation();
    }

    @Override
    public boolean doHurtTarget(ServerLevel world, Entity target) {
        playAttackAnimation();
        return super.doHurtTarget(world, target);
    }

    private void playAttackAnimation() {
        attackAnimationState.start(this.tickCount);
        this.attackAnimationEnd = this.tickCount + 20;
    }

    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    private long angerEndTime = -1L;
    @Nullable
    private net.minecraft.world.entity.EntityReference<net.minecraft.world.entity.LivingEntity> angryAt;

    @Nullable
    private LivingEntity attacking;

    @Nullable
    private LivingEntity attacker;

    @Nullable
    private Player attackingPlayer;

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (this.isClientAuthoritative()) {
            return;
        }

        if (target == null && target != this.getTarget()) {
            this.makeMad();
            this.playAttackAnimation();
        }

        if (target instanceof Player player) {
            setPlayerTarget(player);
        }
        else if (target == null) {
            setPlayerTarget(null);
        }

        super.setTarget(target);
    }

    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide()) {
            player.openHorseInventory(this, this.inventory);
        }
    }

    @Override
    public float getAgeScale() {
        return isBaby() ? 0.5F : 1.0F;
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        addPersistentAngerSaveData(nbt);
        nbt.putBoolean("IsLeftAntlerMissing", this.getEntityData().get(LEFT_ANTLER_MISSING));
        nbt.putBoolean("IsRightAntlerMissing", this.getEntityData().get(RIGHT_ANTLER_MISSING));
        nbt.putBoolean("IsDashing", this.getEntityData().get(DASHING));
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        readPersistentAngerSaveData(level(), nbt);
        this.getEntityData().set(LEFT_ANTLER_MISSING, nbt.getBooleanOr("IsLeftAntlerMissing", false));
        this.getEntityData().set(RIGHT_ANTLER_MISSING, nbt.getBooleanOr("IsRightAntlerMissing", false));
        this.getEntityData().set(DASHING, nbt.getBooleanOr("IsDashing", false));
    }

    public void setPlayerTarget(@Nullable Player attacking) {
        this.attackingPlayer = attacking;
        this.lastHurtByPlayerMemoryTime = this.tickCount;
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity attacker) {
        if (this.isClientAuthoritative()) {
            return;
        }

        super.setLastHurtByMob(attacker);
        this.attacker = attacker;
        setTarget(attacker);
    }

    @Nullable
    public LivingEntity getLastHurtByMob() {
        return this.attacker;
    }

    @Override
    public void startPersistentAngerTimer() {
        setTimeToRemainAngry(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof LivingEntity && this.random.nextIntBetweenInclusive(0, 10) == 0) {
            this.tryShedAntler(this.random.nextBoolean(), true);
        }
        return super.hurtServer(world, source, amount);
    }

    @Override
    public void handleDamageEvent(DamageSource damageSource) {
        if (damageSource.getDirectEntity() instanceof LivingEntity entity) {
            this.setLastHurtByMob(entity);
        }

        super.handleDamageEvent(damageSource);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!this.level().isClientSide() && !this.isBaby()) {
            if (!this.isLeftAntlerMissing()) {
                var item = this.spawnAtLocation((ServerLevel)this.level(), CanadaItems.ANTLERS);
                item.setPos(this.getEyePosition());
            }
            if (!this.isRightAntlerMissing()) {
                var item = this.spawnAtLocation((ServerLevel)this.level(), CanadaItems.ANTLERS);
                item.setPos(this.getEyePosition());
            }
        }

        super.die(damageSource);
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.angerEndTime;
    }

    @Override
    public net.minecraft.world.entity.EntityReference<net.minecraft.world.entity.LivingEntity> getPersistentAngerTarget() {
        return this.angryAt;
    }

    @Override
    public void setPersistentAngerEndTime(long angerEndTime) {
        this.angerEndTime = angerEndTime;
    }

    @Override
    public void setPersistentAngerTarget(
            @Nullable net.minecraft.world.entity.EntityReference<net.minecraft.world.entity.LivingEntity> angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(CanadaItems.MOOSE_SPAWN_EGG);
    }
}
