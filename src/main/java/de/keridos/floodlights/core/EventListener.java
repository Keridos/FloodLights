package de.keridos.floodlights.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Keridos on 28.02.14.
 * This Class Listens for normal forge events.
 */
public class EventListener {
    private static EventListener instance;

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

/*    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (event.blockSnapshot.getReplacedBlock() == ModBlocks.blockFLLight) {
            TileEntityPhantomLight te = (TileEntityPhantomLight) event.blockSnapshot.getWorld().getTileEntity(event.blockSnapshot.x, event.blockSnapshot.y, event.blockSnapshot.z);
            if (te != null) {
                te.updateAllSources();
            }
        }
    }*/

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled()) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    int x = event.x + i;
                    int y = event.y + j;
                    int z = event.z + k;
                    TileEntity te = event.world.getTileEntity(x, y, z);
                    if (te != null && te instanceof TileEntityPhantomLight) {
                        ((TileEntityPhantomLight) te).updateAllSources();
                    } else if (te != null && te instanceof TileEntityMetaFloodlight) {
                        ((TileEntityMetaFloodlight) te).toggleUpdateRun();
                    }
                }
            }
        }
    }
}
