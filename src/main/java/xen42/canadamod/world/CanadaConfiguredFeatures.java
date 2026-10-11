package xen42.canadamod.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import xen42.canadamod.CanadaMod;

public class CanadaConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAPLE_CONFIGURED_FEATURE = registerKey("maple");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAPLE_FOREST_VEGETATION = registerKey("maple_forest_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_MAPLE_TREE = registerKey("fallen_maple_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAPLE_LEAF_LITTER = registerKey("maple_leaf_litter");

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, name));
    }

    public static void onInitialize() {

    }
}
