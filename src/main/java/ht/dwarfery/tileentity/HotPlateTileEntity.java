package ht.dwarfery.tileentity;

import ht.dwarfery.DwarferyMod;
import ht.dwarfery.block.HotPlateBlock;
import ht.dwarfery.init.ModTileEntities;
import ht.dwarfery.inventory.container.ContainerStacks;
import ht.dwarfery.inventory.container.HotPlateContainer;
import ht.dwarfery.inventory.container.HotPlateData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraftforge.common.ForgeHooks.getBurnTime;

public class HotPlateTileEntity extends LockableTileEntity implements INamedContainerProvider, ISidedInventory, ITickableTileEntity {

    public static final int NUM_FUEL_SLOTS = 1;

    private ContainerStacks fuelStacks;

    private final HotPlateData data = new HotPlateData();

    private final String NBT_FUEL_STACKS = "FuelStacks";

    public HotPlateTileEntity() {
        super(ModTileEntities.HOT_PLATE.get());
        fuelStacks = ContainerStacks.createForTileEntity(NUM_FUEL_SLOTS, this::isUsableByPlayer, this::markDirty);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
        return fuelStacks.isItemValidForSlot(index, stack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return !fuelStacks.isEmpty();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.hot_plate");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new HotPlateContainer(id, player, fuelStacks, data);
    }

    @Override
    public int getSizeInventory() {
        return fuelStacks.getSizeInventory();
    }

    @Override
    public boolean isEmpty() {
        return fuelStacks.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return fuelStacks.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return fuelStacks.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return fuelStacks.getAndRemove(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        fuelStacks.setInventorySlotContents(index, stack);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        fuelStacks.clear();
    }

    @Override
    public void tick() {
        if (data.burnTime > 0) {
            data.burnTime = data.burnTime - 1;
        }

        if (!this.world.isRemote) {
            if (data.burnTime == 0) {
                tryToBurn();
            }

            boolean isLit = world.getBlockState(pos).get(HotPlateBlock.LIT);
            boolean shouldBeLit = (data.burnTime > 0);
            if (isLit != shouldBeLit) {
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(HotPlateBlock.LIT, shouldBeLit));
                this.markDirty();
            }
        }
    }

    private void tryToBurn() {
        ItemStack fuel = fuelStacks.getStackInSlot(0);
        data.burnTime = getBurnTime(fuel);
        if (data.burnTime > 0) {
            data.initialBurnTime = data.burnTime;

            fuel.shrink(1);
            if (fuel.isEmpty()) {
                fuelStacks.setInventorySlotContents(0, fuel.getContainerItem());
            }
            this.markDirty();
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        data.putIntoNBT(nbt);
        nbt.put(NBT_FUEL_STACKS, fuelStacks.serializeNBT());
        return nbt;
    }

    // read
    @Override
    public void func_230337_a_(BlockState blockState, CompoundNBT nbt) {
        super.func_230337_a_(blockState, nbt);

        data.readFromNBT(nbt);

        CompoundNBT fuelStacksNBT = nbt.getCompound(NBT_FUEL_STACKS);
        fuelStacks.deserializeNBT(fuelStacksNBT);

        if (fuelStacks.getSizeInventory() != NUM_FUEL_SLOTS) {
            throw new IllegalArgumentException("Corrupted NBT: number of inventory slots did not match expected");
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT updateTagDescribingTileEntityState = getUpdateTag();
        return new SUpdateTileEntityPacket(pos, 0, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT updateTagDescribingTileEntityState = pkt.getNbtCompound();
        BlockState blockState = world.getBlockState(pos);
        handleUpdateTag(blockState, updateTagDescribingTileEntityState);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT tag) {
        func_230337_a_(blockState, tag);
    }

    public void dropAllContents(World world, BlockPos blockPos) {
        InventoryHelper.dropInventoryItems(world, blockPos, fuelStacks);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.dwarfery.hot_plate");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return HotPlateContainer.createContainerServerSide(id, playerInventory, fuelStacks, data);
    }
}
