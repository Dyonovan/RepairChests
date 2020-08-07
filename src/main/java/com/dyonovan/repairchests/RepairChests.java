package com.dyonovan.repairchests;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.client.RepairChestLangProvider;
import com.dyonovan.repairchests.client.RepairChestScreen;
import com.dyonovan.repairchests.client.renderers.RepairChestTileEntityRenderer;
import com.dyonovan.repairchests.containers.RepairChestsContainerTypes;
import com.dyonovan.repairchests.items.RepairChestItems;
import com.dyonovan.repairchests.tileenties.RepairChestTileEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RepairChests.MODID)
public class RepairChests
{
    public static final String MODID = "repairchests";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup REPAIR_CHESTS_ITEM_GROUP = (new ItemGroup("repairchests") {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Blocks.CHEST);
        }
    });

    public RepairChests() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            modBus.addListener(this::setupClient);
        });

        RepairChestBlocks.BLOCKS.register(modBus);
        RepairChestItems.ITEMS.register(modBus);
        RepairChestTileEntityTypes.TILE_ENTITIES.register(modBus);
        RepairChestsContainerTypes.CONTAINERS.register(modBus);
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(RepairChestsContainerTypes.BASIC_CHEST.get(), RepairChestScreen::new);
        ScreenManager.registerFactory(RepairChestsContainerTypes.ADVANCED_CHEST.get(), RepairChestScreen::new);
        ScreenManager.registerFactory(RepairChestsContainerTypes.ULTIMATE_CHEST.get(), RepairChestScreen::new);


        ClientRegistry.bindTileEntityRenderer(RepairChestTileEntityTypes.BASIC_CHEST.get(), RepairChestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(RepairChestTileEntityTypes.ADVANCED_CHEST.get(), RepairChestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(RepairChestTileEntityTypes.ULTIMATE_CHEST.get(), RepairChestTileEntityRenderer::new);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        if (event.includeClient()) {
            generator.addProvider(new RepairChestLangProvider(generator));
        }
    }
}
