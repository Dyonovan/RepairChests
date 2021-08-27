package com.dyonovan.repairchests.blocks.entity;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.GenericRepairChestBlock;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.inventory.RepairChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class AbstractRepairChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {

    public abstract int getTickTime();

    private int tickNum = 0;

    private NonNullList<ItemStack> items;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
            AbstractRepairChestBlockEntity.playSound(level, blockPos, blockState, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
            AbstractRepairChestBlockEntity.playSound(level, blockPos, blockState, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int i1) {
            AbstractRepairChestBlockEntity.this.signalOpenCount(level, blockPos, blockState, i, i1);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof RepairChestMenu)) {
                return false;
            } else {
                Container container = ((RepairChestMenu) player.containerMenu).getContainer();
                return container instanceof AbstractRepairChestBlockEntity || container instanceof CompoundContainer && ((CompoundContainer) container).contains(AbstractRepairChestBlockEntity.this);
            }
        }
    };

    private final ChestLidController chestLidController = new ChestLidController();

    private final RepairChestTypes chestType;
    private final Supplier<Block> blockToUse;

    protected AbstractRepairChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RepairChestTypes chestTypeIn, Supplier<Block> blockToUseIn) {
        super(blockEntityType, blockPos, blockState);

        this.items = NonNullList.<ItemStack>withSize(chestTypeIn.size, ItemStack.EMPTY);
        this.chestType = chestTypeIn;
        this.blockToUse = blockToUseIn;
    }

    @Override
    public int getContainerSize() {
        return this.getItems().size();
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent(RepairChests.MODID + ".container." + this.chestType.getId() + "_chest");
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        if (!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.items);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        super.save(compoundTag);

        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.items);
        }

        return compoundTag;
    }

    /*public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState blockState, AbstractRepairChestBlockEntity chestBlockEntity) {
        chestBlockEntity.chestLidController.tickLid();
    }*/

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AbstractRepairChestBlockEntity chestBlockEntity) {
        chestBlockEntity.chestLidController.tickLid();

        ++chestBlockEntity.tickNum;

        if (chestBlockEntity.tickNum >= chestBlockEntity.getTickTime()) {
            for (int x = 0; x < chestBlockEntity.getContainerSize(); x++) {
                ItemStack itemStack = chestBlockEntity.getItems().get(x);

                if (!itemStack.isEmpty() && itemStack.isRepairable() && itemStack.getDamageValue() > 0) {
                    itemStack.setDamageValue(itemStack.getDamageValue() - 1);
                    chestBlockEntity.tickNum = 0;
                    return;
                }
            }

            chestBlockEntity.tickNum = 0;
        }
    }

    static void playSound(Level level, BlockPos blockPos, BlockState blockState, SoundEvent soundEvent) {
        double d0 = (double) blockPos.getX() + 0.5D;
        double d1 = (double) blockPos.getY() + 0.5D;
        double d2 = (double) blockPos.getZ() + 0.5D;

        level.playSound(null, d0, d1, d2, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
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
        return this.items;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = NonNullList.<ItemStack>withSize(this.getChestType().size, ItemStack.EMPTY);

        for (int i = 0; i < itemsIn.size(); i++) {
            if (i < this.items.size()) {
                this.getItems().set(i, itemsIn.get(i));
            }
        }
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return this.chestLidController.getOpenness(partialTicks);
    }

    public static int getOpenCount(BlockGetter blockGetter, BlockPos blockPos) {
        BlockState blockState = blockGetter.getBlockState(blockPos);

        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = blockGetter.getBlockEntity(blockPos);

            if (blockEntity instanceof AbstractRepairChestBlockEntity) {
                return ((AbstractRepairChestBlockEntity) blockEntity).openersCounter.getOpenerCount();
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

    public void wasPlaced(@Nullable LivingEntity livingEntity, ItemStack stack) {
    }

    public void removeAdornments() {
    }

    public RepairChestTypes getChestType() {
        RepairChestTypes type = RepairChestTypes.BASIC;

        if (this.hasLevel()) {
            RepairChestTypes typeFromBlock = GenericRepairChestBlock.getTypeFromBlock(this.getBlockState().getBlock());

            if (typeFromBlock != null)
                type = typeFromBlock;
        }
        return type;
    }

    public Block getBlockToUse() {
        return this.blockToUse.get();
    }

}
