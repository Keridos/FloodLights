package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class TileEntityUVFloodlight extends TileEntityFLElectric {

    public TileEntityUVFloodlight() {
        lightBlock = ModBlocks.blockPhantomUVLight;
        rangeStraight = ConfigHandler.rangeUVFloodlight;
    }
}

