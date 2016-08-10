package de.keridos.floodlights.block;

import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        if (unlocName != Names.Blocks.GROW_LIGHT)
            setDefaultState(this.blockState.getBaseState().withProperty(COLOR, 0).withProperty(ACTIVE, false).withProperty(FACING, EnumFacing.DOWN));
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
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, COLOR);
    }

    @Override
    public void neighborChanged(IBlockState blockState, World world, BlockPos pos, Block neighborBlock) {
        if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityMetaFloodlight) {
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
        if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityMetaFloodlight) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
        }
    }
}
