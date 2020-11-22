package ht.dwarfery.init;

import ht.dwarfery.DwarferyMod;
import ht.dwarfery.inventory.container.HotPlateContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DwarferyMod.MOD_ID);

    // Containers
    public static final RegistryObject<ContainerType<HotPlateContainer>> HOT_PLATE = CONTAINERS.register("hot_plate",
            () -> new ContainerType<>((id, playerInventory) -> HotPlateContainer.createContainerClientSide(id, playerInventory, null)));

}
