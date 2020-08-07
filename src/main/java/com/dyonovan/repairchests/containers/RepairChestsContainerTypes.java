package com.dyonovan.repairchests.containers;

import com.dyonovan.repairchests.RepairChests;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RepairChestsContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RepairChests.MODID);

    public static final RegistryObject<ContainerType<RepairChestContainer>> BASIC_CHEST =
            CONTAINERS.register("basic_chest", () -> new ContainerType<>(RepairChestContainer::createBasicContainer));

    public static final RegistryObject<ContainerType<RepairChestContainer>> ADVANCED_CHEST =
            CONTAINERS.register("advanced_chest", () -> new ContainerType<>(RepairChestContainer::createAdvancedContainer));

    public static final RegistryObject<ContainerType<RepairChestContainer>> ULTIMATE_CHEST =
            CONTAINERS.register("ultimate_chest", () -> new ContainerType<>(RepairChestContainer::createUltimateContainer));
}
