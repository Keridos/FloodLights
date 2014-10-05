package de.keridos.floodlights.core.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import de.keridos.floodlights.client.render.CustomBlockRenderer;
import de.keridos.floodlights.reference.RenderIDs;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;

/**
 * Created by Nico on 28.02.14.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void initRenderers() {
        RenderIDs.electricFloodlight = RenderingRegistry.getNextAvailableRenderId();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectricFloodlight.class, new CustomBlockRenderer());
    }

    @Override
    public void initSounds() {

    }
}