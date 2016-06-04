package de.keridos.floodlights.core.proxy;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.block.BlockSmallElectricFloodlight;
import de.keridos.floodlights.client.render.block.TileEntityPhantomLightRenderer;
import de.keridos.floodlights.compatability.IGWHandler;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.init.ModItems;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import de.keridos.floodlights.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Keridos on 28.02.14.
 * This Class is the proxy for the client.
 */
public class ClientProxy extends CommonProxy {

    public void registerItemModel(final Item item, int meta)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    public void registerItemModel(final Item item, int meta, final String variantName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variantName));
    }

    public void registerBlockModelAsItem(final Block block, int meta, final String blockName)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + blockName, "inventory"));
    }

    public void registerBlockModelAsItem(final Block block, int meta, final String blockName, String variantName)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + blockName, variantName));
    }

    @Override
    public void initRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPhantomLight.class, new TileEntityPhantomLightRenderer());
        RenderUtil.setupColors();
    }

    @Override
    public void preInit() {
        registerBlockModelAsItem(ModBlocks.blockElectricLight, 0 , Names.Blocks.ELECTRIC_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockCarbonLight, 0 , Names.Blocks.CARBON_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockUVLight, 0 , Names.Blocks.UV_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockSmallElectricLight, 0 , Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT,"inventory_strip");
        registerBlockModelAsItem(ModBlocks.blockSmallElectricLight, 1 , Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT,"inventory_square");
        registerItemModel(ModItems.carbonDissolver, 0);
        registerItemModel(ModItems.carbonLantern, 0);
        registerItemModel(ModItems.glowingFilament, 0);
        registerItemModel(ModItems.lightBulb, 0);
        registerItemModel(ModItems.lightDebugTool, 0);
        registerItemModel(ModItems.mantle, 0);
        registerItemModel(ModItems.rawFilament, 0);
        StateMap ignoreColor = new StateMap.Builder().ignore(BlockFLColorableMachine.COLOR).build();
        ModelLoader.setCustomStateMapper(ModBlocks.blockCarbonLight,ignoreColor);
        ModelLoader.setCustomStateMapper(ModBlocks.blockElectricLight,ignoreColor);
        ModelLoader.setCustomStateMapper(ModBlocks.blockUVLight,ignoreColor);
        ModelLoader.setCustomStateMapper(ModBlocks.blockSmallElectricLight, new StateMap.Builder().withName(BlockSmallElectricFloodlight.MODEL).ignore(BlockFLColorableMachine.COLOR).build());
    }

    @Override
    public void initSounds() {

    }

    @Override
    public void initHandlers() {
        if (ModCompatibility.IGWModLoaded) {
            ModCompatibility.getInstance().igwHandler = IGWHandler.getInstance();
        }
    }
}