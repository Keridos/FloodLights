package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.block.properties.PropertiesEnum;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import de.keridos.floodlights.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import java.util.ArrayList;
import java.util.List;

import static de.keridos.floodlights.util.GeneralUtil.getMinecraftItem;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockSmallElectricFloodlight extends BlockFLColorableMachine implements ITileEntityProvider {
    public static final PropertyBool ROTATIONSTATE = PropertyBool.create("rotationstate");
    public static final PropertyEnum MODEL = PropertyEnum.create("model", PropertiesEnum.EnumModel.class);

    public BlockSmallElectricFloodlight() {
        super(Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setDefaultState(
                this.blockState.getBaseState().withProperty(COLOR, 0).withProperty(ACTIVE, false).withProperty(FACING,
                        EnumFacing.DOWN).withProperty(MODEL, PropertiesEnum.EnumModel.values()[0]).withProperty(ROTATIONSTATE, false));
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }


    @Override
    public void neighborChanged(IBlockState blockState, World world, BlockPos pos, Block neighborBlock) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
            if (!(neighborBlock instanceof BlockFL) && neighborBlock != Blocks.AIR) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleUpdateRun();
            }
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MODEL, PropertiesEnum.EnumModel.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (((PropertiesEnum.EnumModel) state.getValue(MODEL)).getID());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySmallFloodlight) {
            TileEntitySmallFloodlight te = ((TileEntitySmallFloodlight) worldIn.getTileEntity(pos));
            return state.withProperty(COLOR, te.getColor()).withProperty(FACING, te.getOrientation()).withProperty(ROTATIONSTATE, te.getRotationState()).withProperty(ACTIVE, te.getWasActive());
        } else return state.withProperty(COLOR, 16);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, COLOR, ROTATIONSTATE, MODEL);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && heldItem == null && player.isSneaking()) {
            ((TileEntitySmallFloodlight) world.getTileEntity(pos)).toggleInverted();
            String invert = (((TileEntitySmallFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new TextComponentString(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && heldItem != null) {
            if (!ModCompatibility.WrenchAvailable && heldItem.getItem() == getMinecraftItem("stick")) {
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).toggleRotationState();
                return true;
            } else if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                world.destroyBlock(pos, true);
                return true;
            } else if (ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).toggleRotationState();
                return true;
            } else if (heldItem.getItem() == Items.DYE) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(15 - heldItem.getItemDamage());
                return true;
            }
        }
        if (!world.isRemote) {
            player.openGui(FloodLights.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public TileEntitySmallFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntitySmallFloodlight();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState,World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileEntitySmallFloodlight) {
            TileEntitySmallFloodlight tileEntitySmallFloodlight = (TileEntitySmallFloodlight) world.getTileEntity(pos);
            double minX = 0;
            double minY = 0;
            double minZ = 0;
            double maxX = 0;
            double maxY = 0;
            double maxZ = 0;
            switch (this.getMetaFromState(world.getBlockState(pos))) {
                case 0:
                    if (!tileEntitySmallFloodlight.getRotationState()) {
                        minX = 0;
                        maxX = 0.1875;
                        minY = 0.3125;
                        maxY = 0.6875;
                        minZ = 0;
                        maxZ = 1;
                    } else {
                        minX = 0;
                        maxX = 0.1875;
                        minY = 0;
                        maxY = 1;
                        minZ = 0.3125;
                        maxZ = 0.6875;
                    }
                    break;
                case 1:
                    minX = 0;
                    maxX = 0.1875;
                    minY = 0;
                    maxY = 1;
                    minZ = 0;
                    maxZ = 1;
                    break;
            }
            minX = minX - 0.5;
            minY = minY - 0.5;
            minZ = minZ - 0.5;
            maxX = maxX - 0.5;
            maxY = maxY - 0.5;
            maxZ = maxZ - 0.5;
            double[] newMinTemp = MathUtil.rotateD(minX, minY, minZ, tileEntitySmallFloodlight.getOrientation());
            double[] newMaxTemp = MathUtil.rotateD(maxX, maxY, maxZ, tileEntitySmallFloodlight.getOrientation());
            double[] newMax = MathUtil.sortMinMaxToMax(newMinTemp, newMaxTemp);
            double[] newMin = MathUtil.sortMinMaxToMin(newMinTemp, newMaxTemp);
            minX = newMin[0] + 0.5;
            minY = newMin[1] + 0.5;
            minZ = newMin[2] + 0.5;
            maxX = newMax[0] + 0.5;
            maxY = newMax[1] + 0.5;
            maxZ = newMax[2] + 0.5;

            return new AxisAlignedBB((double) pos.getX() + minX,
                    (double) pos.getY() + minY,
                    (double) pos.getZ() + minZ,
                    (double) pos.getX() + maxX,
                    (double) pos.getY() + maxY,
                    (double) pos.getZ() + maxZ);
        }
        return super.getSelectedBoundingBox(state,world, pos);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        ((TileEntitySmallFloodlight) world.getTileEntity(pos)).smallSource(true);
        super.breakBlock(world, pos, blockState);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.getTileEntity(pos) instanceof TileEntityFL) {
            if (stack.hasDisplayName()) {
                ((TileEntityFL) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
            }
            ((TileEntityFL) world.getTileEntity(pos)).setOrientation(
                    getFacing(placer));
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(0, new ItemStack(ModBlocks.blockSmallElectricLight, 1, this.getMetaFromState(state)));
        return drops;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.damageDropped(state);
    }


    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
        for (int i = 0; i < 2; i++) {
            subItems.add(new ItemStack(ModBlocks.blockSmallElectricLight, 1, i));
        }
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


}
