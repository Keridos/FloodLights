package de.keridos.floodlights.handler;

import net.minecraftforge.common.config.Configuration;

/**
 * Created by Keridos on 06.10.14.
 * This Class manages the configuration file.
 */
public class ConfigHandler {
    private static ConfigHandler instance = null;

    public static boolean electricFloodlight;
    public static boolean carbonFloodlight;
    public static int energyUsage;
    public static int carbonTime;
    public static int refreshRate;

    private ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if (instance == null) {
            instance = new ConfigHandler();
        }
        return instance;
    }

    public void initConfig(Configuration config) {
        config.load();
        config.getCategory("blocks");
        electricFloodlight = config.getBoolean("electricFloodlightEnabled", "blocks", true, "Enables the electric FloodLight");
        carbonFloodlight = config.getBoolean("carbonFloodlightEnabled", "blocks", true, "Enables the carbon FloodLight");
        config.getCategory("general");
        energyUsage = config.getInt("energyUsage", "general", 5, 0, 1000, "Energy Usage in RF/t for the electric FloodLight");
        carbonTime = config.getInt("carbonTime", "general", 300, 0, 1000, "How many seconds should 1 coal last in the carbon floodlight?");
        refreshRate = config.getInt("refreshRate", "general", 8, 0, 100, "How many invisible lights should be updated per tick (20 ticks = 1 second)");
        config.save();
    }
}
