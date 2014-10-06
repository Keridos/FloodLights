package de.keridos.floodlights.handler;

import net.minecraftforge.common.config.Configuration;

/**
 * Created by Nico on 06/10/2014.
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
        refreshRate = config.getInt("refreshRate", "general", 3, 0, 100, "How many Phantom lights should be updated per tick (Higher is faster, but lags more)");
        config.save();
    }
}
