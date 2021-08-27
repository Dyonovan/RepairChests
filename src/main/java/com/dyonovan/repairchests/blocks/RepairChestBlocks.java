package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.items.RepairChestItems;
import com.dyonovan.repairchests.items.RepairChestsBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class RepairChestBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RepairChests.MODID);
    public static final DeferredRegister<Item> ITEMS = RepairChestItems.ITEMS;

    public static final RegistryObject<BasicChestBlock> BASIC_CHEST = register("basic_chest",
            () -> new BasicChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)), RepairChestTypes.BASIC);

    public static final RegistryObject<AdvancedChestBlock> ADVANCED_CHEST = register("advanced_chest",
            () -> new AdvancedChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)), RepairChestTypes.ADVANCED);

    public static final RegistryObject<UltimateChestBlock> ULTIMATE_CHEST = register("ultimate_chest",
            () -> new UltimateChestBlock(Block.Properties.of(Material.METAL).strength(3.0F)), RepairChestTypes.ULTIMATE);

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, RepairChestTypes chestType) {
        return register(name, sup, block -> item(block, () -> () -> chestType));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
        RegistryObject<T> ret = registerNoItem(name, sup);
        ITEMS.register(name, itemCreator.apply(ret));
        return ret;
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
        return BLOCKS.register(name, sup);
    }

    private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, Supplier<Callable<RepairChestTypes>> chestType) {
        return () -> new RepairChestsBlockItem(block.get(), new Item.Properties().tab(RepairChests.REPAIR_CHESTS_ITEM_GROUP), chestType);
    }
}
