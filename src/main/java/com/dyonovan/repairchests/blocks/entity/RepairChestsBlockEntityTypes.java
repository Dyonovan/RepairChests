package com.dyonovan.repairchests.blocks.entity;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RepairChestsBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, RepairChests.MODID);

    public static final RegistryObject<BlockEntityType<BasicChestBlockEntity>> BASIC_CHEST = BLOCK_ENTITIES.register(
            "basic_chest", () -> BlockEntityType.Builder.of(BasicChestBlockEntity::new, RepairChestBlocks.BASIC_CHEST.get()).build(null));

    public static final RegistryObject<BlockEntityType<AdvancedChestBlockEntity>> ADVANCED_CHEST = BLOCK_ENTITIES.register(
            "advanced_chest", () -> BlockEntityType.Builder.of(AdvancedChestBlockEntity::new, RepairChestBlocks.ADVANCED_CHEST.get()).build(null));

    public static final RegistryObject<BlockEntityType<UltimateChestBlockEntity>> ULTIMATE_CHEST = BLOCK_ENTITIES.register(
            "ultimate_chest", () -> BlockEntityType.Builder.of(UltimateChestBlockEntity::new, RepairChestBlocks.ULTIMATE_CHEST.get()).build(null));
}
