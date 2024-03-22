package com.dyonovan.repairchests.tileenties;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RepairChestTileEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RepairChests.MODID);

    public static final RegistryObject<BlockEntityType<BasicChestTileEntity>> BASIC_CHEST = TILE_ENTITIES.register(
            "basic_chest", () -> typeOf(BasicChestTileEntity::new, RepairChestBlocks.BASIC_CHEST.get()));

    public static final RegistryObject<BlockEntityType<AdvancedChestTileEntity>> ADVANCED_CHEST = TILE_ENTITIES.register(
            "advanced_chest", () -> typeOf(AdvancedChestTileEntity::new, RepairChestBlocks.ADVANCED_CHEST.get()));

    public static final RegistryObject<BlockEntityType<UltimateChestTileEntity>> ULTIMATE_CHEST = TILE_ENTITIES.register(
            "ultimate_chest", () -> typeOf(UltimateChestTileEntity::new, RepairChestBlocks.ULTIMATE_CHEST.get()));

    private static <T extends BlockEntity> BlockEntityType<T> typeOf(BlockEntityType.BlockEntitySupplier<T> entity, Block... blocks) {
    return BlockEntityType.Builder.of(entity, blocks).build(null);
  }
}
