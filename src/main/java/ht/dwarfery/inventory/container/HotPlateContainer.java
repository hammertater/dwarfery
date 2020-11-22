package ht.dwarfery.inventory.container;

import ht.dwarfery.init.ModContainers;
import ht.dwarfery.tileentity.HotPlateTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

public class HotPlateContainer extends Container {

    private final ContainerStacks hotPlateFuelStacks;
    private final HotPlateData hotPlateData;

    protected final World world;

    public static final int NUM_FUEL_SLOTS = HotPlateTileEntity.NUM_FUEL_SLOTS;

    public static HotPlateContainer createContainerServerSide(int id, PlayerInventory playerInventory, ContainerStacks fuelStacks, HotPlateData hotPlateData) {
        return new HotPlateContainer(id, playerInventory, fuelStacks, hotPlateData);
    }

    public static HotPlateContainer createContainerClientSide(int id, PlayerInventory playerInventory, PacketBuffer extraData) {
        // See https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/ContainerFurnace.java
        ContainerStacks fuelStacks = ContainerStacks.createForClientSideContainer(NUM_FUEL_SLOTS);
        HotPlateData data = new HotPlateData();
        return new HotPlateContainer(id, playerInventory, fuelStacks, data);
    }

    public HotPlateContainer(int id, PlayerInventory playerInventory, ContainerStacks hotPlateFuelStacks, HotPlateData hotPlateData) {
        super(ModContainers.HOT_PLATE.get(), id);

        this.hotPlateFuelStacks = hotPlateFuelStacks;
        this.hotPlateData = hotPlateData;
        this.world = playerInventory.player.world;

        trackIntArray(hotPlateData);

        addSlot(new FuelSlot(hotPlateFuelStacks, 0, 80, 41));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.hotPlateFuelStacks.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return super.transferStackInSlot(playerIn, index);
        // TODO
    }

    public double getBurnLeftScaled() {
        return this.hotPlateData.initialBurnTime == 0 ? 0.0 : (double) this.hotPlateData.burnTime / (double) this.hotPlateData.initialBurnTime;
    }

    public boolean isBurning() {
        return this.hotPlateData.get(0) > 0;
    }
}
