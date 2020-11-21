package ht.dwarfery;

import ht.dwarfery.config.ConfigHandler;
import ht.dwarfery.init.ModBlocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("dwarfery")
public class DwarferyMod {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "dwarfery";

    public DwarferyMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener((ModConfig.Loading e) -> ConfigHandler.onConfigLoad());
        modBus.addListener((ModConfig.Reloading e) -> ConfigHandler.onConfigLoad());

        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
    }
}
