package com.dyonovan.repairchests.containers;

import com.dyonovan.repairchests.RepairChests;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RepairChestsContainerTypes {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RepairChests.MODID);

    public static final RegistryObject<MenuType<RepairChestContainer>> BASIC_CHEST =
            CONTAINERS.register("basic_chest", () -> new MenuType<>(RepairChestContainer::createBasicContainer, FeatureFlags.REGISTRY.allFlags()));

    public static final RegistryObject<MenuType<RepairChestContainer>> ADVANCED_CHEST =
            CONTAINERS.register("advanced_chest", () -> new MenuType<>(RepairChestContainer::createAdvancedContainer, FeatureFlags.REGISTRY.allFlags()));

    public static final RegistryObject<MenuType<RepairChestContainer>> ULTIMATE_CHEST =
            CONTAINERS.register("ultimate_chest", () -> new MenuType<>(RepairChestContainer::createUltimateContainer, FeatureFlags.REGISTRY.allFlags()));
}
