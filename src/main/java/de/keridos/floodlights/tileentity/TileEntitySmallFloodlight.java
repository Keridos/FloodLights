package de.keridos.floodlights.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.MathUtil.rotate;

/**
 * Created by Keridos on 04.05.2015.
 * This Class is the tile entity for the small floodlight.
 */

public class TileEntitySmallFloodlight extends TileEntityFLElectric {
    private boolean rotationState = false;

    public TileEntitySmallFloodlight() {
        super();
        this.rotationState = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.ROTATION_STATE)) {
            this.rotationState = nbtTagCompound.getBoolean(Names.NBT.ROTATION_STATE);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.ROTATION_STATE, rotationState);
        return nbtTagCompound;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing facing) {
        return (facing.getOpposite().ordinal() == orientation.ordinal());
    }

    public void toggleRotationState() {
        rotationState = !rotationState;
        this.worldObj.markBlocksDirtyVertical(this.pos.getX(),this.pos.getZ(),this.pos.getX(),this.pos.getZ());;
    }

    public boolean getRotationState() {
        return rotationState;
    }

    public void setRotationState(boolean rotationState) {
        this.rotationState = rotationState;
    }

    public void smallSource(boolean remove) {
        for (int i = 0; i < 5; i++) {
            int a = 0;
            int b = 0;
            int c = 0;
            if (i == 0) {
                a = 1;
                b = c = 0;
            } else if (i == 1) {
                a = c = 0;
                b = 1;
            } else if (i == 2) {
                a = c = 0;
                b = -1;
            } else if (i == 3) {
                a = b = 0;
                c = 1;
            } else if (i == 4) {
                a = b = 0;
                c = -1;
            }
            int[] rotatedCoords = rotate(a, b, c, this.orientation);
            int x = this.pos.getX() + rotatedCoords[0];
            int y = this.pos.getY() + rotatedCoords[1];
            int z = this.pos.getZ() + rotatedCoords[2];
            if (remove) {
                if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                    TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x, y, z));
                    light.removeSource(this.pos);
                }
            } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isAir(worldObj.getBlockState(new BlockPos(x,y,z)),worldObj, new BlockPos(x,y,z))) {
                setLight(new BlockPos(x,y,z));
            } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                light.addSource(this.pos);
            }
        }
    }

    public void update() {
        super.update();
        World world = this.getWorld();
        /*if (ModCompatibility.IC2Loaded && !wasAddedToEnergyNet && !world.isRemote) {
            addToIc2EnergyNetwork();
            wasAddedToEnergyNet = true;
        }*/
        if (!world.isRemote) {
            int realEnergyUsage = ConfigHandler.energyUsageSmallFloodlight;
            if (inventory[0] != null) {
                /*if (ModCompatibility.IC2Loaded) {
                    if (inventory[0].getItem() instanceof IElectricItem) {
                        double dischargeValue = (storage.getMaxEnergyStored() - (double) storage.getEnergyStored()) / 8.0D;
                        storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(8 * ElectricItem.manager.discharge(inventory[0], dischargeValue, 4, false, true, false)));
                    }
                }*/
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
                if (update) {
                    smallSource(true);
                    smallSource(false);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    world.markBlocksDirtyVertical(this.pos.getX(),this.pos.getZ(),this.pos.getX(),this.pos.getZ());;
                    update = false;
                } else if (!wasActive) {
                    smallSource(false);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    world.markBlocksDirtyVertical(this.pos.getX(),this.pos.getZ(),this.pos.getX(),this.pos.getZ());;
                }
                if (storageEU >= (double) realEnergyUsage / 8.0D) {
                    storageEU -= (double) realEnergyUsage / 8.0D;
                } else {
                    storage.modifyEnergyStored(-realEnergyUsage);
                }
                wasActive = true;
            } else if ((!active || (storage.getEnergyStored() < realEnergyUsage && storageEU < (double) realEnergyUsage / 8.0D)) && wasActive) {
                smallSource(true);
                world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, false), 2);
                world.markBlocksDirtyVertical(this.pos.getX(),this.pos.getZ(),this.pos.getX(),this.pos.getZ());;
                wasActive = false;
                timeout = ConfigHandler.timeoutFloodlights;
                update = false;
            }
        }
    }
}
