package de.keridos.floodlights.block;

/*import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import crazypants.enderio.api.tool.ITool;*/

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import de.keridos.floodlights.tileentity.TileEntityFL;
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
 * This Class defines the block properties of the carbon floodlight.
 */
public class BlockCarbonFloodlight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockCarbonFloodlight() {
        super(Names.Blocks.CARBON_FLOODLIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && heldItem == null && player.isSneaking()) {
            ((TileEntityCarbonFloodlight) world.getTileEntity(pos)).toggleInverted();
            String invert = (((TileEntityCarbonFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new TextComponentString(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && heldItem != null) {
            if (!ModCompatibility.WrenchAvailable && heldItem.getItem() == getMinecraftItem("stick")) {
                ((TileEntityCarbonFloodlight) world.getTileEntity(pos)).changeMode(player);
                return true;
            } else if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                world.destroyBlock(pos, true);
                return true;
            } else if (ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                ((TileEntityCarbonFloodlight) world.getTileEntity(pos)).changeMode(player);
                return true;
            } else if (heldItem.getItem() == Items.DYE) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(15 - heldItem.getItemDamage());
                return true;
            }
        }
        if (!world.isRemote) {
            player.openGui(FloodLights.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        } else {
            return true;
        }
    }

    @Override
    public TileEntityCarbonFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntityCarbonFloodlight();
    }


}
