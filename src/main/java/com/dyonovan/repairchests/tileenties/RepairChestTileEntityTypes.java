package com.dyonovan.repairchests.tileenties;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RepairChestTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RepairChests.MODID);

    public static final RegistryObject<TileEntityType<BasicChestTileEntity>> BASIC_CHEST = TILE_ENTITIES.register(
            "basic_chest", () -> new TileEntityType<>(BasicChestTileEntity::new, Sets.newHashSet(RepairChestBlocks.BASIC_CHEST.get()), null));

    public static final RegistryObject<TileEntityType<AdvancedChestTileEntity>> ADVANCED_CHEST = TILE_ENTITIES.register(
            "advanced_chest", () -> new TileEntityType<>(AdvancedChestTileEntity::new, Sets.newHashSet(RepairChestBlocks.ADVANCED_CHEST.get()), null));

    public static final RegistryObject<TileEntityType<UltimateChestTileEntity>> ULTIMATE_CHEST = TILE_ENTITIES.register(
            "ultimate_chest", () -> new TileEntityType<>(UltimateChestTileEntity::new, Sets.newHashSet(RepairChestBlocks.ULTIMATE_CHEST.get()), null));
}
