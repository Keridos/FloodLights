package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.block.properties.BlockProperties;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import de.keridos.floodlights.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public static final PropertyEnum MODEL = PropertyEnum.create("model", BlockProperties.EnumModel.class);

    public BlockSmallElectricFloodlight() {
        super(Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT, Material.rock, soundTypeMetal, 2.5F);
        setDefaultState(
                this.blockState.getBaseState().withProperty(COLOR, 0).withProperty(ACTIVE, false).withProperty(FACING,
                        EnumFacing.DOWN).withProperty(MODEL, BlockProperties.EnumModel.values()[0]).withProperty(ROTATIONSTATE, false));
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
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).setRedstone(false);
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
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MODEL, BlockProperties.EnumModel.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (((BlockProperties.EnumModel) state.getValue(MODEL)).getID());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySmallFloodlight) {
            TileEntitySmallFloodlight te = ((TileEntitySmallFloodlight) worldIn.getTileEntity(pos));
            return state.withProperty(COLOR, te.getColor()).withProperty(FACING, te.getOrientation()).withProperty(ROTATIONSTATE, te.getRotationState()).withProperty(ACTIVE, te.getWasActive());
        } else return state.withProperty(COLOR, 16);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, ACTIVE, COLOR, ROTATIONSTATE, MODEL);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumFacing side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (!world.isRemote && player.getHeldItem() == null && player.isSneaking()) {
            ((TileEntitySmallFloodlight) world.getTileEntity(pos)).toggleInverted();
            String invert = (((TileEntitySmallFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && player.getHeldItem() != null) {
            if (!ModCompatibility.WrenchAvailable && player.getHeldItem().getItem() == getMinecraftItem("stick")) {
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).toggleRotationState();
                return true;
            } else if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(player.getHeldItem())) {
                world.destroyBlock(pos, true);
                return true;
            } else if (ModCompatibility.getInstance().isItemValidWrench(player.getHeldItem())) {
                ((TileEntitySmallFloodlight) world.getTileEntity(pos)).toggleRotationState();
                return true;
            } else if (player.getHeldItem().getItem() == Items.dye) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(15 - player.getHeldItem().getItemDamage());
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
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileEntitySmallFloodlight) {
            TileEntitySmallFloodlight tileEntitySmallFloodlight = (TileEntitySmallFloodlight) world.getTileEntity(pos);
            switch (this.getMetaFromState(world.getBlockState(pos))) {
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

            return new AxisAlignedBB((double) pos.getX() + this.minX,
                    (double) pos.getY() + this.minY,
                    (double) pos.getZ() + this.minZ,
                    (double) pos.getX() + this.maxX,
                    (double) pos.getY() + this.maxY,
                    (double) pos.getZ() + this.maxZ);
        }
        return super.getSelectedBoundingBox(world,pos);
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
    public boolean isFullCube() {
        return false;
    }



}
