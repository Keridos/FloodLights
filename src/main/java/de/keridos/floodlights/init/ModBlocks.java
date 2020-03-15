package de.keridos.floodlights.init;

import de.keridos.floodlights.block.*;
import de.keridos.floodlights.item.itemBlock.ElectricItemBlock;
import de.keridos.floodlights.item.itemBlock.ItemBlockSmallElectricMetaBlock;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Keridos on 04.10.14.
 * This Class manages all blocks and TileEntities that this mod uses.
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class ModBlocks {
    public static Block blockElectricFloodlight = new BlockElectricFloodlight();
    public static Block blockCarbonFloodlight = new BlockCarbonFloodlight();
    public static Block blockSmallElectricLight = new BlockSmallElectricFloodlight();
    public static Block blockUVFloodlight = new BlockUVFloodlight();
    public static Block blockGrowLight = new BlockGrowLight();
    public static Block blockPhantomLight = new BlockPhantomLight();
    public static Block blockPhantomUVLight = new BlockPhantomUVLight();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //registry.register(getBlock(blockSmallElectricLight, ItemBlockSmallElectricMetaBlock.class, Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT);
        event.getRegistry().registerAll(
                blockElectricFloodlight,
                blockCarbonFloodlight,
                blockSmallElectricLight,
                blockUVFloodlight,
                blockGrowLight,
                blockPhantomLight,
                blockPhantomUVLight
        );
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ElectricItemBlock(blockElectricFloodlight).setRegistryName(blockElectricFloodlight.getRegistryName()),
                new ItemBlock(blockCarbonFloodlight).setRegistryName(blockCarbonFloodlight.getRegistryName()),
                new ItemBlockSmallElectricMetaBlock(blockSmallElectricLight).setRegistryName(blockSmallElectricLight.getRegistryName()),
                new ElectricItemBlock(blockUVFloodlight).setRegistryName(blockUVFloodlight.getRegistryName()),
                new ElectricItemBlock(blockGrowLight).setRegistryName(blockGrowLight.getRegistryName()),
                new ItemBlock(blockPhantomLight).setRegistryName(blockPhantomLight.getRegistryName()),
                new ItemBlock(blockPhantomUVLight).setRegistryName(blockPhantomUVLight.getRegistryName())
        );
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityElectricFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.ELECTRIC_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityCarbonFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.CARBON_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntitySmallFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityUVFloodlight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.UV_FLOODLIGHT);
        GameRegistry.registerTileEntity(TileEntityPhantomLight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.PHANTOM_LIGHT);
        GameRegistry.registerTileEntity(TileEntityGrowLight.class, Reference.MOD_ID.toLowerCase() + ":" + Names.Blocks.GROW_LIGHT);
    }
}
