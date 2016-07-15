package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityUVLight;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class BlockUVLight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockUVLight() {
        super(Names.Blocks.UV_FLOODLIGHT, Material.rock, soundTypeMetal, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public TileEntityUVLight createNewTileEntity(World world, int metadata) {
        return new TileEntityUVLight();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumFacing side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (player.getHeldItem() == null && !world.isRemote && player.isSneaking()) {
            ((TileEntityUVLight) world.getTileEntity(pos)).toggleInverted();
            String invert = (((TileEntityUVLight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && player.getHeldItem() != null) {
            if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(player.getHeldItem())) {
                world.destroyBlock(pos, true);
                return true;
            } else if (player.getHeldItem().getItem() == Items.dye) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(15 - player.getHeldItem().getItemDamage());
                return true;
            }
        }
        if (!world.isRemote) {
            player.openGui(FloodLights.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        ((TileEntityUVLight) world.getTileEntity(pos)).UVSource(true);
        super.breakBlock(world, pos, blockState);
    }
}
