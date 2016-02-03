package de.keridos.floodlights.block;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.GeneralUtil.getMinecraftItem;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
public class BlockElectricFloodlight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockElectricFloodlight() {
        super(Names.Blocks.ELECTRIC_FLOODLIGHT, Material.rock, soundTypeMetal, 2.5F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState blockState, Block block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityElectricFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityElectricFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
            if (!(block instanceof BlockFL) && block == Blocks.air) {
                ((TileEntityMetaFloodlight) world.getTileEntity(pos)).toggleUpdateRun();
            }
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState block) {
        if (!world.isRemote) {
            if (world.isBlockIndirectlyGettingPowered(pos) != 0) {
                ((TileEntityElectricFloodlight) world.getTileEntity(pos)).setRedstone(true);
            } else if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
                ((TileEntityElectricFloodlight) world.getTileEntity(pos)).setRedstone(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumFacing side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (!world.isRemote && player.getHeldItem() == null && player.isSneaking()) {
            ((TileEntityElectricFloodlight) world.getTileEntity(pos)).toggleInverted();
            String invert = (((TileEntityElectricFloodlight) world.getTileEntity(pos)).getInverted() ? Names.Localizations.TRUE : Names.Localizations.FALSE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.INVERT) + ": " + safeLocalize(invert)));
            return true;
        } else if (!world.isRemote && player.getHeldItem() != null) {
            if (!ModCompatibility.WrenchAvailable && player.getHeldItem().getItem() == getMinecraftItem("stick")) {
                ((TileEntityElectricFloodlight) world.getTileEntity(pos)).changeMode(player);
            }
           /* if (ModCompatibility.BCLoaded) {
                if (!player.isSneaking() && player.getHeldItem().getItem() instanceof IToolWrench) {
                    ((TileEntityElectricFloodlight) world.getTileEntity(pos)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolWrench) {
                    world.func_147480_a(pos, true);
                    return true;
                }
            }
            if (ModCompatibility.EnderIOLoaded) {
                if (!player.isSneaking() && player.getHeldItem().getItem() instanceof ITool) {
                    ((TileEntityElectricFloodlight) world.getTileEntity(pos)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem() instanceof ITool) {
                    world.func_147480_a(pos, true);
                    return true;
                }
            }
            if (ModCompatibility.CofhCoreLoaded) {
                if (!player.isSneaking() && player.getHeldItem().getItem() instanceof IToolHammer) {
                    ((TileEntityElectricFloodlight) world.getTileEntity(pos)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem() instanceof IToolHammer) {
                    world.func_147480_a(pos, true);
                    return true;
                }
            }
            if (ModCompatibility.IC2Loaded) {
                if (!player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
                    ((TileEntityElectricFloodlight) world.getTileEntity(pos)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
                    world.func_147480_a(pos, true);
                    return true;
                }
                if (!player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrenchElectric")) {
                    ((TileEntityElectricFloodlight) world.getTileEntity(pos)).changeMode(player);
                    return true;
                } else if (player.isSneaking() && player.getHeldItem().getItem().getUnlocalizedName().equals("ic2.itemToolWrenchElectric")) {
                    world.func_147480_a(pos, true);
                    return true;
                }
            }*/
            if (player.getHeldItem().getItem() == Items.dye) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(15 - player.getHeldItem().getItemDamage());
                return true;
            } else if (player.getHeldItem().getItem() == Item.getItemFromBlock(Blocks.wool) && !player.isSneaking()) {
                ((TileEntityFL) world.getTileEntity(pos)).setColor(16);
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
    public TileEntityElectricFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntityElectricFloodlight();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        ((TileEntityElectricFloodlight) world.getTileEntity(pos)).removeSource(-1);
        super.breakBlock(world, pos, blockState);
    }
}
