package xen42.canadamod.entities.eggs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xen42.canadamod.CanadaItems;
import xen42.canadamod.CanadaMod;

public class DuckEggEntity extends CustomEggEntity {

    public DuckEggEntity(EntityType<? extends ThrowableItemProjectile> entity, Level world) {
        super(net.minecraft.world.entity.EntityTypes.EGG, world);
    }

	public DuckEggEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
    }

    public DuckEggEntity(Level world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
    }

    @Override
	protected Item getDefaultItem() {
		return CanadaItems.DUCK_EGG;
	}

    @Override
	public EntityType<? extends Animal> getHatchedEntity() {
		return CanadaMod.DUCK_ENTITY;
	}

}
