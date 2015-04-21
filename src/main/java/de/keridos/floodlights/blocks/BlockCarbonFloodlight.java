package de.keridos.floodlights.blocks;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.handler.lighting.LightHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.RenderIDs;
import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static de.keridos.floodlights.util.GeneralUtil.getMinecraftItem;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the carbon floodlight.
 */
public class BlockCarbonFloodlight extends BlockFL implements ITileEntityProvider {

    public BlockCarbonFloodlight(Material material) {
        super(Names.Blocks.CARBON_FLOODLIGHT, Material.rock, soundTypeMetal, 2.5F);
        setCreativeTab(CreativeTabs.tabBlock);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public int getRenderType() {
        return RenderIDs.ROTATABLE_BLOCK;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).setActive(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).setActive(false);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).setActive(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).setActive(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (player.getHeldItem() == null && player.isSneaking() && !world.isRemote) {
            ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).toggleInverted();
            String invert = (((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (player.getHeldItem() != null && !world.isRemote) {
            if (!ModCompatibility.WrenchAvailable && player.getHeldItem().getItem() == getMinecraftItem("stick")) {
                ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).changeMode(player);
            }
            if (ModCompatibility.BCLoaded || ModCompatibility.EnderIOLoaded) {
                if (!player.isSneaking() && player.getHeldItem().getItem() instanceof IToolWrench) {
                    ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolWrench) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
            }
            if (ModCompatibility.CofhCoreLoaded) {
                if (!player.isSneaking() && player.getHeldItem().getItem() instanceof IToolHammer) {
                    ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolHammer) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
            }
            if (ModCompatibility.IC2Loaded) {
                if (!player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
                    ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
                if (!player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrenchElectric")) {
                    ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrenchElectric")) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
            }
        }
        if (!world.isRemote) {
            player.openGui(FloodLights.instance, 0, world, x, y, z);
        }
        return true;
    }

    @Override
    public TileEntityCarbonFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntityCarbonFloodlight();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        ForgeDirection direction = ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).getOrientation();
        int mode = ((TileEntityCarbonFloodlight) world.getTileEntity(x, y, z)).getMode();
        LightHandler.getInstance().removeSource(world, x, y, z, direction, mode);
        super.breakBlock(world, x, y, z, block, par6);
    }
}
