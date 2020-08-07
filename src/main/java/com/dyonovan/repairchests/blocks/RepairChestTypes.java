package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.Util;
import com.dyonovan.repairchests.tileenties.AdvancedChestTileEntity;
import com.dyonovan.repairchests.tileenties.BasicChestTileEntity;
import com.dyonovan.repairchests.tileenties.GenericRepairChestTileEntity;
import com.dyonovan.repairchests.tileenties.UltimateChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Locale;

public enum RepairChestTypes implements IStringSerializable {

    BASIC(1, 1, 184, 184, new ResourceLocation(RepairChests.MODID, "textures/gui/basic_container.png"), 256, 256),
    ADVANCED(9, 9, 184, 133, new ResourceLocation(RepairChests.MODID, "textures/gui/advanced_container.png"), 256, 256),
    ULTIMATE(18, 9, 184, 150, new ResourceLocation(RepairChests.MODID, "textures/gui/ultimate_container.png"), 256, 256);

    private final String name;
    public final int size;
    public final int rowLength;
    public final int xSize;
    public final int ySize;
    public final ResourceLocation guiTexture;
    public final int textureXSize;
    public final int textureYSize;

    RepairChestTypes(int size, int rowLength, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
        this(null, size, rowLength, xSize, ySize, guiTexture, textureXSize, textureYSize);
    }

    RepairChestTypes(@Nullable String name, int size, int rowLength, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
        this.name = name == null ? Util.toEnglishName(this.name()) : name;
        this.size = size;
        this.rowLength = rowLength;
        this.xSize = xSize;
        this.ySize = ySize;
        this.guiTexture = guiTexture;
        this.textureXSize = textureXSize;
        this.textureYSize = textureYSize;
    }

    public String getId() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String getEnglishName() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.getEnglishName();
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public boolean isTransparent() {
        //return this == CRYSTAL;
        return false;
    }

    public static Block get(RepairChestTypes type) {
        switch (type) {
            case BASIC:
                return RepairChestBlocks.BASIC_CHEST.get();
            case ADVANCED:
                return RepairChestBlocks.ADVANCED_CHEST.get();
            case ULTIMATE:
                return RepairChestBlocks.ULTIMATE_CHEST.get();
            default:
                return Blocks.CHEST;
        }
    }

    public GenericRepairChestTileEntity makeEntity() {
        switch (this) {
            case BASIC:
                return new BasicChestTileEntity();
            case ADVANCED:
                return new AdvancedChestTileEntity();
            case ULTIMATE:
                return new UltimateChestTileEntity();
            default:
                return null;
        }
    }
}
