package com.dyonovan.repairchests.tileenties;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.containers.RepairChestContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedChestTileEntity extends GenericRepairChestTileEntity {

    public AdvancedChestTileEntity(BlockPos blockPos, BlockState blockState) {
        super(RepairChestTileEntityTypes.ADVANCED_CHEST.get(), blockPos, blockState, RepairChestTypes.ADVANCED, RepairChestBlocks.ADVANCED_CHEST::get);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerID, Inventory playerInventory) {
        return RepairChestContainer.createAdvancedContainer(containerID, playerInventory, this);
    }
}
