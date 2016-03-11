package de.keridos.floodlights.core.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import de.keridos.floodlights.client.render.block.RotatableBlockRenderer;
import de.keridos.floodlights.client.render.block.TileEntityGrowLightRenderer;
import de.keridos.floodlights.client.render.block.TileEntityPhantomLightRenderer;
import de.keridos.floodlights.client.render.block.TileEntitySmallFoodlightRenderer;
import de.keridos.floodlights.client.render.item.GrowLightItemRenderer;
import de.keridos.floodlights.client.render.item.SmallFloodlightItemRenderer;
import de.keridos.floodlights.compatability.IGWHandler;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.RenderIDs;
import de.keridos.floodlights.tileentity.TileEntityGrowLight;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import de.keridos.floodlights.util.RenderUtil;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by Keridos on 28.02.14.
 * This Class is the proxy for the client.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void initRenderers() {
        RenderIDs.ROTATABLE_BLOCK = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderIDs.ROTATABLE_BLOCK, new RotatableBlockRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallFloodlight.class, new TileEntitySmallFoodlightRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrowLight.class, new TileEntityGrowLightRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPhantomLight.class, new TileEntityPhantomLightRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockSmallElectricLight),
                new SmallFloodlightItemRenderer(new TileEntitySmallFoodlightRenderer(), new TileEntitySmallFloodlight()));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockGrowLight),
                new GrowLightItemRenderer(new TileEntityGrowLightRenderer(), new TileEntityGrowLight()));
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