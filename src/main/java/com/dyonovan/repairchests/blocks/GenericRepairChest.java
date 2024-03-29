package com.dyonovan.repairchests.blocks;

import com.dyonovan.repairchests.blockentities.GenericChestBlockEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public abstract class GenericRepairChest extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape CHEST_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    private final RepairChestTypes type;
    private final Supplier<BlockEntityType<? extends GenericChestBlockEntity>> blockEntityTypeSupplier;

    private static final DoubleBlockCombiner.Combiner<GenericChestBlockEntity, Optional<Container>> CHEST_COMBINER = new DoubleBlockCombiner.Combiner<GenericChestBlockEntity, Optional<Container>>() {
        @Override
        public Optional<Container> acceptDouble(GenericChestBlockEntity blockEntity1, GenericChestBlockEntity blockEntity2) {
            return Optional.of(new CompoundContainer(blockEntity1, blockEntity2));
        }

        @Override
        public Optional<Container> acceptSingle(GenericChestBlockEntity blockEntity) {
            return Optional.of(blockEntity);
        }

        @Override
        public Optional<Container> acceptNone() {
            return Optional.empty();
        }
    };

    private static final DoubleBlockCombiner.Combiner<GenericChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<>() {
        @Override
        public Optional<MenuProvider> acceptDouble(GenericChestBlockEntity blockEntity1, GenericChestBlockEntity blockEntity2) {
            return Optional.empty();
        }

        @Override
        public Optional<MenuProvider> acceptSingle(GenericChestBlockEntity blockEntity) {
            return Optional.of(blockEntity);
        }

        @Override
        public Optional<MenuProvider> acceptNone() {
            return Optional.empty();
        }
    };

    public GenericRepairChest(BlockBehaviour.Properties properties, Supplier<BlockEntityType<? extends GenericChestBlockEntity>> blockEntityTypeSupplier, RepairChestTypes chestType) {
        super(properties);

        this.type = chestType;
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE));
    }

    public static DoubleBlockCombiner.BlockType getBlockType(BlockState blockState) {
        return DoubleBlockCombiner.BlockType.SINGLE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CHEST_SHAPE;
    }

    public static Direction getConnectedDirection(BlockState blockState) {
        return blockState.getValue(FACING).getCounterClockWise();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        FluidState iFluidState = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(FACING, direction).setValue(WATERLOGGED, iFluidState.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockEntity tileEntity = level.getBlockEntity(pos);

        if (tileEntity instanceof GenericChestBlockEntity) {
            ((GenericChestBlockEntity) tileEntity).wasPlaced(placer, stack);
        }

        if (stack.hasCustomHoverName()) {
            ((GenericChestBlockEntity) tileEntity).setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof GenericChestBlockEntity) {
                ((GenericChestBlockEntity) tileEntity).removeAdornments();

                Containers.dropContents(level, pos, (GenericChestBlockEntity) tileEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider menuProvider = this.getMenuProvider(state, level, pos);
            if (menuProvider != null) {
                player.openMenu(menuProvider);
                player.awardStat(this.getOpenChestStat());
            }
        }
        return InteractionResult.CONSUME;
    }

    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    public BlockEntityType<? extends GenericChestBlockEntity> blockEntityType() {
        return this.blockEntityTypeSupplier.get();
    }

    @Nullable
    public static Container getContainer(GenericRepairChest chestBlock, BlockState state, Level worldIn, BlockPos pos, boolean ignore) {
        return chestBlock.combine(state, worldIn, pos, ignore).<Optional<Container>>apply(CHEST_COMBINER).orElse((Container) null);
    }

    public DoubleBlockCombiner.NeighborCombineResult<? extends GenericChestBlockEntity> combine(BlockState blockState, Level level, BlockPos blockPos, boolean ignoreBlockedChest) {
        BiPredicate<LevelAccessor, BlockPos> biPredicate;

        if (ignoreBlockedChest) {
            biPredicate = (levelAccessor, blockPos1) -> {
                return false;
            };
        } else {
            biPredicate = GenericRepairChest::isChestBlockedAt;
        }
        return DoubleBlockCombiner.combineWithNeigbour(this.blockEntityTypeSupplier.get(), GenericRepairChest::getBlockType, GenericRepairChest::getConnectedDirection, FACING, blockState, level, blockPos, biPredicate);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        return this.combine(blockState, level, blockPos, false).<Optional<MenuProvider>>apply(MENU_PROVIDER_COMBINER).orElse(null);
    }

    public static DoubleBlockCombiner.Combiner<GenericChestBlockEntity, Float2FloatFunction> opennessCombiner(final LidBlockEntity lidBlockEntity) {
        return new DoubleBlockCombiner.Combiner<>() {
            @Override
            public Float2FloatFunction acceptDouble(GenericChestBlockEntity blockEntity1, GenericChestBlockEntity blockEntity2) {
                return (lidBlockEntity) -> Math.max(blockEntity1.getOpenNess(lidBlockEntity), blockEntity2.getOpenNess(lidBlockEntity));
            }

            @Override
            public Float2FloatFunction acceptSingle(GenericChestBlockEntity blockEntity) {
                return blockEntity::getOpenNess;
            }

            @Override
            public Float2FloatFunction acceptNone() {
                return lidBlockEntity::getOpenNess;
            }
        };
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, this.blockEntityType(), GenericChestBlockEntity::tick);
    }

    private static boolean isChestBlockedAt(LevelAccessor iWorld, BlockPos blockPos) {
        return isBlockedChestByBlock(iWorld, blockPos) || isCatSittingOn(iWorld, blockPos);
    }

    private static boolean isBlockedChestByBlock(BlockGetter iBlockReader, BlockPos worldIn) {
        BlockPos blockpos = worldIn.above();
        return iBlockReader.getBlockState(blockpos).isRedstoneConductor(iBlockReader, blockpos);
    }

    private static boolean isCatSittingOn(LevelAccessor iWorld, BlockPos blockPos) {
        List<Cat> list = iWorld.getEntitiesOfClass(Cat.class, new AABB(blockPos.getX(),
                blockPos.getY() + 1, blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 2, blockPos.getZ() + 1));
        if (!list.isEmpty()) {
            for (Cat catEntity : list) {
                if (catEntity.isInSittingPose()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, blockState, level, blockPos, false));
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockBlockStateBuilder) {
        blockBlockStateBuilder.add(FACING, WATERLOGGED);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);

        if (blockEntity instanceof GenericChestBlockEntity) {
            ((GenericChestBlockEntity) blockEntity).recheckOpen();
        }
    }

    public static RepairChestTypes getTypeFromBlock(Block block) {
        return block instanceof GenericRepairChest ? ((GenericRepairChest) block).getType() : null;
    }

    public RepairChestTypes getType() {
        return this.type;
    }
}
