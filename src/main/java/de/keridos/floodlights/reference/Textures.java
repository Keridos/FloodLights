package de.keridos.floodlights.reference;

import net.minecraft.util.ResourceLocation;

/**
 * Created by Nico on 04/10/2014.
 */
public class Textures {
    public static final String RESOURCE_PREFIX = Reference.MOD_ID.toLowerCase() + ":";

    public static final class Block {
        private static final String MODEL_TEXTURE_LOCATION = RESOURCE_PREFIX + "textures/blocks/";
        public static final ResourceLocation ELECTRICFLOODLIGHT = new ResourceLocation(MODEL_TEXTURE_LOCATION + "electricFloodlight.png");

    }
}
