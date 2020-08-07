package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.tileenties.RepairChestTileEntityTypes;
import com.dyonovan.repairchests.tileenties.UltimateChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class UltimateChestBlock extends GenericRepairChest {
    public UltimateChestBlock(Properties properties) {
        super(RepairChestTypes.ULTIMATE, RepairChestTileEntityTypes.ULTIMATE_CHEST::get, properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new UltimateChestTileEntity();
    }
}
