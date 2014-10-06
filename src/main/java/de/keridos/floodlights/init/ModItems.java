package de.keridos.floodlights.init;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import de.keridos.floodlights.item.ItemFL;
import de.keridos.floodlights.item.ItemGlowingFilament;
import de.keridos.floodlights.item.ItemLightbulb;
import de.keridos.floodlights.item.ItemRawFilament;
import de.keridos.floodlights.reference.Names;

/**
 * Created by Nico on 06/10/2014.
 */
public class ModItems {
    public static final ItemFL rawFilament = new ItemRawFilament();
    public static final ItemFL glowingFilament = new ItemGlowingFilament();
    public static final ItemFL lightbulb = new ItemLightbulb();

    public static void init() {
        GameRegistry.registerItem(rawFilament, Names.Items.RAW_FILAMENT);
        GameRegistry.registerItem(glowingFilament, Names.Items.GLOWING_FILAMENT);
        GameRegistry.registerItem(lightbulb, Names.Items.ELECTRIC_LIGHTBULB);
        LanguageRegistry.addName(rawFilament, "Raw Filament");
        LanguageRegistry.addName(glowingFilament, "Glowing Filament");
        LanguageRegistry.addName(lightbulb, "Electric Lightbulb");
    }
}
