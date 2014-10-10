package de.keridos.floodlights.blocks;

import de.keridos.floodlights.handler.lighting.LightHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.RenderIDs;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockElectricFloodlight extends BlockFL implements ITileEntityProvider {

    public BlockElectricFloodlight(Material material) {
        super(Names.Blocks.ELECTRIC_FLOODLIGHT, Material.rock, soundTypeMetal, 2.5F);
        setCreativeTab(CreativeTabs.tabBlock);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public int getRenderType() {
        return RenderIDs.ROTATABLE_BLOCK;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
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
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityElectricFloodlight) world.getTileEntity(x, y, z)).setActive(true);
            } else if (!world.isBlockIndirectlyGettingPowered(x, y, z)) {
                ((TileEntityElectricFloodlight) world.getTileEntity(x, y, z)).setActive(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (player.getHeldItem() == null && !world.isRemote && player.isSneaking()) {
            ((TileEntityElectricFloodlight) world.getTileEntity(x, y, z)).toggleInverted();
            boolean b = ((TileEntityElectricFloodlight) world.getTileEntity(x, y, z)).getInverted();
            player.addChatMessage(new ChatComponentText("Light now: " + (b ? "inverted" : "not inverted")));
        }
        return false;
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
