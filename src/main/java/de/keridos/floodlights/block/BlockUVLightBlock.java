package de.keridos.floodlights.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityUVLightBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class BlockUVLightBlock extends BlockPhantomLight {

    public BlockUVLightBlock() {
        super(Names.Blocks.UV_LIGHTBLOCK, Material.rock, soundTypeMetal, 2.5F);
    }

    @Override
    public TileEntityUVLightBlock createNewTileEntity(World world, int metadata) {
        return new TileEntityUVLightBlock();
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
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public int getLightValue() {
        return 4;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        ((TileEntityUVLightBlock) world.getTileEntity(x, y, z)).updateAllSources();
        super.breakBlock(world, x, y, z, block, par6);
    }
}
