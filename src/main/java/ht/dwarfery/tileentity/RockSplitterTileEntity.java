package ht.dwarfery.tileentity;

import ht.dwarfery.block.RockSplitterBlock;
import ht.dwarfery.block.RockSplitterChiselBlock;
import ht.dwarfery.init.ModBlocks;
import ht.dwarfery.init.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

public class RockSplitterTileEntity extends TileEntity implements ITickableTileEntity {
    private BlockState chiselState;
    private Direction facing;
    /** if this rock splitter is extending or not */
    private boolean extending;
    private boolean shouldChiselBeRendered;
    private static final ThreadLocal<Direction> MOVING_ENTITY = ThreadLocal.withInitial(() -> null);
    private float progress;
    /** the progress in (de)extending */
    private float lastProgress;

    public RockSplitterTileEntity() {
        super(ModTileEntities.ROCK_SPLITTER.get());
    }

    public RockSplitterTileEntity(BlockState chiselState, Direction facing, boolean extending) {
        this();
        this.chiselState = chiselState;
        this.facing = facing;
        this.extending = extending;
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {handleUpdateTag}
     */
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    /**
     * Returns true if the rock splitter is extending
     */
    public boolean isExtending() {
        return extending;
    }

    public BlockState getChiselState() {
        return chiselState;
    }

    /**
     * Get interpolated progress value (between lastProgress and progress) given the fractional time between ticks as an
     * argument
     */
    public float getProgress(float ticks) {
        if (ticks > 1.0F) {
            ticks = 1.0F;
        }

        return MathHelper.lerp(ticks, lastProgress, progress);
    }

    private float getExtendedProgress(float f) {
        return this.extending ? f - 1.0F : 1.0F - f;
    }

    public float getOffsetX(float ticks) {
        return (float) facing.getXOffset() * getExtendedProgress(getProgress(ticks));
    }

    public float getOffsetY(float ticks) {
        return (float) facing.getYOffset() * getExtendedProgress(getProgress(ticks));
    }

    public float getOffsetZ(float ticks) {
        return (float) facing.getZOffset() * getExtendedProgress(getProgress(ticks));
    }

    public boolean shouldChiselBeRendered() {
        return true;
    }

    private BlockState getCollisionRelatedBlockState() {
        return !isExtending() && shouldChiselBeRendered() && chiselState.getBlock() instanceof RockSplitterBlock ? ModBlocks.ROCK_SPLITTER_CHISEL.get().getDefaultState().with(RockSplitterChiselBlock.FACING, chiselState.get(RockSplitterBlock.FACING)) : chiselState;
    }

    public void clearTileEntity() {
        if (lastProgress < 1.0F && world != null) {
            progress = 1.0F;
            lastProgress = progress;
            world.removeTileEntity(pos);
            remove();
            if (world.getBlockState(pos).isIn(ModBlocks.MOVING_ROCK_SPLITTER.get())) {
                BlockState blockstate;
                if (shouldChiselBeRendered) {
                    blockstate = Blocks.AIR.getDefaultState();
                } else {
                    blockstate = Block.getValidBlockForPosition(chiselState, world, pos);
                }

                world.setBlockState(pos, blockstate, 3);
                world.neighborChanged(pos, blockstate.getBlock(), pos);
            }
        }
    }

    private static void func_227022_a_(Direction direction1, Entity entity, double d, Direction direction2) {
        MOVING_ENTITY.set(direction1);
        entity.move(MoverType.PISTON, new Vector3d(d * (double)direction2.getXOffset(), d * (double)direction2.getYOffset(), d * (double)direction2.getZOffset()));
        MOVING_ENTITY.set(null);
    }

    private void func_227024_g_(float f) {
        if (isStuck()) {
            Direction direction = getMotionDirection();
            if (direction.getAxis().isHorizontal()) {
                double d0 = chiselState.getCollisionShape(world, pos).getEnd(Direction.Axis.Y);
                AxisAlignedBB axisAlignedBB = moveByPositionAndProgress(new AxisAlignedBB(0.0D, d0, 0.0D, 1.0D, 1.5000000999999998D, 1.0D));
                double d1 = f - progress;

                for(Entity entity : world.getEntitiesInAABBexcluding(null, axisAlignedBB, (entity) -> func_227021_a_(axisAlignedBB, entity))) {
                    func_227022_a_(direction, entity, d1, direction);
                }

            }
        }
    }

    private static boolean func_227021_a_(AxisAlignedBB axisAlignedBB, Entity entity) {
        return entity.getPushReaction() == PushReaction.NORMAL && entity.isOnGround() && entity.getPosX() >= axisAlignedBB.minX && entity.getPosX() <= axisAlignedBB.maxX && entity.getPosZ() >= axisAlignedBB.minZ && entity.getPosZ() <= axisAlignedBB.maxZ;
    }

    private boolean isStuck() {
        return chiselState.isIn(Blocks.HONEY_BLOCK);
    }

    public Direction getMotionDirection() {
        return this.extending ? facing : facing.getOpposite();
    }

    private static double getMovement(AxisAlignedBB bbox1, Direction direction, AxisAlignedBB bbox2) {
        switch(direction) {
            case EAST:
                return bbox1.maxX - bbox2.minX;
            case WEST:
                return bbox2.maxX - bbox1.minX;
            case UP:
            default:
                return bbox1.maxY - bbox2.minY;
            case DOWN:
                return bbox2.maxY - bbox1.minY;
            case SOUTH:
                return bbox1.maxZ - bbox2.minZ;
            case NORTH:
                return bbox2.maxZ - bbox1.minZ;
        }
    }

    private AxisAlignedBB moveByPositionAndProgress(AxisAlignedBB axisAlignedBB) {
        double d0 = getExtendedProgress(progress);
        return axisAlignedBB.offset((double) pos.getX() + d0 * (double) facing.getXOffset(), (double) pos.getY() + d0 * (double) facing.getYOffset(), (double) pos.getZ() + d0 * (double) facing.getZOffset());
    }

    @Override
    public void tick() {
        lastProgress = progress;
        if (lastProgress >= 1.0F) {
            world.removeTileEntity(pos);
            remove();
            if (chiselState != null && world.getBlockState(pos).isIn(ModBlocks.MOVING_ROCK_SPLITTER.get())) {
                BlockState blockState = Block.getValidBlockForPosition(chiselState, world, pos);
                if (blockState.isIn(Blocks.AIR)) {
                    world.setBlockState(pos, chiselState, 84);
                    Block.replaceBlock(chiselState, blockState, world, pos, 3);
                } else {
                    if (blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.get(BlockStateProperties.WATERLOGGED)) {
                        blockState = blockState.with(BlockStateProperties.WATERLOGGED, Boolean.FALSE);
                    }

                    world.setBlockState(pos, blockState, 67);
                    world.neighborChanged(pos, blockState.getBlock(), pos);
                }
            }

        } else {
            float f = progress + 0.5F;
//            this.moveCollidedEntities(f);
            func_227024_g_(f);
            progress = f;
            if (progress >= 1.0F) {
                progress = 1.0F;
            }

        }
    }

    @Override
    public void read(BlockState blockState, CompoundNBT nbt) {
        super.read(blockState, nbt);
        chiselState = NBTUtil.readBlockState(nbt.getCompound("blockState"));
        facing = Direction.byIndex(nbt.getInt("facing"));
        progress = nbt.getFloat("progress");
        lastProgress = progress;
        extending = nbt.getBoolean("extending");
        shouldChiselBeRendered = nbt.getBoolean("source");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("blockState", NBTUtil.writeBlockState(chiselState));
        compound.putInt("facing", facing.getIndex());
        compound.putFloat("progress", lastProgress);
        compound.putBoolean("extending", extending);
        compound.putBoolean("source", shouldChiselBeRendered);
        return compound;
    }

    public VoxelShape getCollisionShape(IBlockReader world, BlockPos pos) {
        VoxelShape voxelShape;
        if (!extending && shouldChiselBeRendered) {
            voxelShape = chiselState.with(RockSplitterBlock.EXTENDED, Boolean.TRUE).getCollisionShape(world, pos);
        } else {
            voxelShape = VoxelShapes.empty();
        }

        Direction direction = MOVING_ENTITY.get();
        if ((double) progress < 1.0D && direction == getMotionDirection()) {
            return voxelShape;
        } else {
            BlockState blockState;
            if (shouldChiselBeRendered()) {
                blockState = ModBlocks.ROCK_SPLITTER_CHISEL.get().getDefaultState().with(RockSplitterChiselBlock.FACING, facing);
            } else {
                blockState = chiselState;
            }

            float f = getExtendedProgress(progress);
            double d0 = (float) facing.getXOffset() * f;
            double d1 = (float) facing.getYOffset() * f;
            double d2 = (float) facing.getZOffset() * f;
            return VoxelShapes.or(voxelShape, blockState.getCollisionShape(world, pos).withOffset(d0, d1, d2));
        }
    }

}
