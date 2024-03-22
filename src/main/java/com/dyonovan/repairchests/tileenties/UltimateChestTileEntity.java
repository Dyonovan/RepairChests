package com.dyonovan.repairchests.tileenties;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.containers.RepairChestContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class UltimateChestTileEntity extends GenericRepairChestTileEntity {
    public UltimateChestTileEntity(BlockPos blockPos, BlockState blockState) {
        super(RepairChestTileEntityTypes.ULTIMATE_CHEST.get(), blockPos, blockState, RepairChestTypes.ULTIMATE, RepairChestBlocks.ULTIMATE_CHEST::get);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerID, Inventory playerInventory) {
        return RepairChestContainer.createUltimateContainer(containerID, playerInventory, this);
    }
}
