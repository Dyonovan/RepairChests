package com.dyonovan.repairchests;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * This file was created for InventoryRepair
 * <p>
 * InventoryRepair is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author James Rogers - Dyonovan
 * @since 2020-09-08
 */
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static class General {

        public final ForgeConfigSpec.ConfigValue<Integer> basicRepairTime;
        public final ForgeConfigSpec.ConfigValue<Integer> advancedRepairTime;
        public final ForgeConfigSpec.ConfigValue<Integer> ultimateRepairTime;


        public General(ForgeConfigSpec.Builder builder) {
            builder.push("Repair Times");
            basicRepairTime = builder
                    .comment("How many seconds to repair 1 point of damage")
                    .define("basicRepairTime", 10);
            advancedRepairTime = builder
                    .comment("How many seconds to repair 1 point of damage")
                    .define("advancedRepairTime", 5);
            ultimateRepairTime = builder
                    .comment("How many seconds to repair 1 point of damage")
                    .define("ultimateRepairTime", 2);
        }
    }
}