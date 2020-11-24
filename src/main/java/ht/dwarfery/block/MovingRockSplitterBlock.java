package ht.dwarfery.block;

import ht.dwarfery.tileentity.RockSplitterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

// TODO: why extend ContainerBlock?
public class MovingRockSplitterBlock extends ContainerBlock {
    public static final DirectionProperty FACING = RockSplitterBlock.FACING;

    public MovingRockSplitterBlock(Properties builder) {
        super(builder);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return null;
    }

    public static TileEntity createTileEntity(BlockState blockState, Direction facing, boolean extending) {
        return new RockSplitterTileEntity(blockState, facing, extending);
    }

    public void onReplaced(BlockState blockState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!blockState.isIn(newState.getBlock())) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof RockSplitterTileEntity) {
                ((RockSplitterTileEntity) tileEntity).clearTileEntity();
            }
        }
    }

    /**
     * Called after a player destroys this Block - the posiiton pos may no longer hold the state indicated.
     */
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        BlockPos blockpos = pos.offset(state.get(FACING).getOpposite());
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (blockstate.getBlock() instanceof RockSplitterBlock && blockstate.get(RockSplitterBlock.EXTENDED)) {
            worldIn.removeBlock(blockpos, false);
        }
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            worldIn.removeBlock(pos, false);
            return ActionResultType.CONSUME;
        } else {
            return ActionResultType.PASS;
        }
    }

    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        RockSplitterTileEntity tileEntity = this.getTileEntity(builder.getWorld(), builder.assertPresent(LootParameters.POSITION));
        return tileEntity == null ? Collections.emptyList() : tileEntity.getChiselState().getDrops(builder);
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        RockSplitterTileEntity tileEntity = this.getTileEntity(worldIn, pos);
        return tileEntity != null ? tileEntity.getCollisionShape(worldIn, pos) : VoxelShapes.empty();
    }

    @Nullable
    private RockSplitterTileEntity getTileEntity(IBlockReader world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof RockSplitterTileEntity ? (RockSplitterTileEntity) tileEntity : null;
    }

    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
}
