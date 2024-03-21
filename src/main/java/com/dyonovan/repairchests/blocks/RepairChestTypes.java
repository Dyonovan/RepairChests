package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.RepairChests;
import com.dyonovan.repairchests.Util;
import com.dyonovan.repairchests.tileenties.AdvancedChestTileEntity;
import com.dyonovan.repairchests.tileenties.BasicChestTileEntity;
import com.dyonovan.repairchests.tileenties.GenericRepairChestTileEntity;
import com.dyonovan.repairchests.tileenties.UltimateChestTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public enum RepairChestTypes implements StringRepresentable {

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

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public boolean isTransparent() {
        return false;
    }

    public static List<Block> get(RepairChestTypes type) {
        return switch (type) {
            case BASIC -> List.of(RepairChestBlocks.BASIC_CHEST.get());
            case ADVANCED -> List.of(RepairChestBlocks.ADVANCED_CHEST.get());
            case ULTIMATE -> List.of(RepairChestBlocks.ULTIMATE_CHEST.get());
            default -> List.of(Blocks.CHEST);
        };
    }

    public GenericRepairChestTileEntity makeEntity(BlockPos blockPos, BlockState blockState) {
        return switch (this) {
            case BASIC -> new BasicChestTileEntity(blockPos, blockState);
            case ADVANCED -> new AdvancedChestTileEntity(blockPos, blockState);
            case ULTIMATE -> new UltimateChestTileEntity(blockPos, blockState);
            default -> null;
        };
    }

    @Override
    public String getSerializedName() {
        return this.getEnglishName();
    }
}
