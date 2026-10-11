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
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE, net.minecraft.resources.Identifier.fromNamespaceAndPath("canadamod", "maple_boat"), EntityType.Builder.<Boat>of((type, world) -> new Boat(type, world, () -> CanadaItems.MAPLE_BOAT), MobCategory.MISC)
            .noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10).build(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, net.minecraft.resources.Identifier.fromNamespaceAndPath("canadamod", "maple_boat"))));

    public static final EntityType<ChestBoat> MAPLE_CHEST_BOAT =
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE, net.minecraft.resources.Identifier.fromNamespaceAndPath("canadamod", "maple_chest_boat"), EntityType.Builder.<ChestBoat>of((type, world) -> new ChestBoat(type, world, () -> CanadaItems.MAPLE_CHEST_BOAT), MobCategory.MISC)
            .noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10).build(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, net.minecraft.resources.Identifier.fromNamespaceAndPath("canadamod", "maple_chest_boat"))));

    public static void initialize() {
        DispenserBlock.registerBehavior(CanadaItems.MAPLE_CHEST_BOAT, new BoatDispenseItemBehavior(MAPLE_CHEST_BOAT));
        DispenserBlock.registerBehavior(CanadaItems.MAPLE_BOAT, new BoatDispenseItemBehavior(MAPLE_BOAT));
    }
}
