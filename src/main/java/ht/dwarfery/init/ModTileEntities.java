package ht.dwarfery.init;

import ht.dwarfery.DwarferyMod;
import ht.dwarfery.tileentity.HotPlateTileEntity;
import ht.dwarfery.tileentity.RockSplitterTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities<T extends TileEntity> extends net.minecraftforge.registries.ForgeRegistryEntry<net.minecraft.tileentity.TileEntityType<?>> {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, DwarferyMod.MOD_ID);

    // Tile entities
    public static final RegistryObject<TileEntityType<HotPlateTileEntity>> HOT_PLATE = TILE_ENTITIES.register("hot_plate",
            () -> TileEntityType.Builder.create(HotPlateTileEntity::new, ModBlocks.HOT_PLATE.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<RockSplitterTileEntity>> ROCK_SPLITTER = TILE_ENTITIES.register("rock_splitter",
            () -> TileEntityType.Builder.create(RockSplitterTileEntity::new, ModBlocks.MOVING_ROCK_SPLITTER.get()).build(null)
    );

}
