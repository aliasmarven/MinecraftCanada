package xen42.canadamod;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.block.Block;

public class CanadaVillagers {
    public static final ResourceKey<PoiType> LUMBERJACK_POI_KEY =
            ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE,
                    Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "lumberjack_poi"));

    public static final PoiType LUMBERJACK_POI =
            registerNewPOI("lumberjack_poi", CanadaBlocks.TREE_TAP);

    public static final ResourceKey<VillagerProfession> SUGAR_SHACK_VILLAGER_KEY =
            ResourceKey.create(Registries.VILLAGER_PROFESSION,
                    Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "sugar_shack"));

    public static final VillagerProfession SUGAR_SHACK_VILLAGER =
            registerNewProfession(SUGAR_SHACK_VILLAGER_KEY,
                    LUMBERJACK_POI_KEY, SoundEvents.VILLAGER_WORK_BUTCHER);

    public static VillagerProfession registerNewProfession(
            ResourceKey<VillagerProfession> key,
            ResourceKey<PoiType> poi,
            SoundEvent sound) {

        var profession = new VillagerProfession(
                Component.translatable("entity." +
                        key.location().getNamespace() + ".villager." +
                        key.location().getPath()),
                entry -> entry.is(poi),
                entry -> entry.is(poi),
                ImmutableSet.of(),
                ImmutableSet.of(),
                sound,
                sugarShackTradeSets()
        );

        return Registry.register(
                BuiltInRegistries.VILLAGER_PROFESSION,
                key.location(),
                profession
        );
    }

    private static Int2ObjectMap<ResourceKey<TradeSet>> sugarShackTradeSets() {
        Int2ObjectMap<ResourceKey<TradeSet>> sets =
                new Int2ObjectOpenHashMap<>();

        for (int level = 1; level <= 5; level++) {
            sets.put(level, ResourceKey.create(
                    Registries.TRADE_SET,
                    Identifier.fromNamespaceAndPath(
                            CanadaMod.MOD_ID,
                            "sugar_shack/level_" + level
                    )
            ));
        }

        return sets;
    }

    public static PoiType registerNewPOI(String name, Block block) {
        return PoiHelper.register(
                Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, name),
                1, 1, block
        );
    }

    public static void initialize() {
        // Trade offers are defined in data/canadamod/villager_trade
        // and associated through data/canadamod/trade_set.
        // Vanilla trading pools are extended using trade tags.
    }
}
