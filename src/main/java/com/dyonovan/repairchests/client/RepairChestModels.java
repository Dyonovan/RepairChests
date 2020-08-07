package com.dyonovan.repairchests.client;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RepairChests.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RepairChestModels {

    public static final ResourceLocation BASIC_CHEST_LOCATION = new ResourceLocation(RepairChests.MODID, "model/basic_chest");
    public static final ResourceLocation ADVANCED_CHEST_LOCATION = new ResourceLocation(RepairChests.MODID, "model/advanced_chest");
    public static final ResourceLocation ULTIMATE_CHEST_LOCATION = new ResourceLocation(RepairChests.MODID, "model/ultimate_chest");
    public static final ResourceLocation VANILLA_CHEST_LOCATION = new ResourceLocation("entity/chest/normal");

    public static ResourceLocation chooseChestTexture(RepairChestTypes type) {
        switch (type) {
            case BASIC:
                return BASIC_CHEST_LOCATION;
            case ADVANCED:
                return ADVANCED_CHEST_LOCATION;
            case ULTIMATE:
                return ULTIMATE_CHEST_LOCATION;
            default:
                return VANILLA_CHEST_LOCATION;
        }
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            return;
        }

        event.addSprite(BASIC_CHEST_LOCATION);
        event.addSprite(ADVANCED_CHEST_LOCATION);
        event.addSprite(ULTIMATE_CHEST_LOCATION);
    }
}
