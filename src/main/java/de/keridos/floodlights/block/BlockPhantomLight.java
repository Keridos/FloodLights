package de.keridos.floodlights.block;

import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Keridos on 01.10.14.
 * This Class implements the invisible light block the mod uses to light up areas.
 */
public class BlockPhantomLight extends BlockFL implements ITileEntityProvider {
    public BlockPhantomLight() {
        super(Names.Blocks.PHANTOM_LIGHT, Material.air, soundTypeCloth, 0.0F);
    }

    public BlockPhantomLight(String name, Material material, SoundType soundType, float hardness) {
        super(name, material, soundType, hardness);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
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
    public boolean canProvidePower() {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightValue() {
        return 15;
    }

    @Override
    public TileEntityPhantomLight createNewTileEntity(World world, int metadata) {
        return new TileEntityPhantomLight();
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!(neighborBlock instanceof BlockPhantomLight) && neighborBlock.isAir(worldIn,pos)) { //TODO: rework this to be less laggy
            Logger.getGlobal().info("detected change in neighbour to phantomlight");
            ((TileEntityPhantomLight) worldIn.getTileEntity(pos)).updateAllSources();
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        ((TileEntityPhantomLight) world.getTileEntity(pos)).updateAllSources();
        super.breakBlock(world, pos, blockState);
    }
}
