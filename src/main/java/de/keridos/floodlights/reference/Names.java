package de.keridos.floodlights.reference;

/**
 * Created by Keridos on 03.10.14.
 * This Class contains the internal names of all blocks, items and NBT Tags.
 */
public class Names {
    public static final class Blocks {
        public static final String ELECTRIC_FLOODLIGHT = "electricFloodlight";
        public static final String CARBON_FLOODLIGHT = "carbonFloodlight";
        public static final String PHANTOM_LIGHT = "blockLight";
    }

    public static final class Items {
        public static final String RAW_FILAMENT = "rawFilament";
        public static final String GLOWING_FILAMENT = "glowingFilament";
        public static final String ELECTRIC_INCANDESCENT_LIGHT_BULB = "electricIncandescentLightBulb";
        public static final String CARBON_DISSOLVER = "carbonDissolver";
        public static final String CARBON_LANTERN = "carbonLantern";
        public static final String MANTLE = "mantle";
    }

    public static final class NBT {
        public static final String ITEMS = "Items";
        public static final String INVERT = "inverted";
        public static final String TIMEOUT = "timeout";
        public static final String TIME_REMAINING = "timeRemaining";
        public static final String STORAGE_EU = "storageEU";
        public static final String COLOR = "color";
        public static final String MODE = "teState";
        public static final String STATE = "teMode";
        public static final String WAS_ACTIVE = "wasActive";
        public static final String CUSTOM_NAME = "CustomName";
        public static final String DIRECTION = "teDirection";
        public static final String OWNER = "owner";
    }
}
