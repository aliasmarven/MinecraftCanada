package xen42.canadamod;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class CanadaModEntityLootTableGenerator extends FabricEntityLootTableProvider {
    protected CanadaModEntityLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public String getName() {
        return "CanadaModEntityLootTableGenerator";
    }

    @Override
    public void generate() {
        this.add(
            CanadaMod.BEAVER_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                            LootItem.lootTableItem(CanadaItems.PELT)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 1.0F)))
                        )
                )
            );
        this.add(
            CanadaMod.MOOSE_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(0.5F))
                        .add(
                            LootItem.lootTableItem(CanadaItems.VENISON)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1.0F, 2.0F)))
                                .apply((LootItemFunction.Builder)SmeltItemFunction.smelted()
                                    .when((LootItemCondition.Builder)shouldSmeltLoot()))
                        )
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(0.5F))
                        .add(
                            LootItem.lootTableItem(Items.LEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1.0F, 2.0F)))
                        )
                )
            );

        this.add(
            CanadaMod.DUCK_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(
                            LootItem.lootTableItem(CanadaItems.WATERFOWL)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply((LootItemFunction.Builder)SmeltItemFunction.smelted()
                                    .when((LootItemCondition.Builder)shouldSmeltLoot()))
                        )
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(
                            LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 3.0F)))
                        )
                )
            );
        this.add(
            CanadaMod.GOOSE_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(
                            LootItem.lootTableItem(CanadaItems.WATERFOWL)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply((LootItemFunction.Builder)SmeltItemFunction.smelted()
                                    .when((LootItemCondition.Builder)shouldSmeltLoot()))
                        )
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(
                            LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 3.0F)))
                        )
                )
            );
        this.add(
            CanadaMod.GRIZZLY_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(
                            LootItem.lootTableItem(Items.SALMON)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1.0F, 2.0F)))
                                .apply((LootItemFunction.Builder)SmeltItemFunction.smelted()
                                    .when((LootItemCondition.Builder)shouldSmeltLoot()))
                        )
                )
            );
    }
}
