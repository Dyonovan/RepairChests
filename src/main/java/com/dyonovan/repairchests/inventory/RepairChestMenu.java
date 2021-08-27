package com.dyonovan.repairchests.inventory;

import com.dyonovan.repairchests.blocks.RepairChestTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class RepairChestMenu extends AbstractContainerMenu {

    private final Container container;
    private final RepairChestTypes chestType;

    private RepairChestMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory) {
        this(menuType, containerId, playerInventory, new SimpleContainer(RepairChestTypes.BASIC.size), RepairChestTypes.BASIC);
    }

    public static RepairChestMenu createBasicContainer(int containerId, Inventory playerInventory) {
        return new RepairChestMenu(RepairChestsContainerTypes.BASIC_CHEST.get(), containerId, playerInventory, new SimpleContainer(RepairChestTypes.BASIC.size), RepairChestTypes.BASIC);
    }

    public static RepairChestMenu createBasicContainer(int containerId, Inventory playerInventory, Container inventory) {
        return new RepairChestMenu(RepairChestsContainerTypes.BASIC_CHEST.get(), containerId, playerInventory, inventory, RepairChestTypes.BASIC);
    }

    public static RepairChestMenu createAdvancedContainer(int containerId, Inventory playerInventory) {
        return new RepairChestMenu(RepairChestsContainerTypes.ADVANCED_CHEST.get(), containerId, playerInventory, new SimpleContainer(RepairChestTypes.ADVANCED.size), RepairChestTypes.ADVANCED);
    }

    public static RepairChestMenu createAdvancedContainer(int containerId, Inventory playerInventory, Container inventory) {
        return new RepairChestMenu(RepairChestsContainerTypes.ADVANCED_CHEST.get(), containerId, playerInventory, inventory, RepairChestTypes.ADVANCED);
    }

    public static RepairChestMenu createUltimateContainer(int containerId, Inventory playerInventory) {
        return new RepairChestMenu(RepairChestsContainerTypes.ULTIMATE_CHEST.get(), containerId, playerInventory, new SimpleContainer(RepairChestTypes.ULTIMATE.size), RepairChestTypes.ULTIMATE);
    }

    public static RepairChestMenu createUltimateContainer(int containerId, Inventory playerInventory, Container inventory) {
        return new RepairChestMenu(RepairChestsContainerTypes.ULTIMATE_CHEST.get(), containerId, playerInventory, inventory, RepairChestTypes.ULTIMATE);
    }

    protected RepairChestMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInventory, Container inventory, RepairChestTypes chestType) {
        super(menuType, containerId);

        checkContainerSize(inventory, chestType.size);

        this.container = inventory;
        this.chestType = chestType;

        inventory.startOpen(playerInventory.player);

        if (chestType == RepairChestTypes.BASIC) {
            this.addSlot(new Slot(inventory, 0, 12 + 4 * 18, 8 + 2 * 18));
        } else {
            for (int chestRow = 0; chestRow < chestType.getRowCount(); chestRow++) {
                for (int chestCol = 0; chestCol < chestType.rowLength; chestCol++) {
                    this.addSlot(new Slot(inventory, chestCol + chestRow * chestType.rowLength, 12 + chestCol * 18, 18 + chestRow * 18));
                }
            }
        }

        int leftCol = (chestType.xSize - 162) / 2 + 1;

        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
                this.addSlot(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, chestType.ySize - (4 - playerInvRow) * 18 - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            this.addSlot(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, chestType.ySize - 24));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < this.chestType.size) {
                if (!this.moveItemStackTo(itemstack1, this.chestType.size, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.chestType.size, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.container.stopOpen(playerIn);
    }

    public Container getContainer() {
        return this.container;
    }

    @OnlyIn(Dist.CLIENT)
    public RepairChestTypes getChestType() {
        return this.chestType;
    }
}
