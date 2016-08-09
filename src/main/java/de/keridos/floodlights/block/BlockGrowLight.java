package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityGrowLight;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockGrowLight extends BlockFLColorableMachine {
    public static final PropertyBool LIGHT = PropertyBool.create("light");

    public BlockGrowLight() {
        super(Names.Blocks.GROW_LIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
        setDefaultState(
                this.blockState.getBaseState().withProperty(COLOR, 0).withProperty(ACTIVE, false).withProperty(FACING,
                        EnumFacing.DOWN).withProperty(LIGHT, true));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, COLOR, LIGHT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LIGHT, meta == 0);

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(LIGHT) ? 0 : 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.MAIN_HAND) {
            if (!world.isRemote && heldItem == null && player.isSneaking()) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleInverted();
                String invert = (((TileEntityMetaFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
                player.addChatMessage(new TextComponentString(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
                return true;
            } else if (!world.isRemote && heldItem != null) {
                if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
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
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World w, @Nonnull IBlockState state) {
        return new TileEntityGrowLight();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return this.getBoundingBox(state, world, pos);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileEntityGrowLight) {
            return this.getBoundingBox(state, world, pos).offset(pos);
        }
        return super.getSelectedBoundingBox(state, world, pos);
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

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(ACTIVE) && state.getValue(LIGHT) ? 15 : 0;
    }
}
