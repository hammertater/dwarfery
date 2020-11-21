package ht.dwarfery.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class GeodeBlock extends Block {

//    public static final IntegerProperty CHOPS = BlockStateProperties.CHOP_COUNT;

    public GeodeBlock(Properties properties) {
        super(properties);
        this.setDefaultState(
                this.stateContainer.getBaseState()
        );
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return makeCuboidShape(0, 0, 0, 16, 16, 16);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add();
    }
}
