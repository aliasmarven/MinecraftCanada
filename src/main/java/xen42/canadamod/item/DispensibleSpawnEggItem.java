package xen42.canadamod.item;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class DispensibleSpawnEggItem extends SpawnEggItem {
    protected static DispenseItemBehavior createDispenseItemBehavior() {
        return new DefaultDispenseItemBehavior() {
            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = source.state().getValue(DispenserBlock.FACING);
                EntityType<?> entityType = SpawnEggItem.getType(stack);

                try {
                    entityType.spawn(source.level(), stack, null, source.pos().relative(direction), EntitySpawnReason.DISPENSER, direction != Direction.UP, false);
                } catch (Exception ex) {
                    LOGGER.error("Error while dispensing spawn egg from dispenser at {}", source.pos(), ex);
                    return ItemStack.EMPTY;
                }

                stack.shrink(1);
                source.level().gameEvent(null, GameEvent.ENTITY_PLACE, source.pos());
                return stack;
            }
        };
    }

    public DispensibleSpawnEggItem(EntityType<? extends Mob> entityType, Properties properties) {
        this(entityType, properties, createDispenseItemBehavior());
    }

    public DispensibleSpawnEggItem(EntityType<? extends Mob> entityType, Properties properties,
                                    @Nullable DispenseItemBehavior dispenseItemBehavior) {
        super(properties.spawnEgg(entityType));
        if (dispenseItemBehavior != null) {
            DispenserBlock.registerBehavior(this, dispenseItemBehavior);
        }
    }
}
