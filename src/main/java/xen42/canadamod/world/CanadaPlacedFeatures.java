package xen42.canadamod.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import xen42.canadamod.CanadaMod;

public class CanadaPlacedFeatures {

    public static final ResourceKey<PlacedFeature> MAPLE_CHECKED = registerKey("maple_checked");
    public static final ResourceKey<PlacedFeature> MAPLE_FOREST_VEGETATION = registerKey("maple_forest_vegetation");
    public static final ResourceKey<PlacedFeature> MAPLE_LEAF_LITTER = registerKey("maple_leaf_litter");
    public static final ResourceKey<PlacedFeature> FALLEN_MAPLE_TREE = registerKey("fallen_maple_tree");

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, name));
    }

    public static void onInitialize() {

    }

}
