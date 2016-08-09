package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static de.keridos.floodlights.util.GeneralUtil.getMinecraftItem;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockElectricFloodlight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockElectricFloodlight() {
        super(Names.Blocks.ELECTRIC_FLOODLIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.MAIN_HAND) {
            if (!world.isRemote && heldItem == null && player.isSneaking()) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleInverted();
                String invert = (((TileEntityMetaFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
                player.addChatMessage(new TextComponentString(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
                return true;
            } else if (!world.isRemote && heldItem != null) {
                if (heldItem.getItem() == getMinecraftItem("stick")) {
                    ((TileEntityElectricFloodlight) world.getTileEntity(pos)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                    world.destroyBlock(pos, true);
                    return true;
                } else if (ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                    ((TileEntityElectricFloodlight) world.getTileEntity(pos)).changeMode(player);
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
        return true;
    }

    @Override
    public TileEntityElectricFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntityElectricFloodlight();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        ((TileEntityElectricFloodlight) world.getTileEntity(pos)).removeSource(-1);
        super.breakBlock(world, pos, blockState);
    }
}
