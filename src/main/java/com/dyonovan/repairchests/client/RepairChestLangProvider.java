package com.dyonovan.repairchests.client;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.Util;
import com.dyonovan.repairchests.blocks.RepairChestBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.IOException;
import java.util.function.Supplier;

public class RepairChestLangProvider extends LanguageProvider {

    private static class AccessibleLanguageProvider extends LanguageProvider {

        public AccessibleLanguageProvider(DataGenerator generator, String modid, String locale) {
            super(generator, modid, locale);
        }

        @Override
        public void add(String key, String value) {
            super.add(key, value);
        }

        @Override
        protected void addTranslations() {

        }
    }

    private final AccessibleLanguageProvider upsideDown;

    public RepairChestLangProvider(DataGenerator generator) {
        super(generator, RepairChests.MODID, "en_us");
        this.upsideDown = new AccessibleLanguageProvider(generator, RepairChests.MODID, "en_ud");
    }

    @Override
    protected void addTranslations() {
        this.addBlock(RepairChestBlocks.BASIC_CHEST);

        this.add(RepairChests.REPAIR_CHESTS_ITEM_GROUP, "Repair Chests");
    }

    private String getAutomaticName(Supplier<? extends IForgeRegistryEntry<?>> supplier) {
        return Util.toEnglishName(supplier.get().getRegistryName().getPath());
    }

    private void addBlock(Supplier<? extends Block> block) {
        this.addBlock(block, this.getAutomaticName(block));
    }

    private void add(ItemGroup group, String name) {
        this.add(group.func_242392_c().getString(), name);
    }

    private static final String NORMAL_CHARS =
            /* lowercase */ "abcdefghijklmn\u00F1opqrstuvwxyz" +
            /* uppercase */ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            /*  numbers  */ "0123456789" +
            /*  special  */ "_,;.?!/\\'";
    private static final String UPSIDE_DOWN_CHARS =
            /* lowercase */ "\u0250q\u0254p\u01DD\u025Fb\u0265\u0131\u0638\u029E\u05DF\u026Fuuodb\u0279s\u0287n\u028C\u028Dx\u028Ez" +
            /* uppercase */ "\u2C6F\u15FA\u0186\u15E1\u018E\u2132\u2141HI\u017F\u029E\uA780WNO\u0500\u1F49\u1D1AS\u27D8\u2229\u039BMX\u028EZ" +
            /*  numbers  */ "0\u0196\u1105\u0190\u3123\u03DB9\u312586" +
            /*  special  */ "\u203E'\u061B\u02D9\u00BF\u00A1/\\,";

    static {
        if (NORMAL_CHARS.length() != UPSIDE_DOWN_CHARS.length()) {
            throw new AssertionError("Char maps do not match in length!");
        }
    }

    private String toUpsideDown(String normal) {
        char[] ud = new char[normal.length()];
        for (int i = 0; i < normal.length(); i++) {
            char c = normal.charAt(i);
            if (c == '%') {
                String fmtArg = "";
                while (Character.isDigit(c) || c == '%' || c == '$' || c == 's' || c == 'd') { // TODO this is a bit lazy
                    fmtArg += c;
                    i++;
                    c = i == normal.length() ? 0 : normal.charAt(i);
                }
                i--;
                for (int j = 0; j < fmtArg.length(); j++) {
                    ud[normal.length() - 1 - i + j] = fmtArg.charAt(j);
                }
                continue;
            }
            int lookup = NORMAL_CHARS.indexOf(c);
            if (lookup >= 0) {
                c = UPSIDE_DOWN_CHARS.charAt(lookup);
            }
            ud[normal.length() - 1 - i] = c;
        }
        return new String(ud);
    }

    @Override
    public void add(String key, String value) {
        super.add(key, value);
        this.upsideDown.add(key, this.toUpsideDown(value));
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        super.act(cache);
        this.upsideDown.act(cache);
    }
}
