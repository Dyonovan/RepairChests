package com.dyonovan.repairchests.items;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.client.renderers.RepairChestItemStackRenderer;
import com.dyonovan.repairchests.blockentities.AdvancedChestBlockEntity;
import com.dyonovan.repairchests.blockentities.BasicChestBlockEntity;
import com.dyonovan.repairchests.blockentities.UltimateChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RepairChestBlockItem extends BlockItem {

    protected Supplier<RepairChestTypes> type;

    public RepairChestBlockItem(Block block, Properties properties, Supplier<Callable<RepairChestTypes>> chestType) {
        super(block, properties);

        RepairChestTypes tempType = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, chestType);

        this.type = tempType == null ? null : () -> tempType;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);

        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                Supplier<BlockEntity> modelToUse;

                switch (type.get()) {
                    case ADVANCED -> modelToUse = () -> new AdvancedChestBlockEntity(BlockPos.ZERO, RepairChestBlocks.ADVANCED_CHEST.get().defaultBlockState());
                    case ULTIMATE -> modelToUse = () -> new UltimateChestBlockEntity(BlockPos.ZERO, RepairChestBlocks.ULTIMATE_CHEST.get().defaultBlockState());
                    default -> modelToUse = () -> new BasicChestBlockEntity(BlockPos.ZERO, RepairChestBlocks.BASIC_CHEST.get().defaultBlockState());
                }
                return new RepairChestItemStackRenderer<>(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), modelToUse);
            }
        });
    }

}
