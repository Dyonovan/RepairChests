package com.dyonovan.repairchests.client;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class RepairChestsBlockLoot extends BlockLootSubProvider {

    private static final Set<Item> EXPLOSION_RESISTANT = Set.of();

    public RepairChestsBlockLoot() {
        super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(RepairChestBlocks.BASIC_CHEST.get(), this::createNameableBlockEntityTable);
        this.add(RepairChestBlocks.ADVANCED_CHEST.get(), this::createNameableBlockEntityTable);
        this.add(RepairChestBlocks.ULTIMATE_CHEST.get(), this::createNameableBlockEntityTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return RepairChestBlocks.BLOCKS.getEntries().stream().flatMap(RegistryObject::stream)::iterator;
    }
}
