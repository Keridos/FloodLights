package de.keridos.floodlights.core.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import de.keridos.floodlights.client.render.block.PhantomLightRenderer;
import de.keridos.floodlights.client.render.block.RotatableBlockRenderer;
import de.keridos.floodlights.client.render.block.TileEntitySmallFoodlightRenderer;
import de.keridos.floodlights.client.render.item.SmallFloodlightItemRenderer;
import de.keridos.floodlights.compatability.IGWHandler;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.RenderIDs;
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
        RenderIDs.PHANTOM_LIGHT = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderIDs.ROTATABLE_BLOCK, new RotatableBlockRenderer());
        RenderingRegistry.registerBlockHandler(RenderIDs.PHANTOM_LIGHT, new PhantomLightRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallFloodlight.class, new TileEntitySmallFoodlightRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockSmallElectricLight),
                new SmallFloodlightItemRenderer(new TileEntitySmallFoodlightRenderer(), new TileEntitySmallFloodlight()));
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