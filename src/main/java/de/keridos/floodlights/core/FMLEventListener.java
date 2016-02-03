package de.keridos.floodlights.core;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Keridos on 05.10.14.
 * This Class listens for FML events.
 */
public class FMLEventListener {
    private static FMLEventListener instance = null;

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
        //EventListener.lightHandler.updateLights();
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
