package xen42.canadamod;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class CanadaModPOITagGenerator extends FabricTagProvider<PoiType> {
    public CanadaModPOITagGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, Registries.POINT_OF_INTEREST_TYPE, registriesFuture);
    }

    @Override
    public String getName() {
        return "CanadaModPOITagGenerator";
    }

    @Override
    protected void addTags(Provider registries) {
        tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
            .add(CanadaVillagers.LUMBERJACK_POI_KEY);
    }
}