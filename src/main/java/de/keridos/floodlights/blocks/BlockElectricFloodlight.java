package de.keridos.floodlights.blocks;

import de.keridos.floodlights.core.LightHandler;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


/**
 * Created by Nico on 01/10/2014.
 */
public class BlockElectricFloodlight extends BlockFL implements ITileEntityProvider {

    public BlockElectricFloodlight(Material material) {
        super("blockElectricFloodlight", Material.rock, soundTypeMetal, 0.6F);
        setCreativeTab(CreativeTabs.tabBlock);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        LightHandler LightHandlerInst = LightHandler.getInstance();
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                LightHandlerInst.addSource(world, x, y, z, ForgeDirection.EAST, 0);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                LightHandlerInst.removeSource(world, x, y, z, ForgeDirection.EAST, 0);
            }
            LightHandlerInst.updateLights();
        }
    }

    @Override
    public TileEntityElectricFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntityElectricFloodlight();
    }
}
