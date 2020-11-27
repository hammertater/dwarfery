package ht.dwarfery.block;

import ht.dwarfery.util.ShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import java.util.Collection;
import java.util.List;

public class OpenedGeodeBlock extends DirectionalBlock {

    protected final VoxelShape SHAPE_EAST;
    protected final VoxelShape SHAPE_WEST;
    protected final VoxelShape SHAPE_SOUTH;
    protected final VoxelShape SHAPE_NORTH;
    protected final VoxelShape SHAPE_UP;
    protected final VoxelShape SHAPE_DOWN;

    public OpenedGeodeBlock(Properties properties, VoxelShape eastShape) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));

        this.SHAPE_EAST = eastShape;
        this.SHAPE_WEST = ShapeUtil.rotateShape(eastShape, Direction.WEST);
        this.SHAPE_SOUTH = ShapeUtil.rotateShape(eastShape, Direction.SOUTH);
        this.SHAPE_NORTH = ShapeUtil.rotateShape(eastShape, Direction.NORTH);
        this.SHAPE_UP = ShapeUtil.rotateShape(eastShape, Direction.UP);
        this.SHAPE_DOWN = ShapeUtil.rotateShape(eastShape, Direction.DOWN);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @SuppressWarnings("deprecation")
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

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return super.getDrops(state, builder);
    }
}
