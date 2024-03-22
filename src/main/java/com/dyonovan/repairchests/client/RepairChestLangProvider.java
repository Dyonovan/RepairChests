package com.dyonovan.repairchests.client;

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
    }
}
