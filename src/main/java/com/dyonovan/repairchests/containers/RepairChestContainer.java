package com.dyonovan.repairchests.containers;

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

public class RepairChestContainer extends AbstractContainerMenu {

    private final Container container;

    private final RepairChestTypes chestType;

    private RepairChestContainer(MenuType<?> containerType, int windowId, Inventory playerInventory) {
        this(containerType, windowId, playerInventory, new SimpleContainer(RepairChestTypes.BASIC.size), RepairChestTypes.BASIC);
    }

    public static RepairChestContainer createBasicContainer(int windowId, Inventory playerInventory) {
        return new RepairChestContainer(RepairChestsContainerTypes.BASIC_CHEST.get(), windowId, playerInventory, new SimpleContainer(RepairChestTypes.BASIC.size), RepairChestTypes.BASIC);
    }

    public static RepairChestContainer createBasicContainer(int windowId, Inventory playerInventory, Container inventory) {
        return new RepairChestContainer(RepairChestsContainerTypes.BASIC_CHEST.get(), windowId, playerInventory, inventory, RepairChestTypes.BASIC);
    }

    public static RepairChestContainer createAdvancedContainer(int windowId, Inventory playerInventory) {
        return new RepairChestContainer(RepairChestsContainerTypes.ADVANCED_CHEST.get(), windowId, playerInventory, new SimpleContainer(RepairChestTypes.ADVANCED.size), RepairChestTypes.ADVANCED);
    }

    public static RepairChestContainer createAdvancedContainer(int windowId, Inventory playerInventory, Container inventory) {
        return new RepairChestContainer(RepairChestsContainerTypes.ADVANCED_CHEST.get(), windowId, playerInventory, inventory, RepairChestTypes.ADVANCED);
    }

    public static RepairChestContainer createUltimateContainer(int windowId, Inventory playerInventory) {
        return new RepairChestContainer(RepairChestsContainerTypes.ULTIMATE_CHEST.get(), windowId, playerInventory, new SimpleContainer(RepairChestTypes.ULTIMATE.size), RepairChestTypes.ULTIMATE);
    }

    public static RepairChestContainer createUltimateContainer(int windowId, Inventory playerInventory, Container inventory) {
        return new RepairChestContainer(RepairChestsContainerTypes.ULTIMATE_CHEST.get(), windowId, playerInventory, inventory, RepairChestTypes.ULTIMATE);
    }

    public RepairChestContainer(MenuType<?> containerType, int windowId, Inventory playerInventory, Container container, RepairChestTypes chestTypes) {
        super(containerType, windowId);
        checkContainerSize(container, chestTypes.size);

        this.container = container;
        this.chestType = chestTypes;

       container.startOpen(playerInventory.player);

        if (chestType == RepairChestTypes.BASIC) {
            this.addSlot(new Slot(container, 0, 12 + 4 * 18, 8 + 2 * 18));
        } else {
            for (int chestRow = 0; chestRow < chestType.getRowCount(); chestRow++) {
                for (int chestCol = 0; chestCol < chestType.rowLength; chestCol++) {
                    this.addSlot(new Slot(container, chestCol + chestRow * chestType.rowLength, 12 + chestCol * 18, 18 + chestRow * 18));
                }
            }
        }

        int leftCol = (chestType.xSize - 162) / 2 + 1;

        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
                this.addSlot(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18,
                        chestType.ySize - (4 - playerInvRow) * 18 - 10));
            }
        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            this.addSlot(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, chestType.ySize - 24));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.container.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < this.chestType.size) {
                if (!this.moveItemStackTo(itemstack1, this.chestType.size, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 0, this.chestType.size, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            else {
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
