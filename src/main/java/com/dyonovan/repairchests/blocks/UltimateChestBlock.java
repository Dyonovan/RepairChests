package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.blocks.entity.RepairChestsBlockEntityTypes;
import com.dyonovan.repairchests.blocks.entity.UltimateChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class UltimateChestBlock extends GenericRepairChestBlock {

    public UltimateChestBlock(Properties properties) {
        super(properties, RepairChestsBlockEntityTypes.ULTIMATE_CHEST::get, RepairChestTypes.ULTIMATE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new UltimateChestBlockEntity(blockPos, blockState);
    }
}
