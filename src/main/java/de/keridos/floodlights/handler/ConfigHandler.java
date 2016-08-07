package de.keridos.floodlights.handler;

import net.minecraftforge.common.config.Configuration;

/**
 * Created by Keridos on 06.10.14.
 * This Class manages the configuration file.
 */
public class ConfigHandler {
    private static ConfigHandler instance = null;

    public static boolean electricFloodlight;
    public static boolean smallElectricFloodlight;
    public static boolean carbonFloodlight;
    public static boolean uvFloodlight;
    public static boolean growLight;
    public static int energyUsage;
    public static int energyUsageSmallFloodlight;
    public static int energyUsageUVFloodlight;
    public static int energyUsageGrowLight;
    public static int carbonTime;
    public static int rangeStraightFloodlight;
    public static int rangeConeFloodlight;
    public static int rangeUVFloodlight;
    public static float chanceGrowLight;
    public static int timeoutFloodlights;
    public static boolean uvLightRendered;
    public static boolean IGWNotifierEnabled;

    public static float damageUVFloodlight;

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
        smallElectricFloodlight = config.getBoolean("smallElectricFloodlightEnabled", "blocks", true, "Enables the small electric FloodLight");
        carbonFloodlight = config.getBoolean("carbonFloodlightEnabled", "blocks", true, "Enables the carbon FloodLight");
        uvFloodlight = config.getBoolean("uvFloodlightEnabled", "blocks", true, "Enables the UV FloodLight");
        growLight = config.getBoolean("growLightEnabled", "blocks", true, "Enables the Grow Light");

        config.getCategory("general");
        energyUsage = config.getInt("energyUsage", "general", 20, 0, 1000, "Energy Usage in RF/t for the electric FloodLight (x4 for the cone floodlights)");
        energyUsageSmallFloodlight = config.getInt("energyUsageSmallFloodlight", "general", 2, 0, 100, "Energy Usage in RF/t for the small electric FloodLight");
        energyUsageUVFloodlight = config.getInt("energyUsageUVFloodlight", "general", 80, 0, 800, "Energy Usage in RF/t for the UV FloodLight");
        energyUsageGrowLight = config.getInt("energyUsageGrowLight", "general", 25, 0, 800, "Energy Usage in RF/t for the Grow Light");
        carbonTime = config.getInt("carbonTime", "general", 300, 0, 1000, "How many seconds should 1 coal last in the carbon floodlight (quarter of that for the cone floodlights)? Default:300");
        rangeStraightFloodlight = config.getInt("rangeStraightFloodlight", "general", 64, 1, 128, "How far should the straight Floodlights go?");
        rangeConeFloodlight = config.getInt("rangeConeFloodlight", "general", 32, 1, 64, "How far should the cone floodlights go? (mind that wide only goes quarter as far) Default:32   ");
        rangeUVFloodlight = config.getInt("rangeUVFloodlight", "general", 8, 1, 32, "How far should the UV Floodlights go?");
        chanceGrowLight = config.getFloat("chanceGrowLight", "general", 0.2F, 0F, 1F, "How big should the chance for the growlight for a growtick per second be?");
        timeoutFloodlights = config.getInt("timeoutFloodlights", "general", 40, 1, 240, "How long should the timeout for turning on floodlights again be in ticks?");
        damageUVFloodlight = config.getFloat("damageUVFloodlight", "general", 4.0F, 1.0F, 16.0F, "How much damage should the UV Floodlights do per second?");
        uvLightRendered = config.getBoolean("uvLightRendered", "general", true, "Should the UV Light Block be visible?");
        IGWNotifierEnabled = config.getBoolean("IGWNotifierEnabled", "general", true, "Should the IGW Notifier be shown?");
        config.save();
    }
}
