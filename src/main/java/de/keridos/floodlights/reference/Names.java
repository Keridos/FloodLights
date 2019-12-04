package de.keridos.floodlights.reference;

/**
 * Created by Keridos on 03.10.14.
 * This Class contains the internal names of all blocks, items and NBT Tags.
 */
public class Names {

    public static final String MOD_ID = Reference.MOD_ID;

    public static final class Blocks {
        public static final String ELECTRIC_FLOODLIGHT = "electricFloodlight";
        public static final String SMALL_ELECTRIC_FLOODLIGHT = "smallElectricFloodlightMetaBlock";
        public static final String CARBON_FLOODLIGHT = "carbonFloodlight";
        public static final String UV_FLOODLIGHT = "uvFloodlight";
        public static final String GROW_LIGHT = "growLight";
        public static final String PHANTOM_LIGHT = "phantomLight";
        public static final String PHANTOM_UV_LIGHT = "phantomUVLight";
    }

    public static final class Items {
        public static final String RAW_FILAMENT = "rawFilament";
        public static final String GLOWING_FILAMENT = "glowingFilament";
        public static final String ELECTRIC_INCANDESCENT_LIGHT_BULB = "electricIncandescentLightBulb";
        public static final String CARBON_DISSOLVER = "carbonDissolver";
        public static final String CARBON_LANTERN = "carbonLantern";
        public static final String MANTLE = "mantle";
        public static final String LIGHT_DEBUG_TOOL = "lightDebugTool";
    }

    public static final class Localizations {
        public static final String NONELECTRIC_GUI_TEXT = "gui.floodlights:nonElectricFloodlightTimeRemaining";
        public static final String RF_STORAGE = "gui.floodlights:RFStorage";
        public static final String MODE = "gui.floodlights:mode";
        public static final String INVERT = "gui.floodlights:invert";
        public static final String TRUE = "gui.floodlights:true";
        public static final String FALSE = "gui.floodlights:false";
        public static final String STRAIGHT = "gui.floodlights:straight";
        public static final String NARROW_CONE = "gui.floodlights:narrowCone";
        public static final String WIDE_CONE = "gui.floodlights:wideCone";
        public static final String MACHINE_ENABLED_ERROR = "gui.floodlights:machineEnabledError";
        public static final String LIGHTING = "gui.floodlights:growLightLighting";
        public static final String DARK_LIGHT = "gui.floodlights:growLightDarkLight";
    }

    public static final class NBT {
        public static final String ITEMS = "Items";
        public static final String INVERT = "inverted";
        public static final String TIME_REMAINING = "timeRemaining";
        public static final String STORAGE_FE = "storageFE";
        public static final String COLOR = "color";
        public static final String MODE = "teState";
        public static final String LIGHT = "teLight";
        public static final String WAS_ACTIVE = "wasActive";
        public static final String HAS_REDSTONE = "teRedstone";
        public static final String CURRENT_RANGE = "teRange";
        public static final String FLOODLIGHT_ID = "floodlightId";
        public static final String CUSTOM_NAME = "CustomName";
        public static final String DIRECTION = "teDirection";
        public static final String ROTATION_STATE = "teRotationState";
        public static final String OWNER = "owner";
        public static final String SOURCES = "sources";
        public static final String LIGHT_BLOCK = "lightBlock";
        public static final String CLOAK_BLOCK = "cloakBlock";
        public static final String CLOAK_BLOCKSTATE = "cloakBlockState";
    }

    public static final class DamageSources {
        public static final String UV_LIGHT = "floodlights:uvlight";
    }

    /**
     * Converts given string (block or item name) to underscore-based, which is used as a registry name.
     */
    public static String convertToUnderscore(String input) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c))
                builder.append("_").append(Character.toLowerCase(c));
            else
                builder.append(c);
        }

        return builder.toString();
    }
}
