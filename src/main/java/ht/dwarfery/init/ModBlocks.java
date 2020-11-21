package ht.dwarfery.init;

import ht.dwarfery.DwarferyMod;
import net.minecraft.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DwarferyMod.MOD_ID);

    // Blocks
//    public static final RegistryObject<Block> CHOPPED_LOG = BLOCKS.register("chopped_log",
//            () -> new GeodeBlock(
//                    AbstractBlock.Properties.create(
//                            Material.WOOD,
//                            MaterialColor.WOOD)
//                            .hardnessAndResistance(2.0F)
//                            .sound(SoundType.WOOD))
//    );

}
