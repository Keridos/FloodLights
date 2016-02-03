package de.keridos.floodlights.block;

import de.keridos.floodlights.tileentity.TileEntityFL;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
                this.blockState.getBaseState().withProperty(COLOR, 16).withProperty(ACTIVE, false).withProperty(FACING,
                                                                                                                EnumFacing.DOWN));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta > 5) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta - 6)).withProperty(ACTIVE,
                                                                                                           true);
        } else {
            return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta)).withProperty(ACTIVE, false);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 6 * (state.getValue(ACTIVE) ? 1 : 0) + state.getValue(FACING).ordinal();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityFL) {
            return super.getActualState(state, worldIn, pos).withProperty(COLOR, ((TileEntityFL) worldIn.getTileEntity(
                    pos)).getColor());
        } else return super.getActualState(state, worldIn, pos).withProperty(COLOR, 16);
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING,ACTIVE,COLOR);
    }
}
