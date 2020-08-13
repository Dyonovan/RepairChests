package com.dyonovan.repairchests.tileenties;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.containers.RepairChestContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class AdvancedChestTileEntity extends GenericRepairChestTileEntity {

    public AdvancedChestTileEntity() {
        super(RepairChestTileEntityTypes.ADVANCED_CHEST.get(), RepairChestTypes.ADVANCED, RepairChestBlocks.ADVANCED_CHEST::get);
    }

    @Override
    public int getTickTime() {
        return 100;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return RepairChestContainer.createAdvancedContainer(id, playerInventory, this);
    }
}
