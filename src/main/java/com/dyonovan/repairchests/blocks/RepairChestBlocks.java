package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.client.renderers.RepairChestItemStackRenderer;
import com.dyonovan.repairchests.items.RepairChestItems;
import com.dyonovan.repairchests.tileenties.AdvancedChestTileEntity;
import com.dyonovan.repairchests.tileenties.BasicChestTileEntity;
import com.dyonovan.repairchests.tileenties.UltimateChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class RepairChestBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, RepairChests.MODID);
    public static final DeferredRegister<Item> ITEMS = RepairChestItems.ITEMS;

    public static final RegistryObject<BasicChestBlock> BASIC_CHEST = register("basic_chest",
            () -> new BasicChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)), () -> basicChestRenderer());

    public static final RegistryObject<AdvancedChestBlock> ADVANCED_CHEST = register("advanced_chest",
            () -> new AdvancedChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)), () -> advancedChestRenderer());

    public static final RegistryObject<UltimateChestBlock> ULTIMATE_CHEST = register("ultimate_chest",
            () -> new UltimateChestBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)), () -> ultimateChestRenderer());

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
        return register(name, sup, block -> item(block, renderMethod));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
        RegistryObject<T> ret = registerNoItem(name, sup);
        ITEMS.register(name, itemCreator.apply(ret));
        return ret;
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
        return BLOCKS.register(name, sup);
    }

    private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(RepairChests.REPAIR_CHESTS_ITEM_GROUP).setISTER(renderMethod));
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> basicChestRenderer() {
        return () -> new RepairChestItemStackRenderer(BasicChestTileEntity::new);
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> advancedChestRenderer() {
        return () -> new RepairChestItemStackRenderer(AdvancedChestTileEntity::new);
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> ultimateChestRenderer() {
        return () -> new RepairChestItemStackRenderer(UltimateChestTileEntity::new);
    }
}
