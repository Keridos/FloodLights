package de.keridos.floodlights.core;

/**
 * Created by Nico on 28.02.14.
 */
public class Config {
    private static Config instance = null;

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
