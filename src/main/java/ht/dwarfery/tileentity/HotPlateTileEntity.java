package ht.dwarfery.tileentity;

import ht.dwarfery.block.HotPlateBlock;
import ht.dwarfery.init.ModTileEntities;
import ht.dwarfery.inventory.container.HotPlateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

import static net.minecraft.tileentity.AbstractFurnaceTileEntity.isFuel;
import static net.minecraftforge.common.ForgeHooks.getBurnTime;

public class HotPlateTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {

    public static final int INVENTORY_SIZE = 1;
    public static final int DATA_SIZE = 1;

    protected NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private static final int[] SLOTS = new int[]{0};
    private int burnTime;

    protected final IIntArray hotPlateData = new IIntArray() {
        @Override
        public int get(int index) {
            return HotPlateTileEntity.this.burnTime;
        }

        @Override
        public void set(int index, int value) {
            HotPlateTileEntity.this.burnTime = value;
        }

        @Override
        public int size() {
            return 1;
        }
    };

    public HotPlateTileEntity() {
        super(ModTileEntities.HOT_PLATE.get());
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
        ItemStack currentStack = this.items.get(0);
        return isFuel(stack) || stack.getItem() == Items.BUCKET && currentStack.getItem() != Items.BUCKET;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return !this.items.get(0).isEmpty();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.hot_plate");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new HotPlateContainer(id, player, this, this.hotPlateData);
    }

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (burnTime == 0) {
            startBurning(stack);
        }
    }

    private void startBurning(ItemStack fuel) {
        this.burnTime = getBurnTime(fuel);
        if (this.burnTime > 0) {
            fuel.shrink(1);
            if (fuel.isEmpty()) {
                this.items.set(1, fuel.getContainerItem());
            }

            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(HotPlateBlock.LIT, true));
            this.markDirty();
        }
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
        this.items.clear();
    }

    @Override
    public void tick() {
        if (this.burnTime > 0) {
            this.burnTime = this.burnTime - 1;

            if (this.burnTime == 0) {
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(HotPlateBlock.LIT, false));
            }
        }
    }
}
