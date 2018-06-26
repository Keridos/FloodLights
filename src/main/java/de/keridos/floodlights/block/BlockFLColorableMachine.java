package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.GeneralUtil.getMinecraftItem;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 02/02/2016.
 * This Class
 */
@SuppressWarnings({"deprecation", "NullableProblems", "WeakerAccess"})
public class BlockFLColorableMachine extends BlockFL {
    public static final PropertyInteger COLOR = PropertyInteger.create("color", 0, 16);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    protected int guiId = -1;

    public BlockFLColorableMachine(String unlocName, Material material, SoundType type, float hardness) {
        super(unlocName, material, type, hardness);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(COLOR, 0).withProperty(ACTIVE, false).withProperty(FACING, EnumFacing.DOWN));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta > 7) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta - 8)).withProperty(ACTIVE,
                    true);
        } else {
            return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta)).withProperty(ACTIVE, false);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 8 * (state.getValue(ACTIVE) ? 1 : 0) + state.getValue(FACING).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, COLOR);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        TileEntityMetaFloodlight tileEntity = (TileEntityMetaFloodlight) world.getTileEntity(pos);
        if (tileEntity == null)
            return true;

        ItemStack heldItem = player.getHeldItem(hand);
        if (hand == EnumHand.MAIN_HAND) {
            if (heldItem == ItemStack.EMPTY && player.isSneaking()) {
                tileEntity.toggleInverted();
                String invert = (tileEntity.getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
                player.sendMessage(new TextComponentString(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
                return true;
            } else if (heldItem != ItemStack.EMPTY) {
                if (!ModCompatibility.WrenchAvailable && heldItem.getItem() == getMinecraftItem("stick")) {
                    tileEntity.changeMode(player);
                    return true;
                } else if (player.isSneaking() && ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                    world.destroyBlock(pos, true);
                    return true;
                } else if (ModCompatibility.getInstance().isItemValidWrench(heldItem)) {
                    tileEntity.changeMode(player);
                    return true;
                } else if (heldItem.getItem() == Items.DYE) {
                    tileEntity.setColor(15 - heldItem.getItemDamage());
                    return true;
                }
            }

            player.openGui(FloodLights.instance, guiId, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityMetaFloodlight) {
            ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(world.isBlockIndirectlyGettingPowered(pos) > 0);

            if (!(blockIn instanceof BlockFL) && blockIn != Blocks.AIR)
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleUpdateRun();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState block) {
        if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityMetaFloodlight)
            ((TileEntityMetaFloodlight) world.getTileEntity(pos)).setRedstone(world.isBlockIndirectlyGettingPowered(pos) > 0);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMetaFloodlight)
            ((TileEntityMetaFloodlight) tileEntity).lightSource(true);

        super.breakBlock(world, pos, blockState);
    }
}
