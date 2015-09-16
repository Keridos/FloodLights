package de.keridos.floodlights.init;

import cpw.mods.fml.common.registry.GameRegistry;
import de.keridos.floodlights.block.*;
import de.keridos.floodlights.item.itemBlock.ItemBlockSmallElectricMetaBlock;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.tileentity.*;
import net.minecraft.block.Block;

/**
 * Created by Keridos on 04.10.14.
 * This Class manages all blocks and TileEntities that this mod uses.
 */
public class ModBlocks {
    public static Block blockElectricLight;
    public static Block blockCarbonLight;
    public static Block blockFLLight;
    public static Block blockSmallElectricLight;
    public static Block blockUVLight;
    public static Block blockUVLightBlock;

    public static void setupBlocks() {
        blockElectricLight = new BlockElectricFloodlight();
        blockCarbonLight = new BlockCarbonFloodlight();
        blockFLLight = new BlockPhantomLight();
        blockSmallElectricLight = new BlockSmallElectricFloodlight();
        blockUVLight = new BlockUVLight();
        blockUVLightBlock = new BlockUVLightBlock();
    }

    public static void registerBlocks() {
        GameRegistry.registerBlock(blockElectricLight, Names.Blocks.ELECTRIC_FLOODLIGHT);
        GameRegistry.registerBlock(blockCarbonLight, Names.Blocks.CARBON_FLOODLIGHT);
        GameRegistry.registerBlock(blockFLLight, Names.Blocks.PHANTOM_LIGHT);
        GameRegistry.registerBlock(blockSmallElectricLight, ItemBlockSmallElectricMetaBlock.class, Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT);
        GameRegistry.registerBlock(blockUVLight, Names.Blocks.UV_FLOODLIGHT);
        GameRegistry.registerBlock(blockUVLightBlock, Names.Blocks.UV_LIGHTBLOCK);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityElectricFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.ELECTRIC_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityCarbonFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.CARBON_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntitySmallFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityUVLight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.UV_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityPhantomLight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.PHANTOM_LIGHT);
        GameRegistry.registerTileEntity(TileEntityUVLightBlock.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.UV_LIGHTBLOCK);
    }
}
