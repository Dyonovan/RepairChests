package com.dyonovan.repairchests.tileenties;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.RepairChestTypes;

public class BasicChestTileEntity extends GenericRepairChestTileEntity {

    public BasicChestTileEntity() {
        super(RepairChestTileEntityTypes.BASIC_CHEST.get(), RepairChestTypes.BASIC, RepairChestBlocks.BASIC_CHEST::get);
    }

    @Override
    public int getTickTime() {
        return 200;
    }
}
