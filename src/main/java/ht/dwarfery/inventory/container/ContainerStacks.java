package ht.dwarfery.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

public class ContainerStacks implements IInventory {

    private Predicate<PlayerEntity> canPlayerAccessInventory = x -> true;
    private Runnable markDirtyNotification = () -> {};
    private Runnable openInventoryNotification = () -> {};
    private Runnable closeInventoryNotification = () -> {};

    private final ItemStackHandler contents;

    private ContainerStacks(int size) {
        this.contents = new ItemStackHandler(size);
    }

    private ContainerStacks(int size, Predicate<PlayerEntity> canPlayerAccessInventory, Runnable markDirtyNotification) {
        this.contents = new ItemStackHandler(size);
        this.canPlayerAccessInventory = canPlayerAccessInventory;
        this.markDirtyNotification = markDirtyNotification;
    }

    public static ContainerStacks createForTileEntity(int size, Predicate<PlayerEntity> canPlayerAccessInventory, Runnable markDirtyNoficiation) {
        return new ContainerStacks(size, canPlayerAccessInventory, markDirtyNoficiation);
    }

    public static ContainerStacks createForClientSideContainer(int size) {
        return new ContainerStacks(size);
    }

    public CompoundNBT serializeNBT() {
        return contents.serializeNBT();
    }

    public void deserializeNBT(CompoundNBT nbt) {
        contents.deserializeNBT(nbt);
    }

    public void setCanPlayerAccessInventory(Predicate<PlayerEntity> canPlayerAccessInventory) {
        this.canPlayerAccessInventory = canPlayerAccessInventory;
    }

    public void setMarkDirtyNotification(Runnable markDirtyNotification) {
        this.markDirtyNotification = markDirtyNotification;
    }

    public void setOpenInventoryNotification(Runnable openInventoryNotification) {
        this.openInventoryNotification = openInventoryNotification;
    }

    public void setCloseInventoryNotification(Runnable closeInventoryNotification) {
        this.closeInventoryNotification = closeInventoryNotification;
    }

    @Override
    public int getSizeInventory() {
        return contents.getSlots();
    }

    @Override
    public void openInventory(PlayerEntity player) {
        openInventoryNotification.run();
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        closeInventoryNotification.run();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < contents.getSlots(); ++i) {
            if (!contents.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return contents.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return contents.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        int maxPossibleItemStackSize = contents.getSlotLimit(index);
        return contents.extractItem(index, maxPossibleItemStackSize, false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        contents.setStackInSlot(index, stack);
    }

    @Override
    public void clear() {
        for (int i = 0; i < contents.getSlots(); ++i) {
            contents.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void markDirty() {
        markDirtyNotification.run();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return canPlayerAccessInventory.test(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return indexIsValid(index) && contents.isItemValid(index, stack);
    }

    public ItemStack getAndRemove(int index) {
        ItemStack itemStack = contents.getStackInSlot(index);
        contents.setStackInSlot(index, ItemStack.EMPTY);
        return itemStack;
    }

    private boolean indexIsValid(int index) {
        return index >= 0 && index < contents.getSlots();
    }
}
