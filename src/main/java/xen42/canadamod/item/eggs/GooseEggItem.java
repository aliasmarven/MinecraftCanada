package xen42.canadamod.item.eggs;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xen42.canadamod.entities.eggs.GooseEggEntity;

public class GooseEggItem extends CustomEggItem {
    public GooseEggItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public Projectile createEntity(Level world, LivingEntity owner, ItemStack stack) {
		return new GooseEggEntity(world, owner, stack);
	}

	@Override
	public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
		return new GooseEggEntity(world, pos.x(), pos.y(), pos.z(), stack);
	}
}
