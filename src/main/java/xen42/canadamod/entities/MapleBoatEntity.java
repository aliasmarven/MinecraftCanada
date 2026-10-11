package xen42.canadamod.entities;

import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.level.block.DispenserBlock;
import xen42.canadamod.CanadaItems;

public class MapleBoatEntity {
    public static final EntityType<Boat> MAPLE_BOAT =
        EntityType.register("maple_boat", EntityType.Builder.<Boat>of(EntityType.boatFactory(() -> CanadaItems.MAPLE_BOAT), MobCategory.MISC)
            .noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));

    public static final EntityType<ChestBoat> MAPLE_CHEST_BOAT =
        EntityType.register("maple_chest_boat", EntityType.Builder.<ChestBoat>of(EntityType.chestBoatFactory(() -> CanadaItems.MAPLE_CHEST_BOAT), MobCategory.MISC)
            .noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));

    public static void initialize() {
        DispenserBlock.registerBehavior(CanadaItems.MAPLE_CHEST_BOAT, new BoatDispenseItemBehavior(MAPLE_CHEST_BOAT));
        DispenserBlock.registerBehavior(CanadaItems.MAPLE_BOAT, new BoatDispenseItemBehavior(MAPLE_BOAT));
    }
}
