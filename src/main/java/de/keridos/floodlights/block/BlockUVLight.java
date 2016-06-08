package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import de.keridos.floodlights.tileentity.TileEntityUVLight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class BlockUVLight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockUVLight() {
        super(Names.Blocks.UV_FLOODLIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return true;
    }

    @Override
    public TileEntityUVLight createNewTileEntity(World world, int metadata) {
        return new TileEntityUVLight();
    }

    @Override
    public void neighborChanged(IBlockState blockState, World world, BlockPos pos, Block block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityUVLight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityUVLight) world.getTileEntity(pos)).setRedstone(false);
            }
            if (!(block instanceof BlockFL) && block != Blocks.AIR) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleUpdateRun();
            }
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityUVLight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityUVLight) world.getTileEntity(pos)).setRedstone(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && heldItem == null && player.isSneaking()) {
            ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleInverted();
            String invert = (((TileEntityMetaFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new TextComponentString(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && heldItem != null) {
            if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                world.destroyBlock(pos, true);
                return true;
            } else if (heldItem.getItem() == Items.DYE) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(15 - heldItem.getItemDamage());
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
