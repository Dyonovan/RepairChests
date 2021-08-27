package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.blocks.entity.AdvancedChestBlockEntity;
import com.dyonovan.repairchests.blocks.entity.RepairChestsBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AdvancedChestBlock extends GenericRepairChestBlock {

    public AdvancedChestBlock(Properties properties) {
        super(properties, RepairChestsBlockEntityTypes.ADVANCED_CHEST::get, RepairChestTypes.ADVANCED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AdvancedChestBlockEntity(blockPos, blockState);
    }
}
