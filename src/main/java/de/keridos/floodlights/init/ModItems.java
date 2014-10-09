package de.keridos.floodlights.init;

import cpw.mods.fml.common.registry.GameRegistry;
import de.keridos.floodlights.item.*;
import de.keridos.floodlights.reference.Names;

/**
 * Created by Keridos on 06.10.14.
 * This Class manages all items that the mod uses.
 */
public class ModItems {
    public static final ItemFL rawFilament = new ItemRawFilament();
    public static final ItemFL glowingFilament = new ItemGlowingFilament();
    public static final ItemFL lightBulb = new ItemLightBulb();
    public static final ItemFL carbonDissolver = new ItemCarbonDissolver();
    public static final ItemFL carbonLantern = new ItemCarbonLantern();
    public static final ItemFL mantle = new ItemMantle();

    public static void init() {
        GameRegistry.registerItem(rawFilament, Names.Items.RAW_FILAMENT);
        GameRegistry.registerItem(glowingFilament, Names.Items.GLOWING_FILAMENT);
        GameRegistry.registerItem(lightBulb, Names.Items.ELECTRIC_INCANDESCENT_LIGHT_BULB);
        GameRegistry.registerItem(carbonDissolver, Names.Items.CARBON_DISSOLVER);
        GameRegistry.registerItem(carbonLantern, Names.Items.CARBON_LANTERN);
        GameRegistry.registerItem(mantle, Names.Items.MANTLE);
    }
}
