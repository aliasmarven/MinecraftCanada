package xen42.canadamod.entities;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;
import xen42.canadamod.CanadaSounds;

public class BeaverEntity extends Animal {
    private static final EntityDataAccessor<Integer> CHOP_FATIGUE = SynchedEntityData.defineId(BeaverEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CHOP_FRENZY = SynchedEntityData.defineId(BeaverEntity.class, EntityDataSerializers.INT);

    @Nullable
    private BlockPos choppingTree;

    public final AnimationState chopAnimationState;

    public boolean isChopping;

    public BeaverEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
        //this.moveControl = new BeaverMoveControl(this);
        var mobNavigation = (GroundPathNavigation)this.getNavigation();
        mobNavigation.setCanFloat(true);
        chopAnimationState = new AnimationState();
    }

    private static final EntityDimensions BABY_BASE_DIMENSIONS = EntityType.COW.getDimensions().scale(0.5F).withEyeHeight(0.665F);

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, stack -> this.isTemptItem(stack), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new BeaverChopTreeGoal(this));
    }

    public static AttributeSupplier.Builder createBeaverAttributes() {
        return Animal.createAnimalAttributes()
            .add(Attributes.MAX_HEALTH, 5.0f)
            .add(Attributes.MOVEMENT_SPEED, 0.25f)
            .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 2f)
            .add(Attributes.OXYGEN_BONUS);
    }

    @Override
    public void tick() {
        super.tick();
        updateAnimations();

        if (this.getEntityData().get(CHOP_FATIGUE) > 0) {
            this.getEntityData().set(CHOP_FATIGUE, this.getEntityData().get(CHOP_FATIGUE) - 1);
        }
        if (this.getEntityData().get(CHOP_FRENZY) > 0) {
            this.getEntityData().set(CHOP_FRENZY, this.getEntityData().get(CHOP_FRENZY) - 1);
        }
    }

    public void updateAnimations() {
        if (isChopping && !chopAnimationState.isStarted()) {
            chopAnimationState.start(this.tickCount);
        }
        if (!isChopping && chopAnimationState.isStarted()) {
            chopAnimationState.stop();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(CHOP_FATIGUE, this.random.nextIntBetweenInclusive(20 * 60 * 3, 20 * 60 * 10));
        builder.define(CHOP_FRENZY, 0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.STICK);
    }

    public boolean isFrenzyItem(ItemStack stack) {
        return stack.is(CanadaItems.DONAIR) || stack.is(CanadaItems.POUTINE) || stack.is(CanadaItems.PIEROGI);
    }

    public boolean isFatigueRefreshItem(ItemStack stack) {
        return stack.is(CanadaItems.MAPLE_SYRUP_BOTTLE);
    }

    public boolean isTemptItem(ItemStack stack) {
        return (this.getEntityData().get(CHOP_FATIGUE) > 0 && isFatigueRefreshItem(stack))
            || (this.getEntityData().get(CHOP_FRENZY) <= 0 && isFrenzyItem(stack))
            || isFood(stack);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return (BeaverEntity)CanadaMod.BEAVER_ENTITY.create(world, EntitySpawnReason.BREEDING);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return CanadaSounds.SOUND_BEAVER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CanadaSounds.SOUND_BEAVER_DEATH;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return CanadaSounds.SOUND_BEAVER_AMBIENT;
    }

    @Override
    public float getAgeScale() {
        return isBaby() ? 0.5F : 1.0F;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return isBaby() ? BABY_BASE_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    private static class BeaverMoveControl extends SmoothSwimmingMoveControl {
        public BeaverMoveControl(Mob entity) {
            super(entity, 45, 10, 0.1F, 0.5F, false);
        }
    }

    private static class SwimToRandomPlaceGoal extends RandomSwimmingGoal {
        public SwimToRandomPlaceGoal(BeaverEntity beaver) {
            super(beaver, 1.0D, 40);
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(CanadaItems.BEAVER_SPAWN_EGG);
    }

    @Override
    public boolean isInWall() {
        // Same as super but ignoring logs
        // They die too often when bonemealing trees
        if (this.noPhysics) {
            return false;
        } else {
            var f = this.getDimensions(this.getPose()).width() * 0.8F;
            var box = AABB.ofSize(this.getEyePosition(), (double)f, 1.0E-6, (double)f);
            return BlockPos.betweenClosedStream(box).anyMatch((pos) -> {
                BlockState blockState = this.level().getBlockState(pos);
                return !blockState.isAir() && !blockState.is(BlockTags.LOGS) && blockState.isSuffocating(this.level(), pos) && Shapes.joinIsNotEmpty(blockState.getCollisionShape(this.level(), pos).move(pos), Shapes.create(box), BooleanOp.AND);
            });
        }
    }

    public boolean isFrenzied() {
        return this.getEntityData().get(CHOP_FRENZY) > 0;
    }

    public boolean isFatigued() {
        return this.getEntityData().get(CHOP_FATIGUE) > 0;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var itemStack = player.getItemInHand(hand);

        var didEat = false;
        if (this.isFatigueRefreshItem(itemStack) && this.isFatigued()) {
            didEat = true;
            if (!this.level().isClientSide) {
                this.getEntityData().set(CHOP_FATIGUE, 0);
            }
        }
        if (this.isFrenzyItem(itemStack) && !this.isFrenzied()) {
            didEat = true;
            if (!this.level().isClientSide) {
                this.getEntityData().set(CHOP_FRENZY, 20 * 30);
            }
        }

        if (didEat) {
            if (!this.level().isClientSide) {
                this.usePlayerItem(player, hand, itemStack);
                this.playEatingSound();
                return InteractionResult.SUCCESS_SERVER;
            }
            else {
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                if (itemStack.is(CanadaItems.MAPLE_SYRUP_BOTTLE)) {
                    this.level().playLocalSound(this, SoundEvents.HONEY_DRINK.value(), SoundSource.NEUTRAL, 1f, 1f);
                }
                else {
                    this.level().playLocalSound(this, CanadaSounds.SOUND_BEAVER_EAT, SoundSource.NEUTRAL, 1f, 1f);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void die(DamageSource source) {
        setChoppingProgress(-1);
        super.die(source);
    }

    public void setChoppingProgress(int progress) {
        if (choppingTree != null) {
            PlayerLookup.tracking(this).forEach(player -> {
                ServerPlayNetworking.send(player, new BeaverChopTreeEffectPayload(this.getId(), progress, choppingTree));
            });
        }
    }

    public void setChoppingTree(BlockPos pos) {
        this.choppingTree = pos;
    }

    public BlockPos getChoppingTreePos() {
        return choppingTree;
    }

    public void stopChopping() {
        setChoppingProgress(-1);
        setChoppingTree(null);
    }

    public void onChopTree() {
        // Pause for 5 to 10 minutes
        this.getEntityData().set(CHOP_FATIGUE, this.random.nextIntBetweenInclusive(5 * 60 * 20, 10 * 60 * 20));
    }

    public boolean canChopTree() {
        return !this.isBaby() && (this.getEntityData().get(CHOP_FATIGUE) <= 0 || isFrenzied());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putInt("chopFatigue", this.getEntityData().get(CHOP_FATIGUE));
        nbt.putInt("chopFrenzy", this.getEntityData().get(CHOP_FRENZY));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.getEntityData().set(CHOP_FATIGUE, nbt.getInt("chopFatigue").orElse(0));
        this.getEntityData().set(CHOP_FRENZY, nbt.getInt("chopFrenzy").orElse(0));
    }
}
