package de.keridos.floodlights.reference;

/**
 * Created by Keridos on 04.10.14.
 * This Class stores the location of textures for the mods items and blocks.
 */
public class Textures {
    public static final String RESOURCE_PREFIX = Reference.MOD_ID.toLowerCase() + ":";

    public static final class Block {
        private static final String MODEL_TEXTURE_LOCATION = RESOURCE_PREFIX + "textures/blocks/";
    }

    public static final class Gui {
        private static final String GUI_TEXTURE_LOCATION = RESOURCE_PREFIX + "textures/gui/";
        public static final String CARBON_FLOODLIGHT = GUI_TEXTURE_LOCATION + "carbonFloodlight.png";
    }
}
