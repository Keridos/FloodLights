package de.keridos.floodlights.block;

import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import de.keridos.floodlights.tileentity.TileEntityPhantomLight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Keridos on 01.10.14.
 * This Class implements the invislbe light block the mod uses to light up areas.
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canCollideCheck(int par1, boolean par2) {
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
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        ((TileEntityPhantomLight) world.getTileEntity(x, y, z)).updateAllSources();
        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        ((TileEntityPhantomLight) world.getTileEntity(x, y, z)).updateAllSources();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    int newX = x + i;
                    int newY = y + j;
                    int newZ = z + k;
                    TileEntity te = world.getTileEntity(newX, newY, newZ);
                    if (te != null && te instanceof TileEntityPhantomLight) {
                        ((TileEntityPhantomLight) te).updateAllSources();
                    } else if (te != null && te instanceof TileEntityMetaFloodlight) {
                        ((TileEntityMetaFloodlight) te).toggleUpdateRun();
                    }
                }
            }
        }
        super.breakBlock(world, x, y, z, block, par6);
    }
}
