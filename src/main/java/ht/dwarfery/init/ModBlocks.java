package ht.dwarfery.init;

import ht.dwarfery.DwarferyMod;
import ht.dwarfery.block.GeodeBlock;
import ht.dwarfery.block.HotPlateBlock;
import ht.dwarfery.block.MovingRockSplitterBlock;
import ht.dwarfery.block.OpenedGeodeBlock;
import ht.dwarfery.block.RockSplitterBlock;
import ht.dwarfery.block.RockSplitterChiselBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static net.minecraft.block.Block.makeCuboidShape;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DwarferyMod.MOD_ID);

    // Blocks
    private static final Supplier<AbstractBlock.Properties> GEODE_PROPERTIES = () -> AbstractBlock.Properties.create(
            Material.ROCK,
            MaterialColor.GRAY)
            .hardnessAndResistance(4.0F)
            .harvestTool(ToolType.PICKAXE)
            .sound(SoundType.STONE);

    public static final RegistryObject<Block> GEODE = BLOCKS.register("geode",
            () -> new GeodeBlock(GEODE_PROPERTIES.get())
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

    public static final RegistryObject<Block> ROCK_SPLITTER = BLOCKS.register("rock_splitter",
            () -> new RockSplitterBlock(
                    AbstractBlock.Properties.create(
                            Material.ROCK,
                            MaterialColor.GRAY)
                            .hardnessAndResistance(3.5F)
                            .harvestTool(ToolType.PICKAXE)
                            .sound(SoundType.STONE)
            )
    );

    public static final RegistryObject<Block> MOVING_ROCK_SPLITTER = BLOCKS.register("moving_rock_splitter",
            () -> new MovingRockSplitterBlock(
                    AbstractBlock.Properties.create(
                            Material.ROCK,
                            MaterialColor.GRAY)
                            .hardnessAndResistance(3.5F)
                            .harvestTool(ToolType.PICKAXE)
                            .sound(SoundType.STONE)
            )
    );

    public static final RegistryObject<Block> ROCK_SPLITTER_CHISEL = BLOCKS.register("rock_splitter_chisel",
            () -> new RockSplitterChiselBlock(
                    AbstractBlock.Properties.create(
                            Material.ROCK,
                            MaterialColor.GRAY)
                            .hardnessAndResistance(3.5F)
                            .harvestTool(ToolType.PICKAXE)
                            .sound(SoundType.STONE)
            )
    );

}
