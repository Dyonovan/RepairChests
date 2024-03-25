package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.blockentities.RepairChestBlockEntityTypes;
import com.dyonovan.repairchests.blockentities.UltimateChestBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class UltimateChestBlock extends GenericRepairChest {

    public static final MapCodec<UltimateChestBlock> CODEC = simpleCodec(UltimateChestBlock::new);

    public UltimateChestBlock(Properties properties) {
        super(properties, RepairChestBlockEntityTypes.ULTIMATE_CHEST::get, RepairChestTypes.ULTIMATE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new UltimateChestBlockEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
