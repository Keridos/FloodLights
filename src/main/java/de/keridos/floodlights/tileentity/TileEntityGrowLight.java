package de.keridos.floodlights.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.GeneralUtil;
import de.keridos.floodlights.util.RandomUtil;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;
import static de.keridos.floodlights.util.MathUtil.rotate;

/**
 * Created by Keridos on 04.05.2015.
 * This Class is the tile entity for the small floodlight.
 */

public class TileEntityGrowLight extends TileEntityFLElectric {
    private long nextGrowTick = 0;

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return (from.getOpposite().ordinal() == orientation.ordinal());
    }


    public void growSource(boolean remove) {
        int[] rotatedCoords = rotate(1, 0, 0, this.orientation);
        int x = this.pos.getX() + rotatedCoords[0];
        int y = this.pos.getY()+ rotatedCoords[1];
        int z = this.pos.getZ() + rotatedCoords[2];
        if (remove) {
            if (worldObj.getBlockState(new BlockPos(x, y, z)).getBlock() == ModBlocks.blockPhantomLight) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x, y, z));
                light.removeSource(this.pos);
            }
        } else if (worldObj.getBlockState(new BlockPos(x, y, z)).getBlock().isAir(worldObj,new BlockPos(x, y, z))) {
            setLight(new BlockPos(x, y, z));
        } else if (worldObj.getBlockState(new BlockPos(x, y, z)).getBlock() == ModBlocks.blockPhantomLight) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x, y, z));
            light.addSource(this.pos);
        }

    }

    public void update() {
        super.update();
        World world = this.getWorld();
        if (ModCompatibility.IC2Loaded && !wasAddedToEnergyNet && !world.isRemote) {
            //addToIc2EnergyNetwork();
            wasAddedToEnergyNet = true;
        }
        if (!world.isRemote) {
            int realEnergyUsage = ConfigHandler.energyUsageGrowLight;
            if (inventory[0] != null) {
                if (ModCompatibility.IC2Loaded) {
                    /*if (inventory[0].getItem() instanceof IElectricItem) {
                        double dischargeValue = (storage.getMaxEnergyStored() - (double) storage.getEnergyStored()) / 8.0D;
                        storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(8 * ElectricItem.manager.discharge(inventory[0], dischargeValue, 4, false, true, false)));
                    }*/
                }
                if (inventory[0].getItem() instanceof IEnergyContainerItem) {
                    IEnergyContainerItem item = (IEnergyContainerItem) inventory[0].getItem();
                    int dischargeValue = Math.min(item.getEnergyStored(inventory[0]), (storage.getMaxEnergyStored() - storage.getEnergyStored()));
                    storage.modifyEnergyStored(item.extractEnergy(inventory[0], dischargeValue, false));
                }
            }
            if (timeout > 0) {
                timeout--;
                return;
            }
            if (active && (storage.getEnergyStored() >= realEnergyUsage || storageEU >= (double) realEnergyUsage / 8.0D)) {
                if (world.getWorldTime() > nextGrowTick) {
                    BlockPos blockPosTarget = new BlockPos(this.pos.getX() + this.orientation.getFrontOffsetX() * 2, this.pos.getY() + this.orientation.getFrontOffsetY() * 2, this.pos.getZ() + this.orientation.getFrontOffsetZ() * 2);
                    BlockPos blockPosFront = new BlockPos(this.pos.getX() + this.orientation.getFrontOffsetX(), this.pos.getY() + this.orientation.getFrontOffsetY(), this.pos.getZ() + this.orientation.getFrontOffsetZ());
                    Block block = worldObj.getBlockState(blockPosTarget).getBlock();
                    Block blockFront = worldObj.getBlockState(blockPosFront).getBlock();
                    if (GeneralUtil.isBlockValidGrowable(block, world, blockPosTarget) && blockFront.isAir(world, blockPosFront)) {
                        ((IGrowable) block).grow(world, RandomUtil.random, blockPosTarget,world.getBlockState(blockPosTarget));
                    }
                    nextGrowTick = world.getWorldTime() + RandomUtil.getRandomTickTimeoutFromFloatChance(ConfigHandler.chanceGrowLight);
                }
                if (update) {
                    if (mode == 0) {
                        growSource(true);
                        growSource(false);
                    }
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    world.markBlockForUpdate(this.pos);
                    update = false;
                } else if (!wasActive) {
                    if (mode == 0) {
                        growSource(false);
                    }
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    world.markBlockForUpdate(this.pos);
                }
                if (storageEU >= (double) realEnergyUsage / 8.0D) {
                    storageEU -= (double) realEnergyUsage / 8.0D;
                } else {
                    storage.modifyEnergyStored(-realEnergyUsage);
                }
                wasActive = true;
            } else if ((!active || (storage.getEnergyStored() < realEnergyUsage && storageEU < (double) realEnergyUsage / 8.0D)) && wasActive) {
                if (mode == 0) {
                    growSource(true);
                }
                world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, false), 2);
                world.markBlockForUpdate(this.pos);
                wasActive = false;
                timeout = ConfigHandler.timeoutFloodlights;
                update = false;
            }
        }
    }

    public void changeMode(EntityPlayer player) {
        World world = this.getWorld();
        if (!world.isRemote) {
            if (mode == 0) {
                growSource(true);
            }
            mode = (mode == 1 ? 0 : mode + 1);
            if (active && (storage.getEnergyStored() >= ConfigHandler.energyUsage || storageEU >= ConfigHandler.energyUsage / 8.0D) && mode == 0) {
                growSource(false);
            }
            String modeString = (mode == 0 ? Names.Localizations.LIGHTING : Names.Localizations.DARK_LIGHT);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.MODE) + ": " + safeLocalize(modeString)));
        }
    }
}
