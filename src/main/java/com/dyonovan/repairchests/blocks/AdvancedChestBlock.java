package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.blockentities.AdvancedChestBlockEntity;
import com.dyonovan.repairchests.blockentities.RepairChestBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AdvancedChestBlock extends GenericRepairChest {

    public static final MapCodec<AdvancedChestBlock> CODEC = simpleCodec(AdvancedChestBlock::new);

    public AdvancedChestBlock(Properties properties) {
        super(properties, RepairChestBlockEntityTypes.ADVANCED_CHEST::get, RepairChestTypes.ADVANCED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AdvancedChestBlockEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
