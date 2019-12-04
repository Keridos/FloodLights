package de.keridos.floodlights.tileentity;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the electric floodlight TileEntity.
 */

public class TileEntityElectricFloodlight extends TileEntityFLElectric {

    @Override
    protected int getInventorySize() {
        return 2;
    }

    @Override
    public boolean supportsCloak() {
        return true;
    }
}
