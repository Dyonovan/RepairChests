package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.tileenties.BasicChestTileEntity;
import com.dyonovan.repairchests.tileenties.RepairChestTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BasicChestBlock extends GenericRepairChest {

    public BasicChestBlock(Properties properties) {
        super(RepairChestTypes.BASIC, RepairChestTileEntityTypes.BASIC_CHEST::get, properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BasicChestTileEntity();
    }
}
