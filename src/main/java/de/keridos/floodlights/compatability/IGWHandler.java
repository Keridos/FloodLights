package de.keridos.floodlights.compatability;

import de.keridos.floodlights.init.ModBlocks;
import igwmod.api.WikiRegistry;

/**
 * Created by Keridos on 02/12/2014.
 * This class handles initialization of the IGW mod-related content.
 */
@SuppressWarnings("unused")
public class IGWHandler {

    public static void init() {
        WikiRegistry.registerWikiTab(new FloodLightsWikiTab());

        WikiRegistry.registerBlockAndItemPageEntry(ModBlocks.blockElectricFloodlight, "block/electric_floodlight");
        WikiRegistry.registerBlockAndItemPageEntry(ModBlocks.blockCarbonFloodlight, "block/carbon_floodlight");
        WikiRegistry.registerBlockAndItemPageEntry(ModBlocks.blockSmallElectricLight, "block/small_electric_floodlight");
    }
}
