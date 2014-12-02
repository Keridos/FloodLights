package de.keridos.floodlights.core.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import de.keridos.floodlights.client.render.RotatableBlockRenderer;
import de.keridos.floodlights.compatability.IGWHandler;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.RenderIDs;

/**
 * Created by Keridos on 28.02.14.
 * This Class is the proxy for the client.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void initRenderers() {
        RenderIDs.ROTATABLE_BLOCK = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderIDs.ROTATABLE_BLOCK, new RotatableBlockRenderer());
    }

    @Override
    public void initSounds() {

    }

    @Override
    public void initHandlers() {
        if (ModCompatibility.IGWModLoaded) {
            ModCompatibility.getInstance().igwHandler = IGWHandler.getInstance();
        }
    }
}