package de.keridos.floodlights.block;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.handler.lighting.LightHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import de.keridos.floodlights.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
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
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
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
        if (!world.isRemote && player.getHeldItem() == null && player.isSneaking()) {
            ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).toggleInverted();
            String invert = (((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && player.getHeldItem() != null) {
            if (ModCompatibility.BCLoaded || ModCompatibility.EnderIOLoaded) {
                if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolWrench) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                } else if (player.getHeldItem().getItem() instanceof IToolWrench) {
                    ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).toggleRotationState();
                    return true;
                }
            }
            if (ModCompatibility.CofhCoreLoaded) {
                if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolHammer) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                } else if (player.getHeldItem().getItem() instanceof IToolHammer) {
                    ((TileEntitySmallFloodlight) world.getTileEntity(x, y, z)).toggleRotationState();
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
        }
        return false;
    }

    @Override
    public TileEntitySmallFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntitySmallFloodlight();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }


    //
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        TileEntitySmallFloodlight tileEntitySmallFloodlight = (TileEntitySmallFloodlight) world.getTileEntity(x, y, z);
        switch (world.getBlockMetadata(x, y, z)) {
            case 0:
                if (!tileEntitySmallFloodlight.getRotationState()) {
                    this.minX = 0;
                    this.maxX = 0.1875;
                    this.minY = 0.3125;
                    this.maxY = 0.6875;
                    this.minZ = 0;
                    this.maxZ = 1;
                } else {
                    this.minX = 0;
                    this.maxX = 0.1875;
                    this.minY = 0;
                    this.maxY = 1;
                    this.minZ = 0.3125;
                    this.maxZ = 0.6875;
                }
                break;
            case 1:
                this.minX = 0;
                this.maxX = 0.1875;
                this.minY = 0;
                this.maxY = 1;
                this.minZ = 0;
                this.maxZ = 1;
                break;
        }
        this.minX = this.minX - 0.5;
        this.minY = this.minY - 0.5;
        this.minZ = this.minZ - 0.5;
        this.maxX = this.maxX - 0.5;
        this.maxY = this.maxY - 0.5;
        this.maxZ = this.maxZ - 0.5;
        double[] newMinTemp = MathUtil.rotateD(minX, minY, minZ, tileEntitySmallFloodlight.getOrientation());
        double[] newMaxTemp = MathUtil.rotateD(maxX, maxY, maxZ, tileEntitySmallFloodlight.getOrientation());
        double[] newMax = MathUtil.sortMinMaxToMax(newMinTemp, newMaxTemp);
        double[] newMin = MathUtil.sortMinMaxToMin(newMinTemp, newMaxTemp);
        this.minX = newMin[0] + 0.5;
        this.minY = newMin[1] + 0.5;
        this.minZ = newMin[2] + 0.5;
        this.maxX = newMax[0] + 0.5;
        this.maxY = newMax[1] + 0.5;
        this.maxZ = newMax[2] + 0.5;

        return AxisAlignedBB.getBoundingBox((double) x + this.minX, (double) y + this.minY, (double) z + this.minZ, (double) x + this.maxX, (double) y + this.maxY, (double) z + this.maxZ);
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

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(0, new ItemStack(ModBlocks.blockSmallElectricLight, 1, metadata));
        return drops;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
        for (int i = 0; i < 2; i++) {
            subItems.add(new ItemStack(ModBlocks.blockSmallElectricLight, 1, i));
        }
    }
}
