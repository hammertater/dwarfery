package ht.dwarfery.init;

import ht.dwarfery.DwarferyMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DwarferyMod.MOD_ID);

    // Items
    public static final RegistryObject<Item> GEODE = ITEMS.register("geode",
            () -> new BlockItem(ModBlocks.GEODE.get(), new Item.Properties().group(DwarferyMod.TAB))
    );

    public static final RegistryObject<Item> HOT_PLATE = ITEMS.register("hot_plate",
            () -> new BlockItem(ModBlocks.HOT_PLATE.get(), new Item.Properties().group(DwarferyMod.TAB))
    );

    public static final RegistryObject<Item> ROCK_SPLITTER = ITEMS.register("rock_splitter",
            () -> new BlockItem(ModBlocks.ROCK_SPLITTER.get(), new Item.Properties().group(DwarferyMod.TAB))
    );

}
