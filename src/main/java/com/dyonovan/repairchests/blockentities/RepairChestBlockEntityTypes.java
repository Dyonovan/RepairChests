package com.dyonovan.repairchests.blockentities;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RepairChestBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RepairChests.MODID);

    public static final RegistryObject<BlockEntityType<BasicChestBlockEntity>> BASIC_CHEST = TILE_ENTITIES.register(
            "basic_chest", () -> typeOf(BasicChestBlockEntity::new, RepairChestBlocks.BASIC_CHEST.get()));

    public static final RegistryObject<BlockEntityType<AdvancedChestBlockEntity>> ADVANCED_CHEST = TILE_ENTITIES.register(
            "advanced_chest", () -> typeOf(AdvancedChestBlockEntity::new, RepairChestBlocks.ADVANCED_CHEST.get()));

    public static final RegistryObject<BlockEntityType<UltimateChestBlockEntity>> ULTIMATE_CHEST = TILE_ENTITIES.register(
            "ultimate_chest", () -> typeOf(UltimateChestBlockEntity::new, RepairChestBlocks.ULTIMATE_CHEST.get()));

    private static <T extends BlockEntity> BlockEntityType<T> typeOf(BlockEntityType.BlockEntitySupplier<T> entity, Block... blocks) {
    return BlockEntityType.Builder.of(entity, blocks).build(null);
  }
}
