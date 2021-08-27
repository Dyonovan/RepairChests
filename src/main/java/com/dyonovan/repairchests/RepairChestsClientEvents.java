package com.dyonovan.repairchests;

import com.dyonovan.repairchests.client.render.RepairChestRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RepairChests.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RepairChestsClientEvents {

    public static final ModelLayerLocation BASIC_CHEST = new ModelLayerLocation(new ResourceLocation(RepairChests.MODID, "basic_chest"), "main");

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BASIC_CHEST, RepairChestRenderer::createBodyLayer);
    }
}
