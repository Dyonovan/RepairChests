package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.tileenties.AdvancedChestTileEntity;
import com.dyonovan.repairchests.tileenties.RepairChestTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class AdvancedChestBlock extends GenericRepairChest {

    public AdvancedChestBlock(Properties properties) {
        super(RepairChestTypes.ADVANCED, RepairChestTileEntityTypes.ADVANCED_CHEST::get, properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AdvancedChestTileEntity();
    }
}
