package com.dyonovan.repairchests;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.blocks.entity.RepairChestsBlockEntityTypes;
import com.dyonovan.repairchests.client.render.RepairChestRenderer;
import com.dyonovan.repairchests.client.screen.RepairChestScreen;
import com.dyonovan.repairchests.inventory.RepairChestsContainerTypes;
import com.dyonovan.repairchests.items.RepairChestItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(RepairChests.MODID)
public class RepairChests
{
    public static final String MODID = "repairchests";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final CreativeModeTab REPAIR_CHESTS_ITEM_GROUP = (new CreativeModeTab("repairchests") {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(RepairChestBlocks.BASIC_CHEST.get());
        }
    });

    public RepairChests() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            modBus.addListener(this::setupClient);
        });

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        RepairChestBlocks.BLOCKS.register(modBus);
        RepairChestItems.ITEMS.register(modBus);
        RepairChestsBlockEntityTypes.BLOCK_ENTITIES.register(modBus);
        RepairChestsContainerTypes.CONTAINERS.register(modBus);
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        MenuScreens.register(RepairChestsContainerTypes.BASIC_CHEST.get(), RepairChestScreen::new);
        MenuScreens.register(RepairChestsContainerTypes.ADVANCED_CHEST.get(), RepairChestScreen::new);
        MenuScreens.register(RepairChestsContainerTypes.ULTIMATE_CHEST.get(), RepairChestScreen::new);

        BlockEntityRenderers.register(RepairChestsBlockEntityTypes.BASIC_CHEST.get(), RepairChestRenderer::new);
        BlockEntityRenderers.register(RepairChestsBlockEntityTypes.ADVANCED_CHEST.get(), RepairChestRenderer::new);
        BlockEntityRenderers.register(RepairChestsBlockEntityTypes.ULTIMATE_CHEST.get(), RepairChestRenderer::new);
    }
}
