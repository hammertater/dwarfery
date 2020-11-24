package ht.dwarfery.block;

import ht.dwarfery.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class RockSplitterBlock extends DirectionalBlock {

    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;

    protected static final VoxelShape SHAPE_EAST = VoxelShapes.or(
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D),
            Block.makeCuboidShape(12.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D)
    );
    protected static final VoxelShape SHAPE_WEST = VoxelShapes.or(
            Block.makeCuboidShape(4.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 6.0D, 6.0D, 4.0D, 10.0D, 10.0D)
    );
    protected static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 12.0D),
            Block.makeCuboidShape(6.0D, 6.0D, 12.0D, 10.0D, 10.0D, 16.0D)
    );
    protected static final VoxelShape SHAPE_NORTH = VoxelShapes.or(
            Block.makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 16.0D),
            Block.makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 4.0D)
    );
    protected static final VoxelShape SHAPE_UP = VoxelShapes.or(
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.makeCuboidShape(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D)
    );
    protected static final VoxelShape SHAPE_DOWN = VoxelShapes.or(
            Block.makeCuboidShape(0.0D, 4.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D)
    );

    private static final int EXTEND_EVENT_ID = 0;
    private static final int RETRACT_EVENT_ID = 1;

    public RockSplitterBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(EXTENDED, Boolean.FALSE));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXTENDED);
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case DOWN:
                return SHAPE_DOWN;
            case UP:
            default:
                return SHAPE_UP;
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case EAST:
                return SHAPE_EAST;
        }
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!worldIn.isRemote) {
            this.maybeMove(worldIn, pos, state);
        }

    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            this.maybeMove(worldIn, pos, state);
        }

    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.isIn(state.getBlock())) {
            if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
                this.maybeMove(worldIn, pos, state);
            }

        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite()).with(EXTENDED, Boolean.FALSE);
    }

    private void maybeMove(World world, BlockPos pos, BlockState blockState) {
        Direction facing = blockState.get(FACING);
        boolean flag = shouldBeExtended(world, pos, facing);
        if (flag && !blockState.get(EXTENDED)) {
            if (canExtend(world, pos, blockState)) {
                world.addBlockEvent(pos, this, EXTEND_EVENT_ID, facing.getIndex());
            }
        } else if (!flag && blockState.get(EXTENDED)) {
            world.addBlockEvent(pos, this, RETRACT_EVENT_ID, facing.getIndex());
        }
    }

    private boolean canExtend(World world, BlockPos pos, BlockState blockState) {
        if (blockState.isIn(ModBlocks.ROCK_SPLITTER.get())) {
            BlockState facingState = world.getBlockState(pos.offset(blockState.get(RockSplitterBlock.FACING)));
            return facingState.getBlock() != ModBlocks.MOVING_ROCK_SPLITTER.get() && facingState.getBlockHardness(world, pos) >= 0;
        }
        else {
            return false;
        }
    }

    // Copied from PistonBlock
    private boolean shouldBeExtended(World worldIn, BlockPos pos, Direction facing) {
        for(Direction direction : Direction.values()) {
            if (direction != facing && worldIn.isSidePowered(pos.offset(direction), direction)) {
                return true;
            }
        }

        if (worldIn.isSidePowered(pos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockpos = pos.up();

            for(Direction direction1 : Direction.values()) {
                if (direction1 != Direction.DOWN && worldIn.isSidePowered(blockpos.offset(direction1), direction1)) {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean doMove(World world, BlockPos pos, Direction facing, boolean extending) {
        BlockPos forwardPos = pos.offset(facing);
        if (extending) {
            boolean success = crackOrDestroy(world, forwardPos);
            if (success) {
                BlockState newChiselState = ModBlocks.ROCK_SPLITTER_CHISEL.get().getDefaultState().with(RockSplitterChiselBlock.FACING, facing);
                BlockState newMovingState = ModBlocks.MOVING_ROCK_SPLITTER.get().getDefaultState().with(MovingRockSplitterBlock.FACING, facing);
                world.setBlockState(forwardPos, newMovingState, 68);
                world.setTileEntity(forwardPos, MovingRockSplitterBlock.createTileEntity(newChiselState, facing, true));
                world.notifyNeighborsOfStateChange(forwardPos, ModBlocks.ROCK_SPLITTER_CHISEL.get());
            }
        } else {
            if (world.getBlockState(forwardPos).isIn(ModBlocks.ROCK_SPLITTER_CHISEL.get())) {
                world.setBlockState(forwardPos, Blocks.AIR.getDefaultState(), 20);
                return true;
            }
            else {
                return false;
            }
        }

        return false;
    }

    private boolean crackOrDestroy(World world, BlockPos forwardPos) {
        // TODO: crack geodes and stuff; ICrackable
        world.destroyBlock(forwardPos, true);
        return true;
    }

    /**
     * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On
     * the Server, this may perform additional changes to the world, like pistons replacing the block with an extended
     * base. On the client, the update may involve replacing tile entities or effects such as sounds or particles
     */
    public boolean eventReceived(BlockState blockState, World world, BlockPos pos, int id, int param) {
        Direction facing = blockState.get(FACING);
        if (!world.isRemote()) {
            boolean flag = this.shouldBeExtended(world, pos, facing);
            if (!flag && id == EXTEND_EVENT_ID) {
                return false;
            }

            if (flag && id == RETRACT_EVENT_ID) {
                world.setBlockState(pos, blockState.with(EXTENDED, Boolean.TRUE), 2);
                return false;
            }
        }

        if (id == EXTEND_EVENT_ID) {
            if (!canExtend(world, pos, blockState)) {
                return false;
            }

            world.setBlockState(pos, blockState.with(EXTENDED, Boolean.TRUE), 67);
            doMove(world, pos, facing, true); // Must be called after EXTENDED is set
            world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);

        } else if (id == RETRACT_EVENT_ID) {
            BlockState newMovingState = ModBlocks.MOVING_ROCK_SPLITTER.get().getDefaultState().with(MovingRockSplitterBlock.FACING, facing);
            world.setBlockState(pos, newMovingState, 20);
            world.setTileEntity(pos, MovingRockSplitterBlock.createTileEntity(this.getDefaultState().with(FACING, Direction.byIndex(param & 7)), facing, false));
            world.func_230547_a_(pos, newMovingState.getBlock());
            newMovingState.func_235734_a_(world, pos, 2);

            world.removeBlock(pos.offset(facing), false);
            world.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);
        }

        return true;
    }
}
