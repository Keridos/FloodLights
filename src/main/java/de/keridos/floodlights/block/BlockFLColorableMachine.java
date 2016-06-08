package de.keridos.floodlights.block;

import de.keridos.floodlights.tileentity.TileEntityFL;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

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
        if (meta > 7) {
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
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, COLOR);
    }

    /*@Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        if (worldIn.getBlockState(pos).getValue(COLOR) != null) {
            return getColorAsInt(getActualState(worldIn.getBlockState(pos),worldIn,pos).getValue(COLOR));
        }
        return super.colorMultiplier(worldIn, pos, renderPass);
    }    */


}
