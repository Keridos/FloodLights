package de.keridos.floodlights.block;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityUVLightBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class BlockUVLightBlock extends BlockPhantomLight {

    public BlockUVLightBlock() {
        super(Names.Blocks.UV_LIGHTBLOCK, Material.glass, soundTypeCloth, 0.0F);
    }

    @Override
    public TileEntityUVLightBlock createNewTileEntity(World world, int metadata) {
        return new TileEntityUVLightBlock();
    }

    @Override
    public int getRenderType() {
        return 0;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return ConfigHandler.uvLightRendered && (side.getIndex() == 0 || (!world.getBlockState(pos).getBlock().isOpaqueCube() && world.getBlockState(pos).getBlock() != ModBlocks.blockUVLightBlock));
    }

    @Override
    public int getLightValue() {
        return 4;
    }
}
