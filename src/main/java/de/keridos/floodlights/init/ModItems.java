package de.keridos.floodlights.init;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import de.keridos.floodlights.item.ItemFL;
import de.keridos.floodlights.item.ItemGlowingFilament;
import de.keridos.floodlights.item.ItemLightBulb;
import de.keridos.floodlights.item.ItemRawFilament;
import de.keridos.floodlights.reference.Names;

/**
 * Created by Keridos on 06.10.14.
 * This Class manages all items that the mod uses.
 */
public class ModItems {
    public static final ItemFL rawFilament = new ItemRawFilament();
    public static final ItemFL glowingFilament = new ItemGlowingFilament();
    public static final ItemFL lightBulb = new ItemLightBulb();

    public static void init() {
        GameRegistry.registerItem(rawFilament, Names.Items.RAW_FILAMENT);
        GameRegistry.registerItem(glowingFilament, Names.Items.GLOWING_FILAMENT);
        GameRegistry.registerItem(lightBulb, Names.Items.ELECTRIC_LIGHT_BULB);
        LanguageRegistry.addName(rawFilament, "Raw Filament");
        LanguageRegistry.addName(glowingFilament, "Glowing Filament");
        LanguageRegistry.addName(lightBulb, "Electric light bulb");
    }
}
