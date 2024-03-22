package com.dyonovan.repairchests.client;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.client.renderers.RepairChestTileEntityRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RepairChests.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RepairChestsClientEvents {

    public static final ModelLayerLocation REPAIR_CHEST = new ModelLayerLocation(new ResourceLocation(RepairChests.MODID, "repair_chest"), "main");

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(REPAIR_CHEST, RepairChestTileEntityRenderer::createBodyLayer);
    }
}
