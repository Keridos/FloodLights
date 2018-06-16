package de.keridos.floodlights.init;

import de.keridos.floodlights.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Keridos on 06.10.14.
 * This Class manages all items that the mod uses.
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class ModItems {
    public static final ItemFL rawFilament = new ItemRawFilament();
    public static final ItemFL glowingFilament = new ItemGlowingFilament();
    public static final ItemFL lightBulb = new ItemLightBulb();
    public static final ItemFL carbonDissolver = new ItemCarbonDissolver();
    public static final ItemFL carbonLantern = new ItemCarbonLantern();
    public static final ItemFL mantle = new ItemMantle();
    public static final ItemFL lightDebugTool = new ItemLightDebugTool();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                rawFilament,
                glowingFilament,
                lightBulb,
                carbonDissolver,
                carbonLantern,
                mantle,
                lightDebugTool
        );
    }
}
