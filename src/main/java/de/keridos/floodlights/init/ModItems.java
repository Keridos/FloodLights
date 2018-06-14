package de.keridos.floodlights.init;

import de.keridos.floodlights.item.*;
import de.keridos.floodlights.reference.Names;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

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
    public static final ItemFL lightDebugTool = new ItemLightDebugTool();

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(
                rawFilament,
                glowingFilament,
                lightBulb,
                carbonDissolver,
                carbonLantern,
                mantle,
                lightDebugTool
        );
    }

    private static void setupItem(Item item, String name) {
        item.setUnlocalizedName(name).setRegistryName(Names.convertToUnderscore(name));
    }

    static {
        setupItem(rawFilament, Names.Items.RAW_FILAMENT);
        setupItem(glowingFilament, Names.Items.GLOWING_FILAMENT);
        setupItem(lightBulb, Names.Items.ELECTRIC_INCANDESCENT_LIGHT_BULB);
        setupItem(carbonDissolver, Names.Items.CARBON_DISSOLVER);
        setupItem(carbonLantern, Names.Items.CARBON_LANTERN);
        setupItem(mantle, Names.Items.MANTLE);
        setupItem(lightDebugTool, Names.Items.LIGHT_DEBUG_TOOL);
    }
}
