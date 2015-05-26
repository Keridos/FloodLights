package de.keridos.floodlights.reference;

/**
 * Created by Keridos on 03.10.14.
 * This Class contains the internal names of all blocks, items and NBT Tags.
 */
public class Names {
    public static final class Blocks {
        public static final String ELECTRIC_FLOODLIGHT = "electricFloodlight";
        public static final String SMALL_ELECTRIC_FLOODLIGHT = "smallElectricFloodlight";
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

    public static final class Localizations {
        public static final String NONELECTRIC_GUI_TEXT = "gui.floodlights:nonElectricFloodlightTimeRemaining";
        public static final String MODE = "gui.floodlights:mode";
        public static final String INVERT = "gui.floodlights:invert";
        public static final String TRUE = "gui.floodlights:true";
        public static final String FALSE = "gui.floodlights:false";
        public static final String STRAIGHT = "gui.floodlights:straight";
        public static final String NARROW_CONE = "gui.floodlights:narrowCone";
        public static final String WIDE_CONE = "gui.floodlights:wideCone";
        public static final String GUI_MINUTES_SHORT = "gui.floodlights:minutesShort";
        public static final String GUI_SECONDS_SHORT = "gui.floodlights:secondsShort";
        public static final String CLEARLIGHTS_COMMAND_TEXT = "gui.floodlights:clearLightsCommandText";
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
        public static final String ROTATION_STATE = "teRotationState";
        public static final String OWNER = "owner";
    }
}
