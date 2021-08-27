package com.dyonovan.repairchests.client.render;

import com.dyonovan.repairchests.RepairChestsClientEvents;
import com.dyonovan.repairchests.blocks.GenericRepairChestBlock;
import com.dyonovan.repairchests.blocks.RepairChestTypes;
import com.dyonovan.repairchests.blocks.entity.AbstractRepairChestBlockEntity;
import com.dyonovan.repairchests.client.model.RepairChestModels;
import com.dyonovan.repairchests.client.model.inventory.ModelItem;
import com.google.common.primitives.SignedBytes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
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
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RepairChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {

    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;

    private static ItemEntity customItem;
    private static ItemEntityRenderer itemRenderer;
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

    public RepairChestRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(RepairChestsClientEvents.BASIC_CHEST);

        this.renderer = context.getBlockEntityRenderDispatcher();
        this.bottom = modelPart.getChild("basic_bottom");
        this.lid = modelPart.getChild("basic_lid");
        this.lock = modelPart.getChild("basic_lock");
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
    public void render(T tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        AbstractRepairChestBlockEntity tileEntity = (AbstractRepairChestBlockEntity) tileEntityIn;

        Level level = tileEntity.getLevel();
        boolean useTileEntityBlockState = level != null;

        BlockState blockState = useTileEntityBlockState ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().defaultBlockState().setValue(GenericRepairChestBlock.FACING, Direction.SOUTH);
        Block block = blockState.getBlock();
        RepairChestTypes chestType = RepairChestTypes.BASIC;
        RepairChestTypes actualType = GenericRepairChestBlock.getTypeFromBlock(block);

        if (actualType != null) {
            chestType = actualType;
        }

        if (block instanceof GenericRepairChestBlock abstractChestBlock) {
            poseStack.pushPose();

            float f = blockState.getValue(GenericRepairChestBlock.FACING).toYRot();

            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            poseStack.translate(-0.5D, -0.5D, -0.5D);

            DoubleBlockCombiner.NeighborCombineResult<? extends AbstractRepairChestBlockEntity> neighborCombineResult;

            if (useTileEntityBlockState) {
                neighborCombineResult = abstractChestBlock.combine(blockState, level, tileEntityIn.getBlockPos(), true);
            } else {
                neighborCombineResult = DoubleBlockCombiner.Combiner::acceptNone;
            }

            float openness = neighborCombineResult.apply(GenericRepairChestBlock.opennessCombiner(tileEntity)).get(partialTicks);
            openness = 1.0F - openness;
            openness = 1.0F - openness * openness * openness;

            int brightness = neighborCombineResult.apply(new BrightnessCombiner<>()).applyAsInt(combinedLightIn);

            Material material = new Material(Sheets.CHEST_SHEET, RepairChestModels.chooseChestTexture(chestType));

            VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityCutout);

            this.render(poseStack, vertexConsumer, this.lid, this.lock, this.bottom, openness, brightness, combinedOverlayIn);

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

    /**
     * Renders a single item in a TESR
     *
     * @param matrices  Matrix stack instance
     * @param buffer    Buffer instance
     * @param item      Item to render
     * @param modelItem Model items for render information
     * @param light     Model light
     */
    public static void renderItem(PoseStack matrices, MultiBufferSource buffer, ItemStack item, ModelItem modelItem, float rotation, int light, float partialTicks) {
        // if no stack, skip
        if (item.isEmpty()) return;

        customItem.setItem(item);

        // start rendering
        matrices.pushPose();
        Vector3f center = modelItem.getCenter();
        matrices.translate(center.x(), center.y(), center.z());

        matrices.mulPose(Vector3f.YP.rotation(rotation));

        // scale
        float scale = modelItem.getSizeScaled();
        matrices.scale(scale, scale, scale);

        // render the actual item
        if (itemRenderer == null) {
            itemRenderer = new ItemEntityRenderer(new EntityRendererProvider.Context(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().font)) {
                @Override
                public int getRenderAmount(ItemStack stack) {
                    return SignedBytes.saturatedCast(Math.min(stack.getCount() / 32, 15) + 1);
                }

                @Override
                public boolean shouldBob() {
                    return false;
                }

                @Override
                public boolean shouldSpreadItems() {
                    return true;
                }
            };
        }

        itemRenderer.render(customItem, 0F, partialTicks, matrices, buffer, light);
        matrices.popPose();
    }
}
