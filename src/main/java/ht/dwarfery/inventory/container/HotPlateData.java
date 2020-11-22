package ht.dwarfery.inventory.container;

import ht.dwarfery.tileentity.HotPlateTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;

public class HotPlateData implements IIntArray {

    public static final int INVENTORY_SIZE = HotPlateTileEntity.NUM_FUEL_SLOTS;

    public int burnTime;
    public int initialBurnTime;

    private String NBT_BURN_TIME = "BurnTime";
    private String NBT_INITIAL_BURN_TIME = "InitialBurnTime";

    private final int BURN_TIME_INDEX = 0;
    private final int INITIAL_BURN_TIME_INDEX = BURN_TIME_INDEX + 1;
    private final int NUM_INDEXES = INITIAL_BURN_TIME_INDEX + 1;

    public void putIntoNBT(CompoundNBT nbt) {
        nbt.putInt(NBT_BURN_TIME, burnTime);
        nbt.putInt(NBT_INITIAL_BURN_TIME, initialBurnTime);
    }

    public void readFromNBT(CompoundNBT nbt) {
        burnTime = nbt.getInt(NBT_BURN_TIME);
        initialBurnTime = nbt.getInt(NBT_INITIAL_BURN_TIME);
    }

    @Override
    public int get(int index) {
        switch (index) {
            case BURN_TIME_INDEX:
                return burnTime;
            case INITIAL_BURN_TIME_INDEX:
                return initialBurnTime;
            default:
                throw new IndexOutOfBoundsException("Data index out of bounds");
        }
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case BURN_TIME_INDEX:
                burnTime = value;
                return;
            case INITIAL_BURN_TIME_INDEX:
                initialBurnTime = value;
                return;
            default:
                throw new IndexOutOfBoundsException("Data index out of bounds");
        }
    }

    @Override
    public int size() {
        return NUM_INDEXES;
    }
}
