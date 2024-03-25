package com.dyonovan.repairchests.providers;

import com.dyonovan.repairchests.RepairChests;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SpriteSourceProvider extends net.minecraftforge.common.data.SpriteSourceProvider {

    public SpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, RepairChests.MODID);
    }

    @Override
    protected void addSources() {
        atlas(CHESTS_ATLAS).addSource(new DirectoryLister("model", "model/"));
    }
}
