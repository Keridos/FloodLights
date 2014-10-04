package de.keridos.floodlights.init;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import de.keridos.floodlights.blocks.BlockElectricFloodlight;
import de.keridos.floodlights.blocks.BlockPhantomLight;
import de.keridos.floodlights.core.Config;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Nico on 04/10/2014.
 */
public class ModBlocks {
    public static Block blockElectricLight;
    public static Block blockFLLight;
    private static Config Configuration = Config.getInstance();

    public static void setupBlocks() {
        blockElectricLight = new BlockElectricFloodlight(Material.rock);
        blockFLLight = new BlockPhantomLight();

    }

    public static void registerBlocks() {
        GameRegistry.registerBlock(blockElectricLight, "blockElectricFloodlight");
        LanguageRegistry.addName(blockElectricLight, "Electric FloodLight");
        GameRegistry.registerBlock(blockFLLight, "blockLight");
        LanguageRegistry.addName(blockFLLight, "FloodLight PhantomLight");
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityElectricFloodlight.class, "floodlights:electricFloodlight");
    }

}
