package com.dyonovan.repairchests.inventory;

import com.dyonovan.repairchests.RepairChests;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RepairChestsContainerTypes {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RepairChests.MODID);

    public static final RegistryObject<MenuType<RepairChestMenu>> BASIC_CHEST = CONTAINERS.register("basic_chest", () -> new MenuType<>(RepairChestMenu::createBasicContainer));

    public static final RegistryObject<MenuType<RepairChestMenu>> ADVANCED_CHEST = CONTAINERS.register("advanced_chest", () -> new MenuType<>(RepairChestMenu::createAdvancedContainer));

    public static final RegistryObject<MenuType<RepairChestMenu>> ULTIMATE_CHEST = CONTAINERS.register("ultimate_chest", () -> new MenuType<>(RepairChestMenu::createUltimateContainer));
}
