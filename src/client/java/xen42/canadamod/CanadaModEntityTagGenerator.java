package xen42.canadamod;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.EntityTypeTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;


public class CanadaModEntityTagGenerator extends EntityTypeTagProvider {

    private static ResourceKey<EntityType<?>> key(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).orElseThrow();
    }


    public CanadaModEntityTagGenerator(FabricDataOutput output, CompletableFuture<Provider> completableFuture) {
        super(output, completableFuture);

    }

    @Override
	public String getName() {
		return "CanadaModEntityTagGenerator";
	}

    @Override
    protected void addTags(Provider wrapperLookup) {
        this.tag(EntityTypeTags.CAN_EQUIP_SADDLE).add(key(CanadaMod.MOOSE_ENTITY));
        this.tag(EntityTypeTags.AQUATIC).add(key(CanadaMod.MOOSE_ENTITY)).add(key(CanadaMod.BEAVER_ENTITY));
    }

}
