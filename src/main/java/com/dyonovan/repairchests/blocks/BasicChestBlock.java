package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.blocks.entity.BasicChestBlockEntity;
import com.dyonovan.repairchests.blocks.entity.RepairChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BasicChestBlock extends GenericRepairChestBlock {

    public BasicChestBlock(Properties properties) {
        super(properties, RepairChestsBlockEntityTypes.BASIC_CHEST::get, RepairChestTypes.BASIC);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BasicChestBlockEntity(blockPos, blockState);
    }
}
