package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.tileenties.GenericRepairChestTileEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class GenericRepairChest extends Block implements IWaterLoggable {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape CHEST_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    private final RepairChestTypes type;
    private final Supplier<TileEntityType<? extends GenericRepairChestTileEntity>> tileEntityTypeSupplier;

    public GenericRepairChest(RepairChestTypes typeIn, Supplier<TileEntityType<? extends GenericRepairChestTileEntity>> tileEntityTypeSupplierIn, Properties properties) {
        super(properties);

        this.type = typeIn;
        this.tileEntityTypeSupplier = tileEntityTypeSupplierIn;

        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return CHEST_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getPlacementHorizontalFacing().getOpposite();
        FluidState iFluidState = context.getWorld().getFluidState(context.getPos());

        return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, iFluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof GenericRepairChestTileEntity) {
            ((GenericRepairChestTileEntity) tileEntity).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof GenericRepairChestTileEntity) {
                ((GenericRepairChestTileEntity) tileEntity).removeAdornments();
                InventoryHelper.dropInventoryItems(worldIn, pos, (GenericRepairChestTileEntity) tileEntity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            INamedContainerProvider iNamedContainerProvider = this.getContainer(state, worldIn, pos);
            if (iNamedContainerProvider != null) {
                player.openContainer(iNamedContainerProvider);
                player.addStat(this.getOpenStat());
            }
        }
        return ActionResultType.SUCCESS;
    }

    protected Stat<ResourceLocation> getOpenStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    @Override
    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        return tileEntity instanceof INamedContainerProvider ? (INamedContainerProvider) tileEntity : null;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        return tileEntity != null && tileEntity.receiveClientEvent(id, param);
    }

    private static boolean isBlocked(IWorld iWorld, BlockPos blockPos) {
        return isBelowSolidBlock(iWorld, blockPos) || isCatSittingOn(iWorld, blockPos);
    }

    private static boolean isBelowSolidBlock(IBlockReader iBlockReader, BlockPos worldIn) {
        BlockPos blockpos = worldIn.up();
        return iBlockReader.getBlockState(blockpos).isNormalCube(iBlockReader, blockpos);
    }

    private static boolean isCatSittingOn(IWorld iWorld, BlockPos blockPos) {
        List<CatEntity> list = iWorld.getEntitiesWithinAABB(CatEntity.class, new AxisAlignedBB(blockPos.getX(),
                blockPos.getY() + 1, blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 2, blockPos.getZ() + 1));
        if (!list.isEmpty()) {
            for (CatEntity catentity : list) {
                if (catentity.func_233684_eK_()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory((IInventory) worldIn.getTileEntity(pos));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public static RepairChestTypes getTypeFromItem(Item item) {
        return getTypeFromBlock(Block.getBlockFromItem(item));
    }

    public static RepairChestTypes getTypeFromBlock(Block block) {
        return block instanceof GenericRepairChest ? ((GenericRepairChest) block).getType() : null;
    }

    public RepairChestTypes getType() {
        return this.type;
    }

    @OnlyIn(Dist.CLIENT)
    public static TileEntityMerger.ICallback<GenericRepairChestTileEntity, Float2FloatFunction> getLid(final IChestLid p_226917_0_) {
        return new TileEntityMerger.ICallback<GenericRepairChestTileEntity, Float2FloatFunction>() {
            @Override
            public Float2FloatFunction func_225539_a_(GenericRepairChestTileEntity p_225539_1_, GenericRepairChestTileEntity p_225539_2_) {
                return (p_226921_2_) -> {
                    return Math.max(p_225539_1_.getLidAngle(p_226921_2_), p_225539_2_.getLidAngle(p_226921_2_));
                };
            }

            @Override
            public Float2FloatFunction func_225538_a_(GenericRepairChestTileEntity p_225538_1_) {
                return p_225538_1_::getLidAngle;
            }

            @Override
            public Float2FloatFunction func_225537_b_() {
                return p_226917_0_::getLidAngle;
            }
        };
    }

    public TileEntityMerger.ICallbackWrapper<? extends GenericRepairChestTileEntity> getWrapper(BlockState blockState, World world, BlockPos blockPos, boolean p_225536_4_) {
        BiPredicate<IWorld, BlockPos> biPredicate;
        if (p_225536_4_) {
            biPredicate = (p_226918_0_, p_226918_1_) -> false;
        }
        else {
            biPredicate = GenericRepairChest::isBlocked;
        }

        return TileEntityMerger.func_226924_a_(this.tileEntityTypeSupplier.get(), GenericRepairChest::getMergerType, GenericRepairChest::getDirectionToAttached, FACING, blockState, world, blockPos, biPredicate);
    }

    public static TileEntityMerger.Type getMergerType(BlockState blockState) {
        return TileEntityMerger.Type.SINGLE;
    }

    public static Direction getDirectionToAttached(BlockState state) {
        Direction direction = state.get(FACING);
        return direction.rotateYCCW();
    }
}
