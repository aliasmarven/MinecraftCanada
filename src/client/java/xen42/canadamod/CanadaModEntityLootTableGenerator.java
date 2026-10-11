package xen42.canadamod;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
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

public class CanadaModEntityLootTableGenerator extends FabricEntityLootSubProvider {
    protected CanadaModEntityLootTableGenerator(FabricPackOutput dataOutput, CompletableFuture<Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public String getName() {
        return "CanadaModEntityLootTableGenerator";
    }

    private static net.minecraft.core.Holder<
            net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider>
            exactInt(int value) {
        return net.minecraft.core.Holder.direct(
            new net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue(value)
        );
    }

    private static net.minecraft.core.Holder<
            net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider>
            uniformInt(int min, int max) {
        return net.minecraft.core.Holder.direct(
            new net.minecraft.world.level.storage.loot.providers.number.ints.UniformGenerator(
                exactInt(min), exactInt(max)
            )
        );
    }

    private static net.minecraft.core.Holder<
            net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider>
            uniformFloat(float min, float max) {
        return net.minecraft.core.Holder.direct(
            new net.minecraft.world.level.storage.loot.providers.number.floats.UniformGenerator(
                net.minecraft.core.Holder.direct(
                    new net.minecraft.world.level.storage.loot.providers.number.floats.ConstantValue(min)
                ),
                net.minecraft.core.Holder.direct(
                    new net.minecraft.world.level.storage.loot.providers.number.floats.ConstantValue(max)
                )
            )
        );
    }

    @Override
    public void generate() {
        this.add(
            CanadaMod.BEAVER_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(exactInt(1))
                        .add(
                            LootItem.lootTableItem(CanadaItems.PELT)
                                .apply(SetItemCountFunction.setCount(uniformInt(0, 1)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, uniformFloat(0f, 1f)))
                        )
                )
            );
        this.add(
            CanadaMod.MOOSE_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(exactInt(1))
                        .when(net.minecraft.core.Holder.direct(
                            net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
                                .randomChance(0.5F).build()
                        ))
                        .add(
                            LootItem.lootTableItem(CanadaItems.VENISON)
                                .apply(SetItemCountFunction.setCount(uniformInt(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, uniformFloat(1f, 2f)))
                                .apply((LootItemFunction.Builder)SmeltItemFunction.smelted()
                                    .when((LootItemCondition.Builder)shouldSmeltLoot()))
                        )
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(exactInt(1))
                        .when(net.minecraft.core.Holder.direct(
                            net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
                                .randomChance(0.5F).build()
                        ))
                        .add(
                            LootItem.lootTableItem(Items.LEATHER)
                                .apply(SetItemCountFunction.setCount(uniformInt(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, uniformFloat(1f, 2f)))
                        )
                )
            );

        this.add(
            CanadaMod.DUCK_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(exactInt(1))
                        .add(
                            LootItem.lootTableItem(CanadaItems.WATERFOWL)
                                .apply(SetItemCountFunction.setCount(exactInt(1)))
                                .apply((LootItemFunction.Builder)SmeltItemFunction.smelted()
                                    .when((LootItemCondition.Builder)shouldSmeltLoot()))
                        )
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(exactInt(1))
                        .add(
                            LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(uniformInt(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, uniformFloat(0f, 3f)))
                        )
                )
            );
        this.add(
            CanadaMod.GOOSE_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(exactInt(1))
                        .add(
                            LootItem.lootTableItem(CanadaItems.WATERFOWL)
                                .apply(SetItemCountFunction.setCount(exactInt(1)))
                                .apply((LootItemFunction.Builder)SmeltItemFunction.smelted()
                                    .when((LootItemCondition.Builder)shouldSmeltLoot()))
                        )
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(exactInt(1))
                        .add(
                            LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(uniformInt(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, uniformFloat(0f, 3f)))
                        )
                )
            );
        this.add(
            CanadaMod.GRIZZLY_ENTITY,
                LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(exactInt(1))
                        .add(
                            LootItem.lootTableItem(Items.SALMON)
                                .apply(SetItemCountFunction.setCount(uniformInt(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, uniformFloat(1f, 2f)))
                                .apply((LootItemFunction.Builder)SmeltItemFunction.smelted()
                                    .when((LootItemCondition.Builder)shouldSmeltLoot()))
                        )
                )
            );
    }
}
