package de.keridos.floodlights.core.proxy;

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
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Keridos on 28.02.14.
 * This Class is the proxy for the client.
 */
public class ClientProxy extends CommonProxy {

    public void registerItemModel(final Item item, int meta, final String itemName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MOD_ID + ":" + itemName, "inventory"));
    }

    public void registerItemModel(final Item item, int meta, final String itemName, final String variantName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MOD_ID + ":" + itemName, variantName));
    }

    public void registerBlockModelAsItem(final Block block, int meta, final String blockName)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID + ":" + blockName, "inventory"));
    }

    @Override
    public void initRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPhantomLight.class, new TileEntityPhantomLightRenderer());
        RenderUtil.setupColors();
    }

    @Override
    public void initItemModels() {
        registerBlockModelAsItem(ModBlocks.blockElectricLight, 0 , Names.Blocks.ELECTRIC_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockCarbonLight, 0 , Names.Blocks.CARBON_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockUVLight, 0 , Names.Blocks.UV_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockSmallElectricLight, 0 , Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT);
        registerItemModel(ModItems.carbonDissolver, 0, Names.Items.CARBON_DISSOLVER);
        registerItemModel(ModItems.carbonLantern, 0, Names.Items.CARBON_LANTERN);
        registerItemModel(ModItems.glowingFilament, 0, Names.Items.GLOWING_FILAMENT);
        registerItemModel(ModItems.lightBulb, 0, Names.Items.ELECTRIC_INCANDESCENT_LIGHT_BULB);
        registerItemModel(ModItems.lightDebugTool, 0, Names.Items.LIGHT_DEBUG_TOOL);
        registerItemModel(ModItems.mantle, 0, Names.Items.MANTLE);
        registerItemModel(ModItems.rawFilament, 0, Names.Items.RAW_FILAMENT);
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