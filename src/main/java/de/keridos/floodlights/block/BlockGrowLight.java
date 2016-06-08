package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static de.keridos.floodlights.util.GeneralUtil.getMinecraftItem;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockGrowLight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockGrowLight() {
        super(Names.Blocks.GROW_LIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public void neighborChanged(IBlockState blockState,World world, BlockPos pos,  Block block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
            if (!(block instanceof BlockFL) && block == Blocks.AIR) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleUpdateRun();
            }
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
        }
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySmallFloodlight) {
            TileEntitySmallFloodlight te = ((TileEntitySmallFloodlight) worldIn.getTileEntity(pos));
            return state.withProperty(ACTIVE, te.getWasActive());
        } else return state.withProperty(COLOR, 16);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && heldItem == null && player.isSneaking()) {
            ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleInverted();
            String invert = (((TileEntityMetaFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new TextComponentString(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && heldItem != null) {
            if (!ModCompatibility.WrenchAvailable && heldItem.getItem() == getMinecraftItem("stick")) {
                ((TileEntityGrowLight) world.getTileEntity(pos)).changeMode(player);
                return true;
            } else if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                world.destroyBlock(pos, true);
                return true;
            } else if (ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                ((TileEntityGrowLight) world.getTileEntity(pos)).changeMode(player);
                return true;
            } else if (heldItem.getItem() == Items.DYE) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(15 - heldItem.getItemDamage());
                return true;
            }
        }
        if (!world.isRemote) {
            player.openGui(FloodLights.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        } else {
            return true;
        }
    }

    @Override
    public TileEntityGrowLight createNewTileEntity(World world, int metadata) {
        return new TileEntityGrowLight();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        //getSelectedBoundingBox(World world, BlockPos pos);
        return null;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state,World world, BlockPos pos) {

        double minX = 0;
        double minY = 0.8125;
        double minZ = 0;
        double maxX = 1;
        double maxY = 1;
        double maxZ = 1;

        return new AxisAlignedBB((double) pos.getX() + minX,
                (double) pos.getY() + minY,
                (double) pos.getZ() + minZ,
                (double) pos.getX() + maxX,
                (double) pos.getY() + maxY,
                (double) pos.getZ() + maxZ);

    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        ((TileEntityGrowLight) world.getTileEntity(pos)).growSource(true);
        super.breakBlock(world, pos, blockState);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.getTileEntity(pos) instanceof TileEntityFL) {
            if (stack.hasDisplayName()) {
                ((TileEntityFL) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
            }
            ((TileEntityFL) world.getTileEntity(pos)).setOrientation(EnumFacing.DOWN);
        }
    }
}
