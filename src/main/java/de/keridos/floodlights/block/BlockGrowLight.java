package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityGrowLight;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static de.keridos.floodlights.util.GeneralUtil.getMinecraftItem;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockGrowLight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockGrowLight() {
        super(Names.Blocks.GROW_LIGHT, Material.rock, soundTypeMetal, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState blockState, Block block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
            if (!(block instanceof BlockFL) && block != Blocks.air) {
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumFacing side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (!world.isRemote && player.getHeldItem() == null && player.isSneaking()) {
            ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleInverted();
            String invert = (((TileEntityMetaFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && player.getHeldItem() != null) {
            if (!ModCompatibility.WrenchAvailable && player.getHeldItem().getItem() == getMinecraftItem("stick")) {
                ((TileEntityGrowLight) world.getTileEntity(pos)).changeMode(player);
                return true;
            } else if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(player.getHeldItem())) {
                world.destroyBlock(pos, true);
                return true;
            } else if (ModCompatibility.getInstance().isItemValidWrench(player.getHeldItem())) {
                ((TileEntityGrowLight) world.getTileEntity(pos)).changeMode(player);
                return true;
            } else if (player.getHeldItem().getItem() == Items.dye) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(15 - player.getHeldItem().getItemDamage());
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
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
        //getSelectedBoundingBox(World world, BlockPos pos);
            return null;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos) {

            this.minX = 0;
            this.minY = 0.8125;
            this.minZ = 0;
            this.maxX = 1;
            this.maxY = 1;
            this.maxZ = 1;

            return new AxisAlignedBB((double) pos.getX() + this.minX,
                    (double) pos.getY() + this.minY,
                    (double) pos.getZ() + this.minZ,
                    (double) pos.getX() + this.maxX,
                    (double) pos.getY() + this.maxY,
                    (double) pos.getZ() + this.maxZ);

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
