package de.keridos.floodlights.blocks;

import de.keridos.floodlights.core.LightHandler;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


/**
 * Created by Nico on 01/10/2014.
 */
public class BlockElectricFloodlight extends BlockFL implements ITileEntityProvider {

    public BlockElectricFloodlight(Material material) {
        super("electricFloodlight", Material.rock, soundTypeMetal, 0.6F);
        setCreativeTab(CreativeTabs.tabBlock);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }


    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityElectricFloodlight) world.getTileEntity(x, y, z)).setActive(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityElectricFloodlight) world.getTileEntity(x, y, z)).setActive(false);
            }
        }
    }

    @Override
    public TileEntityElectricFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntityElectricFloodlight();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        ForgeDirection direction = ((TileEntityElectricFloodlight) world.getTileEntity(x, y, z)).getOrientation();
        LightHandler.getInstance().removeSource(world, x, y, z, direction, 0);
        super.breakBlock(world, x, y, z, block, par6);
    }
}
