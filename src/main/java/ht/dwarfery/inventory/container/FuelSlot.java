package ht.dwarfery.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import static net.minecraftforge.common.ForgeHooks.getBurnTime;

public class FuelSlot extends Slot {
    public FuelSlot(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    public boolean isItemValid(ItemStack stack) {
        return isFuel(stack) || isBucket(stack);
    }

    public int getItemStackLimit(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }

    private static boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }

    private static boolean isFuel(ItemStack stack) {
        return getBurnTime(stack) > 0;
    }
}
