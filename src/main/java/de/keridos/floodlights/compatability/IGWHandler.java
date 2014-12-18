package de.keridos.floodlights.compatability;

import de.keridos.floodlights.init.ModBlocks;
import igwmod.api.WikiRegistry;

/**
 * Created by Keridos on 02/12/2014.
 * This Class
 */
public class IGWHandler {
    private static IGWHandler instance = null;

    private IGWHandler() {
        initTab();
    }

    public static IGWHandler getInstance() {
        if (instance == null) {
            instance = new IGWHandler();
        }
        return instance;
    }

    private void initTab() {
        WikiRegistry.registerWikiTab(new FloodLightsWikiTab());

        WikiRegistry.registerBlockAndItemPageEntry(ModBlocks.blockElectricLight, "block/electricFloodlight");
        WikiRegistry.registerBlockAndItemPageEntry(ModBlocks.blockCarbonLight, "block/carbonFloodlight");
    }
}
