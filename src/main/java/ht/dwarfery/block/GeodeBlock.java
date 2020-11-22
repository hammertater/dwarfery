package ht.dwarfery.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class GeodeBlock extends Block {

    public GeodeBlock(Properties properties) {
        super(properties);
        this.setDefaultState(
                this.stateContainer.getBaseState()
        );
    }

    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        final VoxelShape shape = VoxelShapes.or(
                makeCuboidShape(2, 0, 2, 14, 11, 14),
                makeCuboidShape(1, 1, 1, 15, 13, 15),
                makeCuboidShape(3, 15, 3, 13, 16, 13),
                makeCuboidShape(2, 13, 2, 14, 15, 14)
        );
        return shape;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add();
    }
}
