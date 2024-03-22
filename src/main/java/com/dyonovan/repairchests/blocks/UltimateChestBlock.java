package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.tileenties.RepairChestTileEntityTypes;
import com.dyonovan.repairchests.tileenties.UltimateChestTileEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class UltimateChestBlock extends GenericRepairChest {

    public static final MapCodec<UltimateChestBlock> CODEC = simpleCodec(UltimateChestBlock::new);

    public UltimateChestBlock(Properties properties) {
        super(properties, RepairChestTileEntityTypes.ULTIMATE_CHEST::get, RepairChestTypes.ULTIMATE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new UltimateChestTileEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
