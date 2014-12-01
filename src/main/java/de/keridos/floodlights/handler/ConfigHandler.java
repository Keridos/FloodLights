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
    public static int rangeStraightFloodlight;
    public static int rangeConeFloodlight;
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
        energyUsage = config.getInt("energyUsage", "general", 10, 0, 1000, "Energy Usage in RF/t for the electric FloodLight (x4 for the cone floodlights)");
        carbonTime = config.getInt("carbonTime", "general", 300, 0, 1000, "How many seconds should 1 coal last in the carbon floodlight (quarter of that for the cone floodlights)?");
        rangeStraightFloodlight = config.getInt("rangeStraightFloodlight", "general", 64, 1, 128, "How far should the straight Floodlights go?");
        rangeConeFloodlight = config.getInt("rangeConeFloodlight", "general", 32, 1, 64, "How far should the cone floodlights go (Mind that wide only goes quarter as far as this)?.");
        refreshRate = config.getInt("refreshRate", "general", 8, 0, 100, "How many invisible lights should be updated per tick (20 ticks = 1 second)");
        config.save();
    }
}
