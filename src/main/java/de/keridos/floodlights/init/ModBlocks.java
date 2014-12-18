package de.keridos.floodlights.init;

import cpw.mods.fml.common.registry.GameRegistry;
import de.keridos.floodlights.blocks.BlockCarbonFloodlight;
import de.keridos.floodlights.blocks.BlockElectricFloodlight;
import de.keridos.floodlights.blocks.BlockPhantomLight;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Keridos on 04.10.14.
 * This Class manages all blocks and TileEntities that this mod uses.
 */
public class ModBlocks {
    public static Block blockElectricLight;
    public static Block blockCarbonLight;
    public static Block blockFLLight;
    private static ConfigHandler configHandler = ConfigHandler.getInstance();

    public static void setupBlocks() {
        blockElectricLight = new BlockElectricFloodlight(Material.rock);
        blockCarbonLight = new BlockCarbonFloodlight(Material.rock);
        blockFLLight = new BlockPhantomLight();
    }

    public static void registerBlocks() {
        GameRegistry.registerBlock(blockElectricLight, Names.Blocks.ELECTRIC_FLOODLIGHT);
        GameRegistry.registerBlock(blockCarbonLight, Names.Blocks.CARBON_FLOODLIGHT);
        GameRegistry.registerBlock(blockFLLight, Names.Blocks.PHANTOM_LIGHT);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityElectricFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.ELECTRIC_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityCarbonFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.CARBON_FLOODLIGHT);
    }
}
