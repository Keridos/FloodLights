package de.keridos.floodlights.compatability;

import cpw.mods.fml.common.Loader;

/**
 * Created by Keridos on 28.02.14.
 * This Class will be used for Mod Compatibility functions.
 */
public class ModCompatibility {
    private static ModCompatibility instance = null;

    public static boolean IC2Loaded = false;

    private ModCompatibility() {
    }

    public static ModCompatibility getInstance() {
        if (instance == null) {
            instance = new ModCompatibility();
        }
        return instance;
    }

    public void checkForMods() {
        IC2Loaded = Loader.isModLoaded("IC2");
    }
}
