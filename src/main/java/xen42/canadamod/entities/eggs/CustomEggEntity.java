package xen42.canadamod.entities.eggs;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;

public abstract class CustomEggEntity extends ThrowableItemProjectile {
	private static final EntityDimensions EMPTY_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

    public CustomEggEntity(EntityType<? extends ThrowableItemProjectile> entity, Level world) {
        super(EntityType.EGG, world);
    }

	public CustomEggEntity(Level world, LivingEntity owner, ItemStack stack) {
		super(EntityType.EGG, owner, world, stack);
	}

	public CustomEggEntity(Level world, double x, double y, double z, ItemStack stack) {
		super(EntityType.EGG, x, y, z, world, stack);
	}

	@Override
	public void handleEntityEvent(byte status) {
		if (status == EntityEvent.DEATH) {
			double d = 0.08;

			for (int i = 0; i < 8; i++) {
				this.level()
					.addParticle(
						new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
						this.getX(),
						this.getY(),
						this.getZ(),
						(this.random.nextFloat() - 0.5) * 0.08,
						(this.random.nextFloat() - 0.5) * 0.08,
						(this.random.nextFloat() - 0.5) * 0.08
					);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
	}

	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		if (!this.level().isClientSide) {
			if (this.random.nextInt(8) == 0) {
				int i = 1;
				if (this.random.nextInt(32) == 0) {
					i = 4;
				}

				for (int j = 0; j < i; j++) {
					Animal entity = getHatchedEntity().create(this.level(), EntitySpawnReason.TRIGGERED);
					if (entity != null) {
						entity.setAge(-24000);
						entity.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
						if (!entity.fudgePositionAfterSizeChange(EMPTY_DIMENSIONS)) {
							break;
						}

						this.level().addFreshEntity(entity);
					}
				}
			}

			this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
			this.discard();
		}
	}

	@Override
	protected abstract Item getDefaultItem();

	public abstract EntityType<? extends Animal> getHatchedEntity();
}
