package ht.dwarfery;

import ht.dwarfery.client.gui.screen.inventory.HotPlateScreen;
import ht.dwarfery.init.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class StartupClientOnly {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.HOT_PLATE.get(), HotPlateScreen::new);
    }
}
