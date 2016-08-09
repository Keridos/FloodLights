package de.keridos.floodlights.core;

import de.keridos.floodlights.block.BlockFL;
import de.keridos.floodlights.compatability.ModCompatibility;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    public void RightClickBlock(PlayerInteractEvent.RightClickBlock event){
          if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockFL
                  && ModCompatibility.getInstance().isItemValidWrench(event.getItemStack())){
              event.setUseItem(Event.Result.DENY);
          }
    }
}
