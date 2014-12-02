package de.keridos.floodlights.core.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInterModComms;
import de.keridos.floodlights.client.render.RotatableBlockRenderer;
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
        FMLInterModComms.sendMessage("IGWMod", "de.keridos.floodlights.compatability.IGWHandler", "init");
    }
}