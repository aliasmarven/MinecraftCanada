package xen42.canadamod;

import java.util.concurrent.atomic.AtomicInteger;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;

public final class CanadaCampsiteLoot {
    private static final ResourceKey<LootTable> COMMON_CHEST =
        ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(
                "minecraft", "chests/abandoned_camp_common_chest"
            )
        );

    private static final ResourceKey<LootTable> SECRET_CHEST =
        ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(
                "minecraft", "chests/abandoned_camp_secret_chest"
            )
        );

    private CanadaCampsiteLoot() {
    }

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, table, source, registries) -> {
            if (source != LootTableSource.VANILLA) {
                return;
            }

            final int targetPool;
            final int entriesToAdd;

            if (COMMON_CHEST.equals(key)) {
                targetPool = 2;
                entriesToAdd = 1;
            } else if (SECRET_CHEST.equals(key)) {
                targetPool = 3;
                entriesToAdd = 2;
            } else {
                return;
            }

            var structures = registries.lookupOrThrow(Registries.STRUCTURE)
                .getOrThrow(CanadaTags.StructureTags.MAPLE_CABIN);

            var map = LootItem.lootTableItem(Items.FILLED_MAP)
                .apply(Holder.direct(
                    SetNameFunction.setName(
                        Component.translatable(
                            "filled_map.canadamod.maple_cabin"
                        ),
                        SetNameFunction.Target.ITEM_NAME
                    ).build()
                ))
                .apply(Holder.direct(
                    ExplorationMapFunction.makeExplorationMap(structures)
                        .setMapDecoration(MapDecorationTypes.TARGET_X)
                        .build()
                ));

            // Minecraft 26.3's fourth secret-chest pool selects a map.
            AtomicInteger poolIndex = new AtomicInteger();

            ((net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder) table)
                .modifyPools(pool -> {
                    if (poolIndex.getAndIncrement() == targetPool) {
                        for (int i = 0; i < entriesToAdd; i++) {
                            pool.add(map);
                        }
                    }
                });
        });
    }
}
