package de.keridos.floodlights.core.proxy;

import de.keridos.floodlights.client.render.block.TileEntityPhantomLightRenderer;
import de.keridos.floodlights.client.render.block.TileEntitySmallFoodlightRenderer;
import de.keridos.floodlights.compatability.IGWHandler;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallFloodlight.class, new TileEntitySmallFoodlightRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPhantomLight.class, new TileEntityPhantomLightRenderer());
        registerItemModel(Item.getItemFromBlock(ModBlocks.blockSmallElectricLight), 0, "BlockSmallElectricFloodlight");
        RenderUtil.setupColors();
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