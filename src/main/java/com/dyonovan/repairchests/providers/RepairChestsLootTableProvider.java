package com.dyonovan.repairchests.providers;

import com.dyonovan.repairchests.providers.RepairChestsBlockLoot;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class RepairChestsLootTableProvider extends LootTableProvider {
    public RepairChestsLootTableProvider(PackOutput packOutput) {
        super(packOutput, Set.of(), List.of(new SubProviderEntry(RepairChestsBlockLoot::new, LootContextParamSets.BLOCK)));
    }
}
