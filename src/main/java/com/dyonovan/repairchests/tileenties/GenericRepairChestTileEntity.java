package com.dyonovan.repairchests.tileenties;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.GenericRepairChest;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.containers.RepairChestContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public abstract class GenericRepairChestTileEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {

    private static final int EVENT_SET_OPEN_COUNT = 1;
    private NonNullList<ItemStack> chestContents;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
            GenericRepairChestTileEntity.playSound(level, blockPos, blockState, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
            GenericRepairChestTileEntity.playSound(level, blockPos, blockState, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int i1) {
            GenericRepairChestTileEntity.this.signalOpenCount(level, blockPos, blockState, i, i1);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof RepairChestContainer)) {
                return false;
            } else {
                Container container = ((RepairChestContainer) player.containerMenu).getContainer();
                return container instanceof GenericRepairChestTileEntity || container instanceof CompoundContainer &&
                        ((CompoundContainer) container).contains(GenericRepairChestTileEntity.this);
            }
        }
    };

    private final ChestLidController chestLidController = new ChestLidController();

    private RepairChestTypes chestType;
    private Supplier<Block> blockToUse;

    private int tickNum;

    protected GenericRepairChestTileEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState state, RepairChestTypes chestTypeIn, Supplier<Block> blockToUseIn) {
        super(blockEntityType, blockPos, state);

        this.chestContents = NonNullList.withSize(chestTypeIn.size, ItemStack.EMPTY);
        this.chestType = chestTypeIn;
        this.blockToUse = blockToUseIn;
    }

    @Override
    public int getContainerSize() {
        return this.getItems().size();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(RepairChests.MODID + ".container." + this.chestType.getId() + "_chest");
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        this.chestContents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        if (!this.tryLoadLootTable(compound)) {
            ContainerHelper.loadAllItems(compound, this.chestContents);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.chestContents);
        }
    }

    public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState blockState, GenericRepairChestTileEntity chestTileEntity) {
        chestTileEntity.chestLidController.tickLid();
    }

    static void playSound(Level level, BlockPos blockPos, BlockState blockState, SoundEvent soundIn) {
        double d0 = (double) blockPos.getX() + 0.5D;
        double d1 = (double) blockPos.getY() + 0.5D;
        double d2 = (double) blockPos.getZ() + 0.5D;

        level.playSound(null, d0, d1, d2, soundIn, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.chestLidController.shouldBeOpen(type > 0);
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        this.chestContents = NonNullList.withSize(this.getChestType().size, ItemStack.EMPTY);

        for (int i = 0; i < itemsIn.size(); i++) {
            if (i < this.chestContents.size()) {
                this.getItems().set(i, itemsIn.get(i));
            }
        }
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return this.chestLidController.getOpenness(partialTicks);
    }

    public static int getOpenCount(BlockGetter blockGetter, BlockPos blockPos) {
        BlockState blockstate = blockGetter.getBlockState(blockPos);

        if (blockstate.hasBlockEntity()) {
            BlockEntity blockentity = blockGetter.getBlockEntity(blockPos);

            if (blockentity instanceof GenericRepairChestTileEntity) {
                return ((GenericRepairChestTileEntity) blockentity).openersCounter.getOpenerCount();
            }
        }
        return 0;
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int previousCount, int newCount) {
        Block block = blockState.getBlock();
        level.blockEvent(blockPos, block, 1, newCount);
    }

    public void wasPlaced(LivingEntity livingEntity, ItemStack stack) {
    }

    public void removeAdornments() {
    }

    public RepairChestTypes getChestType() {
        RepairChestTypes type = RepairChestTypes.BASIC;

        if (this.hasLevel()) {
            RepairChestTypes typeFromBlock = GenericRepairChest.getTypeFromBlock(this.getBlockState().getBlock());

            if (typeFromBlock != null) {
                type = typeFromBlock;
            }
        }
        return type;
    }

    public Block getBlockToUse() {
        return this.blockToUse.get();
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, GenericRepairChestTileEntity blockEntity) {
        // check chest contains and repair if item is repairable
        ++blockEntity.tickNum;
        int ticktime = blockEntity.chestType == RepairChestTypes.BASIC ? 600 : blockEntity.chestType == RepairChestTypes.ADVANCED ? 400 : blockEntity.chestType == RepairChestTypes.ULTIMATE ? 200 : 600;
        if (blockEntity.tickNum >= ticktime) {
            for (int c = 0; c < blockEntity.getContainerSize(); c++) {
                ItemStack stack = blockEntity.getItem(c);

                if (!stack.isEmpty() && stack.isRepairable() && stack.getDamageValue() > 0) {
                    stack.setDamageValue(stack.getDamageValue() - 1);
                    blockEntity.tickNum = 0;
                    return;
                }
            }
            blockEntity.tickNum = 0;
        }

        blockEntity.chestLidController.tickLid();
    }


}
