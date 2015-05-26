package de.keridos.floodlights.blocks;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.handler.lighting.LightHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.logging.Logger;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockSmallElectricFloodlight extends BlockFL implements ITileEntityProvider {

    public BlockSmallElectricFloodlight() {
        super(Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT, Material.rock, soundTypeMetal, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).setRedstone(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).setRedstone(false);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).setRedstone(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).setRedstone(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (player.getHeldItem() == null && !world.isRemote && player.isSneaking()) {
            ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).toggleInverted();
            String invert = (((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (player.getHeldItem() != null && !world.isRemote) {
            if (ModCompatibility.BCLoaded || ModCompatibility.EnderIOLoaded) {
                if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolWrench) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                } else if (player.getHeldItem().getItem() instanceof IToolWrench) {
                    ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).toggleRotationState();
                    Logger.getGlobal().info("toggled rotation state");
                    return true;
                }
            }
            if (ModCompatibility.CofhCoreLoaded) {
                if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolHammer) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                } else if (player.getHeldItem().getItem() instanceof IToolHammer) {
                    ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).toggleRotationState();
                    Logger.getGlobal().info("toggled rotation state");
                    return true;
                }
            }
            if (ModCompatibility.IC2Loaded) {
                if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                } else if (player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
                    ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).toggleRotationState();
                    Logger.getGlobal().info("toggled rotation state");
                    return true;
                }
                if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrenchElectric")) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrenchElectric")) {
                    ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).toggleRotationState();
                    Logger.getGlobal().info("toggled rotation state");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TileEntitySmallFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntitySmallFloodlight();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        ForgeDirection direction = ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).getOrientation();
        int mode = ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).getMode();
        LightHandler.getInstance().removeSource(world, x, y, z, direction, mode);
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityFL) {
            if (itemStack.hasDisplayName()) {
                ((TileEntityFL) world.getTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
            }
            ((TileEntityFL) world.getTileEntity(x, y, z)).setOrientation(ForgeDirection.getOrientation(getFacing(entityLiving)));
        }
    }
}
