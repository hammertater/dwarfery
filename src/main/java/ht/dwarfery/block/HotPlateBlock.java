package ht.dwarfery.block;

import ht.dwarfery.tileentity.HotPlateTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class HotPlateBlock extends ContainerBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public HotPlateBlock(Properties properties) {
        super(properties
                .setLightLevel(HotPlateBlock::getLightLevel)
        );
        this.setDefaultState(
                this.stateContainer.getBaseState()
                        .with(FACING, Direction.NORTH)
                        .with(LIT, false)
        );
    }

    private static int getLightLevel(BlockState blockState) {
        return blockState.get(BlockStateProperties.LIT) ? 13 : 0;
    }

    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        final VoxelShape shape = VoxelShapes.or(
                makeCuboidShape(0, 0, 0, 16, 15, 16),
                makeCuboidShape(1, 15, 1, 15, 16, 15)
        );
        return shape;
    }

    // Copied from MagmaBlock
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn.getBlockState(pos).get(LIT) && !entityIn.isImmuneToFire() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn)) {
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }

    // Copied from AbstractFurnaceBlock
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + 0.375D;
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = stateIn.get(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * 0.52D : d4;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.52D : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new HotPlateTileEntity();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public ActionResultType onBlockActivated(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) { // Don't do anything on client side
            return ActionResultType.SUCCESS;
        } else {
            this.interactWith(blockState, worldIn, pos, player);
            return ActionResultType.CONSUME;
        }
    }

    protected void interactWith(BlockState blockState, World world, BlockPos pos, PlayerEntity player) {
        INamedContainerProvider namedContainerProvider = getContainer(blockState, world, pos);
        if (namedContainerProvider != null) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer -> {}));
        }
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof HotPlateTileEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (HotPlateTileEntity)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }
}
