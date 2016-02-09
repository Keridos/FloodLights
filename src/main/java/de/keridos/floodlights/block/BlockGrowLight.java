package de.keridos.floodlights.block;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.api.tool.ITool;
import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityGrowLight;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import de.keridos.floodlights.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockGrowLight extends BlockFL implements ITileEntityProvider {

    public BlockGrowLight() {
        super(Names.Blocks.GROW_LIGHT, Material.rock, soundTypeMetal, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityMetaFloodlight) world.getTileEntity(x, y, z)).setRedstone(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityMetaFloodlight) world.getTileEntity(x, y, z)).setRedstone(false);
            }
            if (!(block instanceof BlockFL) && block != Blocks.air) {
                ((TileEntityMetaFloodlight) world.getTileEntity(x, y, z)).toggleUpdateRun();
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityMetaFloodlight) world.getTileEntity(x, y, z)).setRedstone(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityMetaFloodlight) world.getTileEntity(x, y, z)).setRedstone(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (!world.isRemote && player.getHeldItem() == null && player.isSneaking()) {
            ((TileEntityMetaFloodlight) world.getTileEntity(x, y, z)).toggleInverted();
            String invert = (((TileEntityMetaFloodlight) world.getTileEntity(x, y, z)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && player.getHeldItem() != null) {
            if (ModCompatibility.BCLoaded) {
                if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolWrench) {
                    world.func_147480_a(x, y, z, true);
                    return true;
                }
            }
            if (ModCompatibility.EnderIOLoaded) {
                if (player.isSneaking() && player.getHeldItem().getItem() instanceof ITool) {
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
    public TileEntityGrowLight createNewTileEntity(World world, int metadata) {
        return new TileEntityGrowLight();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }


    //
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        TileEntityGrowLight tileEntityGrowLight = (TileEntityGrowLight) world.getTileEntity(x, y, z);

        this.minX = -0.5;
        this.minY = -0.5;
        this.minZ = -0.5;
        this.maxX = 0.1875 - 0.5;
        this.maxY = 0.5;
        this.maxZ = 0.5;
        double[] newMinTemp = MathUtil.rotateD(minX, minY, minZ, tileEntityGrowLight.getOrientation());
        double[] newMaxTemp = MathUtil.rotateD(maxX, maxY, maxZ, tileEntityGrowLight.getOrientation());
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
        ((TileEntityGrowLight) world.getTileEntity(x, y, z)).growSource(true);
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
