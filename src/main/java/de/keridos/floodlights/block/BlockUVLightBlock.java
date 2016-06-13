package de.keridos.floodlights.block;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityUVLightBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class BlockUVLightBlock extends BlockPhantomLight {

    public BlockUVLightBlock() {
        super(Names.Blocks.UV_LIGHTBLOCK, Material.AIR, SoundType.CLOTH, 0.0F);
    }

    @Override
    public TileEntityUVLightBlock createNewTileEntity(World world, int metadata) {
        return new TileEntityUVLightBlock();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState state,IBlockAccess world, BlockPos pos, EnumFacing side) {
        return ConfigHandler.uvLightRendered && (!world.getBlockState(pos.offset(side)).getBlock().isOpaqueCube(world.getBlockState(pos.offset(side))) && world.getBlockState(pos.offset(side)).getBlock() != ModBlocks.blockUVLightBlock);
    }

    @Override
    public int getLightValue(IBlockState state) {
        return 4;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
}
