package de.keridos.floodlights.block;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.RenderIDs;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityUVLight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class BlockUVLight extends BlockFL implements ITileEntityProvider {

    public BlockUVLight() {
        super(Names.Blocks.UV_FLOODLIGHT, Material.rock, soundTypeMetal, 2.5F);
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
    public TileEntityUVLight createNewTileEntity(World world, int metadata) {
        return new TileEntityUVLight();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityUVLight) world.getTileEntity(x, y, z)).setRedstone(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityUVLight) world.getTileEntity(x, y, z)).setRedstone(false);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityUVLight) world.getTileEntity(x, y, z)).setRedstone(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityUVLight) world.getTileEntity(x, y, z)).setRedstone(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (player.getHeldItem() == null && !world.isRemote && player.isSneaking()) {
            ((TileEntityUVLight) world.getTileEntity(x, y, z)).toggleInverted();
            String invert = (((TileEntityUVLight) world.getTileEntity(x, y, z)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && player.getHeldItem() != null) {
            if (ModCompatibility.BCLoaded || ModCompatibility.EnderIOLoaded) {
                if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolWrench) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
            }
            if (ModCompatibility.CofhCoreLoaded) {
                if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolHammer) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
            }
            if (ModCompatibility.IC2Loaded) {
                if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
                if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrenchElectric")) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
            }
            if (player.getHeldItem().getItem() == Items.dye) {
                ((TileEntityFL) world.getTileEntity(x, y, z)).setColor(15 - player.getHeldItem().getItemDamage());
                return true;
            } else if (player.getHeldItem().getItem() == Item.getItemFromBlock(Blocks.wool) && !player.isSneaking()) {
                ((TileEntityFL) world.getTileEntity(x, y, z)).setColor(16);
                return true;
            }
        }
        if (!world.isRemote) {
            player.openGui(FloodLights.instance, 1, world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        ((TileEntityUVLight) world.getTileEntity(x, y, z)).UVSource(true);
        super.breakBlock(world, x, y, z, block, par6);
    }
}
