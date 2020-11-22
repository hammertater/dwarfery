package ht.dwarfery;

import ht.dwarfery.config.ConfigHandler;
import ht.dwarfery.init.ModBlocks;
import ht.dwarfery.init.ModContainers;
import ht.dwarfery.init.ModItems;
import ht.dwarfery.init.ModTileEntities;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
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

    public static IEventBus MOD_EVENT_BUS;

    public DwarferyMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        MOD_EVENT_BUS.addListener((ModConfig.Loading e) -> ConfigHandler.onConfigLoad());
        MOD_EVENT_BUS.addListener((ModConfig.Reloading e) -> ConfigHandler.onConfigLoad());

        registerCommonEvents();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DwarferyMod::registerClientOnlyEvents);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void registerCommonEvents() {
        ModBlocks.BLOCKS.register(MOD_EVENT_BUS);
        ModItems.ITEMS.register(MOD_EVENT_BUS);
        ModTileEntities.TILE_ENTITIES.register(MOD_EVENT_BUS);
        ModContainers.CONTAINERS.register(MOD_EVENT_BUS);
    }

    public static void registerClientOnlyEvents() {
        MOD_EVENT_BUS.register(StartupClientOnly.class);
    }

    public static final ItemGroup TAB = new ItemGroup("dwarferyTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.GEODE.get());
        }
    };

}
