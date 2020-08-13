package com.dyonovan.repairchests.tileenties;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.containers.RepairChestContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class UltimateChestTileEntity extends GenericRepairChestTileEntity {
    public UltimateChestTileEntity() {
        super(RepairChestTileEntityTypes.ULTIMATE_CHEST.get(), RepairChestTypes.ULTIMATE, RepairChestBlocks.ULTIMATE_CHEST::get);
    }

    @Override
    public int getTickTime() {
        return 40;
    }

    @Override
    protected Container createMenu(int windowId, PlayerInventory playerInventory) {
        return RepairChestContainer.createUltimateContainer(windowId, playerInventory, this);
    }
}
