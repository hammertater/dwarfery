package ht.dwarfery.inventory.container;

import ht.dwarfery.init.ModContainers;
import ht.dwarfery.tileentity.HotPlateTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HotPlateContainer extends Container {

    private final IInventory hotPlateInventory;
    private final IIntArray hotPlateData;
    protected final World world;

    public static HotPlateContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        // See https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe30_inventory_basic/StartupCommon.java
        return new HotPlateContainer(windowID, playerInventory);
    }

    public HotPlateContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(HotPlateTileEntity.INVENTORY_SIZE), new IntArray(HotPlateTileEntity.DATA_SIZE));
    }

    public HotPlateContainer(int id, PlayerInventory playerInventory, IInventory hotPlateInventory, IIntArray hotPlateData) {
        super(ModContainers.HOT_PLATE.get(), id);

        assertInventorySize(hotPlateInventory, HotPlateTileEntity.INVENTORY_SIZE);
        assertIntArraySize(hotPlateData, HotPlateTileEntity.DATA_SIZE);
        this.hotPlateInventory = hotPlateInventory;
        this.hotPlateData = hotPlateData;
        this.world = playerInventory.player.world;

        this.addSlot(new FuelSlot(hotPlateInventory, 0, 56, 53));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(hotPlateData);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.hotPlateInventory.isUsableByPlayer(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return hotPlateInventory.getSizeInventory();
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int i = this.hotPlateData.get(0);
        if (i == 0) {
            i = 200;
        }

        return this.hotPlateData.get(0) * 13 / i;
    }

    public boolean isBurning() {
        return this.hotPlateData.get(0) > 0;
    }
}
