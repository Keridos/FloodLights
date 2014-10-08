package de.keridos.floodlights.reference;

import net.minecraft.util.ResourceLocation;

/**
 * Created by Keridos on 04.10.14.
 * This Class stores the location of textures for the mods items and blocks.
 */
public class Textures {
    public static final String RESOURCE_PREFIX = Reference.MOD_ID.toLowerCase() + ":";

    public static final class Block {
        private static final String MODEL_TEXTURE_LOCATION = RESOURCE_PREFIX + "textures/blocks/";
        public static final ResourceLocation ELECTRICFLOODLIGHT = new ResourceLocation(MODEL_TEXTURE_LOCATION + "FLOODLIGHT.png");

    }
}
