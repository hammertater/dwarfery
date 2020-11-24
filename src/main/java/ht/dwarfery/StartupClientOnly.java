package ht.dwarfery;

import ht.dwarfery.client.gui.screen.inventory.HotPlateScreen;
import ht.dwarfery.client.renderer.tileentity.RockSplitterTileEntityRenderer;
import ht.dwarfery.init.ModContainers;
import ht.dwarfery.init.ModTileEntities;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class StartupClientOnly {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.HOT_PLATE.get(), HotPlateScreen::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.ROCK_SPLITTER.get(), RockSplitterTileEntityRenderer::new);
    }
}
