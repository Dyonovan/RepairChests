package com.dyonovan.repairchests;

import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import com.dyonovan.repairchests.providers.RepairChestCreativeTabs;
import com.dyonovan.repairchests.providers.RepairChestLangProvider;
import com.dyonovan.repairchests.client.RepairChestScreen;
import com.dyonovan.repairchests.providers.RepairChestsLootTableProvider;
import com.dyonovan.repairchests.client.renderers.RepairChestBlockEntityRenderer;
import com.dyonovan.repairchests.containers.RepairChestsContainerTypes;
import com.dyonovan.repairchests.items.RepairChestItems;
import com.dyonovan.repairchests.blockentities.RepairChestBlockEntityTypes;
import com.dyonovan.repairchests.providers.SpriteSourceProvider;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RepairChests.MODID)
public class RepairChests {
    public static final String MODID = "repairchests";

    /*public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register(MODID, () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get());
            }).build());*/

    public RepairChests() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modBus.addListener(this::setupClient);
        });

        RepairChestBlocks.BLOCKS.register(modBus);
        RepairChestItems.ITEMS.register(modBus);
        RepairChestBlockEntityTypes.TILE_ENTITIES.register(modBus);
        RepairChestsContainerTypes.CONTAINERS.register(modBus);
        RepairChestCreativeTabs.CREATIVE_MODE_TABS.register(modBus);
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        MenuScreens.register(RepairChestsContainerTypes.BASIC_CHEST.get(), RepairChestScreen::new);
        MenuScreens.register(RepairChestsContainerTypes.ADVANCED_CHEST.get(), RepairChestScreen::new);
        MenuScreens.register(RepairChestsContainerTypes.ULTIMATE_CHEST.get(), RepairChestScreen::new);

        BlockEntityRenderers.register(RepairChestBlockEntityTypes.BASIC_CHEST.get(), RepairChestBlockEntityRenderer::new);
        BlockEntityRenderers.register(RepairChestBlockEntityTypes.ADVANCED_CHEST.get(), RepairChestBlockEntityRenderer::new);
        BlockEntityRenderers.register(RepairChestBlockEntityTypes.ULTIMATE_CHEST.get(), RepairChestBlockEntityRenderer::new);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper ext = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new RepairChestsLootTableProvider(packOutput));

        generator.addProvider(event.includeClient(), new RepairChestLangProvider(packOutput, "en_us"));
        generator.addProvider(event.includeClient(), new SpriteSourceProvider(packOutput, ext));

    }
}
