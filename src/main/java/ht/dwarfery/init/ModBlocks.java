package ht.dwarfery.init;

import ht.dwarfery.DwarferyMod;
import ht.dwarfery.block.GeodeBlock;
import ht.dwarfery.block.HotPlateBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DwarferyMod.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> GEODE = BLOCKS.register("geode",
            () -> new GeodeBlock(
                    AbstractBlock.Properties.create(
                            Material.ROCK,
                            MaterialColor.GRAY)
                            .hardnessAndResistance(4.0F)
                            .harvestTool(ToolType.PICKAXE)
                            .sound(SoundType.STONE)
            )
    );

    public static final RegistryObject<Block> HOT_PLATE = BLOCKS.register("hot_plate",
            () -> new HotPlateBlock(
                    AbstractBlock.Properties.create(
                            Material.ROCK,
                            MaterialColor.GRAY)
                            .hardnessAndResistance(3.5F)
                            .harvestTool(ToolType.PICKAXE)
                            .sound(SoundType.STONE)
            )
    );

}
