package de.keridos.floodlights.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import de.keridos.floodlights.handler.LightHandler;

/**
 * Created by Nico on 05/10/2014.
 */
public class FMLEventListener {
    private static FMLEventListener instance = null;
    private static LightHandler lightHandler = LightHandler.getInstance();

    private FMLEventListener() {
    }

    public static FMLEventListener getInstance() {
        if (instance == null) {
            instance = new FMLEventListener();
        }
        return instance;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {

    }

    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

    }

    //Called when the server ticks. Usually 20 ticks a second.
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        lightHandler.updateLights();

    }

    //Called when a new frame is displayed (See fps)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {

    }

    //Called when the world ticks
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {

    }


}
