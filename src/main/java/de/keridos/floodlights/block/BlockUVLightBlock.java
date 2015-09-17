package de.keridos.floodlights.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityUVLightBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
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
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        topIcon = iconRegister.registerIcon(getUnwrappedUnlocalizedName(this.getUnlocalizedName()));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return topIcon;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        return ConfigHandler.uvLightRendered && (side == 0 || (!world.getBlock(x, y, z).isOpaqueCube() && world.getBlock(x, y, z) != ModBlocks.blockUVLightBlock));
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getLightValue() {
        return 4;
    }
}
