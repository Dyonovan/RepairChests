package com.dyonovan.repairchests.client.renderers;

import com.dyonovan.repairchests.blocks.GenericRepairChest;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.client.RepairChestModels;
import com.dyonovan.repairchests.client.RepairChestsClientEvents;
import com.dyonovan.repairchests.tileenties.GenericRepairChestTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RepairChestTileEntityRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {

    private final ModelPart chestLid;
    private final ModelPart chestBottom;
    private final ModelPart chestLock;

    private final BlockEntityRenderDispatcher renderer;

    private static final List<ModelItem> MODEL_ITEMS = Arrays.asList(
            new ModelItem(new Vector3f(0.3F, 0.45F, 0.3F), 3.0F),
            new ModelItem(new Vector3f(0.7F, 0.45F, 0.3F), 3.0F),
            new ModelItem(new Vector3f(0.3F, 0.45F, 0.7F), 3.0F),
            new ModelItem(new Vector3f(0.7F, 0.45F, 0.7F), 3.0F),
            new ModelItem(new Vector3f(0.3F, 0.1F, 0.3F), 3.0F),
            new ModelItem(new Vector3f(0.7F, 0.1F, 0.3F), 3.0F),
            new ModelItem(new Vector3f(0.3F, 0.1F, 0.7F), 3.0F),
            new ModelItem(new Vector3f(0.7F, 0.1F, 0.7F), 3.0F),
            new ModelItem(new Vector3f(0.5F, 0.32F, 0.5F), 3.0F)
    );

    public RepairChestTileEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(RepairChestsClientEvents.REPAIR_CHEST);

        this.renderer = context.getBlockEntityRenderDispatcher();
        this.chestBottom = modelPart.getChild("basic_bottom");
        this.chestLid = modelPart.getChild("basic_lid");
        this.chestLock = modelPart.getChild("basic_lock");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("basic_bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
        partDefinition.addOrReplaceChild("basic_lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        partDefinition.addOrReplaceChild("basic_lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        GenericRepairChestTileEntity tileEntity = (GenericRepairChestTileEntity) tileEntityIn;

        Level level = tileEntity.getLevel();
        boolean useTileEntityBlockState = level != null;

        BlockState blockstate = useTileEntityBlockState ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().defaultBlockState().setValue(GenericRepairChest.FACING, Direction.SOUTH);
        Block block = blockstate.getBlock();
        RepairChestTypes chestType = RepairChestTypes.BASIC;
        RepairChestTypes actualType = GenericRepairChest.getTypeFromBlock(block);

        if (actualType != null) {
            chestType = actualType;
        }

        if (block instanceof GenericRepairChest genericRepairChest) {
            poseStack.pushPose();

            float f = blockstate.getValue(GenericRepairChest.FACING).toYRot();

            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-f));
            poseStack.translate(-0.5D, -0.5D, -0.5D);

            DoubleBlockCombiner.NeighborCombineResult<? extends GenericRepairChestTileEntity> neighborCombineResult;

            if (useTileEntityBlockState)
                neighborCombineResult = genericRepairChest.combine(blockstate, level, tileEntityIn.getBlockPos(), true);
            else
                neighborCombineResult = DoubleBlockCombiner.Combiner::acceptNone;

            float openness = neighborCombineResult.<Float2FloatFunction>apply(GenericRepairChest.opennessCombiner(tileEntity)).get(partialTicks);
            openness = 1.0F - openness;
            openness = 1.0F - openness * openness * openness;

            int brightness = neighborCombineResult.<Int2IntFunction>apply(new BrightnessCombiner<>()).applyAsInt(combinedLightIn);

            boolean trapped = true;

            Material material = new Material(Sheets.CHEST_SHEET, RepairChestModels.chooseChestTexture(chestType));

            VertexConsumer vertexConsumer = material.buffer(bufferIn, RenderType::entityCutout);

            this.render(poseStack, vertexConsumer, this.chestLid, this.chestLock, this.chestBottom, openness, brightness, combinedOverlayIn);

            poseStack.popPose();
        }
    }

    private void render(PoseStack poseStack, VertexConsumer vertexConsumer, ModelPart lid, ModelPart lock, ModelPart bottom, float openness, int brightness, int combinedOverlayIn) {
        lid.xRot = -(openness * ((float) Math.PI / 2F));
        lock.xRot = lid.xRot;

        lid.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
        lock.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
        bottom.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
    }

    /*public static void renderItem(PoseStack matrices, MultiBufferSource buffer, ItemStack item, ModelItem modelItem, float rotation, int light) {
        // if no stack, skip
        if (item.isEmpty()) return;

        // start rendering
        matrices.pushPose();
        Vector3f center = modelItem.getCenter();
        matrices.translate(center.x(), center.y(), center.z());

        matrices.mulPose(Axis.YP.rotationDegrees(rotation));

        // scale
        float scale = modelItem.getSizeScaled();
        matrices.scale(scale, scale, scale);

        // render the actual item
        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, matrices, buffer, null, 0);

        matrices.popPose();
    }*/
}
