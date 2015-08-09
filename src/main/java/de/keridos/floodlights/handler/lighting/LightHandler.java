package de.keridos.floodlights.handler.lighting;

import de.keridos.floodlights.util.DiskIO;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Keridos on 03.10.14.
 * This Class is the main handler for all lights, stores the handler for each world.
 */
public class LightHandler implements Serializable {
    private static LightHandler instance = null;
    private ArrayList<WorldHandler> worlds = new ArrayList<WorldHandler>();

    private LightHandler() {
    }

    public static LightHandler getInstance() {
        if (instance == null) {
            instance = DiskIO.loadFromDisk();
            if (instance == null) {
                instance = new LightHandler();
            }
        }
        return instance;
    }

    public WorldHandler getWorldHandler(World world) {
        boolean added = false;
        if (worlds == null) {
            added = true;
            worlds.add(new WorldHandler(world));
        }
        for (WorldHandler w : worlds) {
            if (w.getDimensionID() == world.provider.dimensionId) {
                return w;
            }
        }
        if (!added) {
            worlds.add(new WorldHandler(world));
        }
        return getWorldHandler(world);
    }

    public void addSource(World world, int x, int y, int z, ForgeDirection direction, int type) {
        boolean foundWorld = false;
        if (worlds == null) {
            worlds.add(getWorldHandler(world));
        }
        for (Object worldIterator : worlds) {
            if (worldIterator != null) {
                if (((WorldHandler) worldIterator).getDimensionID() == world.provider.dimensionId) {
                    ((WorldHandler) worldIterator).addSource(x, y, z, direction, type);
                    foundWorld = true;
                    break;
                }
            }
        }
        if (!foundWorld) {
            worlds.add(new WorldHandler(world));
            getWorldHandler(world).addSource(x, y, z, direction, type);
        }
    }

    public void removeSource(World world, int x, int y, int z, ForgeDirection direction, int type) {
        if (worlds == null) {
            worlds.add(getWorldHandler(world));
        }
        for (Object worldIterator : worlds) {
            if (worldIterator != null) {
                if (((WorldHandler) worldIterator).getDimensionID() == world.provider.dimensionId) {
                    ((WorldHandler) worldIterator).removeSource(x, y, z, direction, type);
                    break;
                }
            }
        }
    }

    public void updateLights() {
        if (worlds != null) {
            for (Object world : worlds) {
                ((WorldHandler) world).updateRun();
            }
        }
    }

    public void removeAllLights() {
        if (worlds != null) {
            for (Object world : worlds) {
                ((WorldHandler) world).removeAllLights();
            }
        }
    }
}
