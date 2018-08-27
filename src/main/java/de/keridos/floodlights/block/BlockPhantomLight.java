package de.keridos.floodlights.block;

import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Keridos on 01.10.14.
 * This Class implements the invisible light block the mod uses to light up areas.
 */
@SuppressWarnings({"WeakerAccess", "deprecation"})
public class BlockPhantomLight extends BlockFL implements ITileEntityProvider {
    public static final PropertyBool UPDATE = PropertyBool.create("update");

    public BlockPhantomLight() {
        super(Names.Blocks.PHANTOM_LIGHT, Material.AIR, SoundType.CLOTH, 0.0F);
    }

    public BlockPhantomLight(String name, Material material, SoundType soundType, float hardness) {
        super(name, material, soundType, hardness);
        setHarvestLevel("pickaxe", 1);
        setDefaultState(this.blockState.getBaseState().withProperty(UPDATE, false));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(UPDATE, (meta == 1));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(UPDATE) ? 1 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UPDATE);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return this.isCollidable();
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public int quantityDropped(Random rand) {
        return 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return 15;
    }

    @Override
    public TileEntityPhantomLight createNewTileEntity(World world, int metadata) {
        return new TileEntityPhantomLight();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote && state.getValue(UPDATE) && (blockIn != Blocks.AIR)) {
            ((TileEntityPhantomLight) worldIn.getTileEntity(pos)).invalidateSources();
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        if (!world.isRemote && blockState.getValue(UPDATE))
            ((TileEntityPhantomLight) world.getTileEntity(pos)).invalidateSources();
        super.breakBlock(world, pos, blockState);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
}
