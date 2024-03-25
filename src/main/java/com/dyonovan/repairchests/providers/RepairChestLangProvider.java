package com.dyonovan.repairchests.providers;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class RepairChestLangProvider extends LanguageProvider {

    public RepairChestLangProvider(PackOutput output, String locale) {
        super(output, RepairChests.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.addBlock(RepairChestBlocks.BASIC_CHEST, "Basic Chest");
        this.addBlock(RepairChestBlocks.ADVANCED_CHEST, "Advanced Chest");
        this.addBlock(RepairChestBlocks.ULTIMATE_CHEST, "Ultimate Chest");

        this.add("itemGroup.repairchest", "Repair Chests");

        this.add("repairchests.container.basic_chest", "Basic Repair Chest");
        this.add("repairchests.container.advanced_chest", "Advanced Repair Chest");
        this.add("repairchests.container.ultimate_chest", "Ultimate Repair Chest");
    }
}
