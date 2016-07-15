package de.keridos.floodlights.block;

import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.RenderUtil.getColorAsInt;

/**
 * Created by Keridos on 02/02/2016.
 * This Class
 */
public class BlockFLColorableMachine extends BlockFL {
    public static final PropertyInteger COLOR = PropertyInteger.create("color", 0, 16);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockFLColorableMachine(String unlocName, Material material, SoundType type, float hardness) {
        super(unlocName, material, type, hardness);
        setDefaultState(
                this.blockState.getBaseState().withProperty(COLOR, 0).withProperty(ACTIVE, false).withProperty(FACING,
                        EnumFacing.DOWN));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta > 5) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta - 8)).withProperty(ACTIVE,
                    true);
        } else {
            return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta)).withProperty(ACTIVE, false);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 8 * (state.getValue(ACTIVE) ? 1 : 0) + state.getValue(FACING).ordinal();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityFL) {
            return state.withProperty(COLOR, ((TileEntityFL) worldIn.getTileEntity(
                    pos)).getColor());
        } else return state.withProperty(COLOR, 0);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, ACTIVE, COLOR);
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        if (worldIn.getBlockState(pos).getValue(COLOR) != null) {
            return getColorAsInt(getActualState(worldIn.getBlockState(pos),worldIn,pos).getValue(COLOR));
        }
        return super.colorMultiplier(worldIn, pos, renderPass);
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
}
