package ht.dwarfery.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class RockSplitterChiselBlock extends DirectionalBlock {

    protected static final VoxelShape SHAPE_UP = Block.makeCuboidShape(6.0D, -4.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    protected static final VoxelShape SHAPE_DOWN = Block.makeCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 20.0D, 10.0D);
    protected static final VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(6.0D, 6.0D, -4.0D, 10.0D, 10.0D, 10.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.makeCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 20.0D);
    protected static final VoxelShape SHAPE_EAST = Block.makeCuboidShape(-4.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    protected static final VoxelShape SHAPE_WEST = Block.makeCuboidShape(6.0D, 6.0D, 6.0D, 20.0D, 10.0D, 10.0D);

    public RockSplitterChiselBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
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
}
