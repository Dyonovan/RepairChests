package com.dyonovan.repairchests.client;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import net.minecraft.resources.ResourceLocation;

public class RepairChestModels {

    public static final ResourceLocation BASIC_CHEST_LOCATION = new ResourceLocation(RepairChests.MODID, "model/basic_chest");
    public static final ResourceLocation ADVANCED_CHEST_LOCATION = new ResourceLocation(RepairChests.MODID, "model/advanced_chest");
    public static final ResourceLocation ULTIMATE_CHEST_LOCATION = new ResourceLocation(RepairChests.MODID, "model/ultimate_chest");
    public static final ResourceLocation VANILLA_CHEST_LOCATION = new ResourceLocation("entity/chest/normal");

    public static ResourceLocation chooseChestTexture(RepairChestTypes type) {
        return switch (type) {
            case BASIC -> BASIC_CHEST_LOCATION;
            case ADVANCED -> ADVANCED_CHEST_LOCATION;
            case ULTIMATE -> ULTIMATE_CHEST_LOCATION;
            default -> VANILLA_CHEST_LOCATION;
        };
    }
}
