package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.tileenties.BasicChestTileEntity;
import com.dyonovan.repairchests.tileenties.RepairChestTileEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BasicChestBlock extends GenericRepairChest {

    public static final MapCodec<BasicChestBlock> CODEC = simpleCodec(BasicChestBlock::new);

    public BasicChestBlock(Properties properties) {
        super(properties, RepairChestTileEntityTypes.BASIC_CHEST::get, RepairChestTypes.BASIC);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BasicChestTileEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
