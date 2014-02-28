package de.keridos.floodlights.blocks;

/**
 * Created by Nico on 28.02.14.
 */
public class BlockHandler {
    private static BlockHandler instance = null;

    private BlockHandler() {
    }

    public static BlockHandler getInstance() {
        if (instance == null) {
            instance = new BlockHandler();
        }
        return instance;
    }
}
