package de.keridos.floodlights.init;

import crazypants.enderio.machines.machine.light.BlockElectricLight;
import de.keridos.floodlights.block.*;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.tileentity.*;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by Keridos on 04.10.14.
 * This Class manages all blocks and TileEntities that this mod uses.
 */
public class ModBlocks {
    public static Block blockElectricLight = new BlockElectricFloodlight();
    public static Block blockCarbonLight = new BlockCarbonFloodlight();
    public static Block blockPhantomLight = new BlockPhantomLight();
    public static Block blockSmallElectricLight = new BlockSmallElectricFloodlight();
    public static Block blockUVLight = new BlockUVLight();
    public static Block blockUVLightBlock = new BlockUVLightBlock();
    public static Block blockGrowLight = new BlockGrowLight();

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        setupBlock(blockElectricLight, Names.Blocks.ELECTRIC_FLOODLIGHT);
        setupBlock(blockCarbonLight, Names.Blocks.CARBON_FLOODLIGHT);
        setupBlock(blockPhantomLight, Names.Blocks.PHANTOM_LIGHT);
        //registry.register(getBlock(blockSmallElectricLight, ItemBlockSmallElectricMetaBlock.class, Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT);
        setupBlock(blockSmallElectricLight, Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT);
        setupBlock(blockUVLight, Names.Blocks.UV_FLOODLIGHT);
        setupBlock(blockUVLightBlock, Names.Blocks.UV_LIGHTBLOCK);
        setupBlock(blockGrowLight, Names.Blocks.GROW_LIGHT);

        registry.registerAll(
                blockElectricLight,
                blockCarbonLight,
                blockPhantomLight,
                blockSmallElectricLight,
                blockUVLight,
                blockUVLightBlock,
                blockGrowLight
        );
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityElectricFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.ELECTRIC_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityCarbonFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.CARBON_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntitySmallFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityUVLight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.UV_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityPhantomLight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.PHANTOM_LIGHT);
        GameRegistry.registerTileEntity(TileEntityUVLightBlock.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.UV_LIGHTBLOCK);
        GameRegistry.registerTileEntity(TileEntityGrowLight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.GROW_LIGHT);
    }

    private static void setupBlock(Block block, String name) {
        block.setUnlocalizedName(name).setRegistryName(Names.convertToUnderscore(name));
    }
}
