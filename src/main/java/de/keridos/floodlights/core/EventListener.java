package de.keridos.floodlights.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.keridos.floodlights.handler.lighting.LightHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Keridos on 28.02.14.
 * This Class Listens for normal forge events.
 */
public class EventListener {
    private static EventListener instance;
    public static LightHandler lightHandler;

    private EventListener() {
    }

    public static EventListener getInstance() {
        if (instance == null) {
            instance = new EventListener();
        }
        return instance;
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (event.blockSnapshot.getReplacedBlock() == ModBlocks.blockFLLight) {
            TileEntityPhantomLight te = (TileEntityPhantomLight) event.blockSnapshot.getWorld().getTileEntity(event.blockSnapshot.x, event.blockSnapshot.y, event.blockSnapshot.z);
            te.removeAllSources();
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled()) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; i < 2; j++) {
                for (int k = -1; i < 2; k++) {
                    int x = event.x + i;
                    int y = event.y + j;
                    int z = event.z + k;
                    if (event.block == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight te = (TileEntityPhantomLight) event.world.getTileEntity(x, y, z);
                        te.updateAllSources();
                    }
                }
            }
        }
    }
}
