package de.keridos.floodlights.core.proxy;

import de.keridos.floodlights.util.RenderUtil;
import net.minecraft.world.World;

/**
 * Created by Keridos on 28.02.14.
 * This Class is the common proxy.
 */
public class CommonProxy {

    public void initRenderers() {
        RenderUtil.setupColors();
    }

    public void preInit(){

    }

    public void initSounds() {

    }

    public void initHandlers() {

    }

    public World getWorld(){
        return null;
    }
}