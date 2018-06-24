package de.keridos.floodlights.core.proxy;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.block.BlockPhantomLight;
import de.keridos.floodlights.block.BlockSmallElectricFloodlight;
import de.keridos.floodlights.client.render.block.TileEntityPhantomLightRenderer;
import de.keridos.floodlights.compatability.IGWHandler;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.init.ModItems;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import de.keridos.floodlights.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static de.keridos.floodlights.util.RenderUtil.getColorAsInt;

/**
 * Created by Keridos on 28.02.14.
 * This Class is the proxy for the client.
 */
public class ClientProxy extends CommonProxy {
    private static final Minecraft minecraft = Minecraft.getMinecraft();

    public void registerItemModel(final Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString()));
    }

    public void registerItemModel(final Item item, int meta, final String variantName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(item.getRegistryName().toString().toLowerCase()), variantName));
    }

    public void registerBlockModelAsItem(final Block block, int meta, final String blockName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

    public void registerBlockModelAsItem(final Block block, int meta, final String blockName, String variantName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), variantName));
    }

    @Override
    public void initRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPhantomLight.class, new TileEntityPhantomLightRenderer());
        RenderUtil.setupColors();
        minecraft.getBlockColors().registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int renderPass) {
                return getColorAsInt(state.getValue(BlockFLColorableMachine.COLOR));
            }
        }, ModBlocks.blockCarbonFloodlight, ModBlocks.blockElectricFloodlight/*, ModBlocks.blockSmallElectricLight*/);
    }

    @Override
    public void registerModels() {
        registerBlockModelAsItem(ModBlocks.blockElectricFloodlight, 0, Names.Blocks.ELECTRIC_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockCarbonFloodlight, 0, Names.Blocks.CARBON_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockUVFloodlight, 0, Names.Blocks.UV_FLOODLIGHT);
        registerBlockModelAsItem(ModBlocks.blockGrowLight, 0, Names.Blocks.GROW_LIGHT);
        registerBlockModelAsItem(ModBlocks.blockSmallElectricLight, 0, Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT, "inventory_strip");
        registerBlockModelAsItem(ModBlocks.blockSmallElectricLight, 1, Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT, "inventory_square");
        registerItemModel(ModItems.carbonDissolver, 0);
        registerItemModel(ModItems.carbonLantern, 0);
        registerItemModel(ModItems.glowingFilament, 0);
        registerItemModel(ModItems.lightBulb, 0);
        registerItemModel(ModItems.lightDebugTool, 0);
        registerItemModel(ModItems.mantle, 0);
        registerItemModel(ModItems.rawFilament, 0);
        StateMap ignoreColor = new StateMap.Builder().ignore(BlockFLColorableMachine.COLOR).build();
        ModelLoader.setCustomStateMapper(ModBlocks.blockCarbonFloodlight, ignoreColor);
        ModelLoader.setCustomStateMapper(ModBlocks.blockElectricFloodlight, ignoreColor);
        ModelLoader.setCustomStateMapper(ModBlocks.blockUVFloodlight, ignoreColor);
        ModelLoader.setCustomStateMapper(ModBlocks.blockSmallElectricLight, new StateMap.Builder().withName(BlockSmallElectricFloodlight.MODEL).ignore(BlockFLColorableMachine.COLOR).build());
        ModelLoader.setCustomStateMapper(ModBlocks.blockGrowLight, new StateMap.Builder().ignore(BlockFLColorableMachine.COLOR).ignore(BlockFLColorableMachine.FACING).build());
        ModelLoader.setCustomStateMapper(ModBlocks.blockPhantomLight, new StateMap.Builder().ignore(BlockPhantomLight.UPDATE).build());
        ModelLoader.setCustomStateMapper(ModBlocks.blockPhantomUVLight, new StateMap.Builder().ignore(BlockPhantomLight.UPDATE).build());
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