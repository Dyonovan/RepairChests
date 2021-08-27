package com.dyonovan.repairchests.blocks.entity;

import com.dyonovan.repairchests.Config;
import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.inventory.RepairChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class BasicChestBlockEntity extends AbstractRepairChestBlockEntity {

    public BasicChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RepairChestsBlockEntityTypes.BASIC_CHEST.get(), blockPos, blockState, RepairChestTypes.BASIC, RepairChestBlocks.BASIC_CHEST::get);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return RepairChestMenu.createBasicContainer(containerId, playerInventory, this);
    }

    @Override
    public int getTickTime() {
        return Config.GENERAL.basicRepairTime.get() * 20;
    }


}
