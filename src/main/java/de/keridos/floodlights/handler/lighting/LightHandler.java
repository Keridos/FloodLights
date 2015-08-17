package de.keridos.floodlights.handler.lighting;

import com.sun.xml.internal.ws.util.VersionUtil;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.util.DiskIO;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Keridos on 03.10.14.
 * This Class is the main handler for all lights, stores the handler for each world.
 */
public class LightHandler implements Serializable {
    private static LightHandler instance = null;
    private ArrayList<WorldHandler> worlds = new ArrayList<>();
    private String version = "";

    private LightHandler() {
        this.version = Reference.VERSION;
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

    public boolean wrongVersion() {
        return VersionUtil.compare(this.version, Reference.VERSION) != 0;
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

    public void removeDuplicateWorlds() {
        Logger.getGlobal().info("TEEEEEEEEEEEEEEEEEEST");
        if (worlds != null && worlds.size() > 0) {
            for (int i = 0; i < worlds.size(); i++) {
                for (int j = 0; j < worlds.size(); j++) {
                    WorldHandler worldHandler = worlds.get(i);
                    WorldHandler worldHandler2 = worlds.get(j);
                    if (worldHandler.getDimensionID() == worldHandler2.getDimensionID() && i != j) {
                        worldHandler2.removeAllLights();
                        worlds.remove(worldHandler2);
                        worldHandler.removeAllLights();
                        worlds.remove(worldHandler);
                        Logger.getGlobal().info("removed duplicate worldhandlers");
                    }
                }
            }
        }
    }

    public void removeWorldHandler(WorldHandler worldHandler) {
        worlds.remove(worldHandler);
    }

    public void addSource(World world, int x, int y, int z, ForgeDirection direction, int type) {
        boolean foundWorld = false;
        if (worlds == null) {
            worlds.add(getWorldHandler(world));
        }
        for (WorldHandler worldIterator : worlds) {
            if (worldIterator != null) {
                if (worldIterator.getDimensionID() == world.provider.dimensionId) {
                    worldIterator.addSource(x, y, z, direction, type);
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
        for (WorldHandler worldIterator : worlds) {
            if (worldIterator != null) {
                if (worldIterator.getDimensionID() == world.provider.dimensionId) {
                    worldIterator.removeSource(x, y, z, direction, type);
                    break;
                }
            }
        }
    }

    public void updateLights() {
        try {
            if (worlds != null) {
                for (WorldHandler world : worlds) {
                    world.updateRun();
                }
            }
        } catch (Exception e) {
            instance = null;
        }
    }

    public void removeAllLights() {
        if (worlds != null) {
            for (WorldHandler world : worlds) {
                world.removeAllLights();
            }
        }
    }
}
