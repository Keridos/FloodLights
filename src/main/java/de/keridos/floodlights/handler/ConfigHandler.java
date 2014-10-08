package de.keridos.floodlights.handler;

import net.minecraftforge.common.config.Configuration;

/**
 * Created by Keridos on 06.10.14.
 * This Class manages the configuration file.
 */
public class ConfigHandler {
    private static ConfigHandler instance = null;

    public static boolean electricFloodlight;
    public static int energyUsage;
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
        config.getCategory("crafting");
        electricFloodlight = config.getBoolean("electricFloodlightEnabled", "crafting", true, "Enables the electric FloodLight");
        config.getCategory("general");
        energyUsage = config.getInt("energyUsage", "general", 5, 0, 1000, "Energy Usage in RF/t for the electric FloodLight");
        refreshRate = config.getInt("refreshRate", "general", 8, 0, 100, "How many invisible lights should be updated per tick (20 ticks = 1 second)");
        config.save();
    }
}
