package com.dyonovan.repairchests.blocks.entity;

import com.dyonovan.repairchests.Config;
import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.inventory.RepairChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UltimateChestBlockEntity extends AbstractRepairChestBlockEntity {

    public UltimateChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RepairChestsBlockEntityTypes.ULTIMATE_CHEST.get(), blockPos, blockState, RepairChestTypes.ULTIMATE, RepairChestBlocks.ULTIMATE_CHEST::get);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return RepairChestMenu.createUltimateContainer(containerId, playerInventory, this);
    }

    @Override
    public int getTickTime() {
        return Config.GENERAL.ultimateRepairTime.get() * 20;
    }
}
