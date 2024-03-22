package com.dyonovan.repairchests;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.items.RepairChestItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RepairChestCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RepairChests.MODID);

    public static final RegistryObject<CreativeModeTab> REPAIR_CHEST_TAB = CREATIVE_MODE_TABS.register("repairchests", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup.repairchest"))
            .icon(() -> new ItemStack(RepairChestBlocks.BASIC_CHEST.get()))
            .displayItems((parameters, output) -> {
                for (final RegistryObject<Item> item : RepairChestItems.ITEMS.getEntries())
                    output.accept(item.get());
            }).build());
}
