package de.keridos.floodlights.tileentity;

import cpw.mods.fml.common.Optional;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.handler.lighting.LightHandler;
import de.keridos.floodlights.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by Keridos on 04.05.2015.
 * This Class is the tile entity for the small floodlight
 */

@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2")
public class TileEntitySmallFloodlight extends TileEntityFLElectric {
    private boolean wasActive = false;
    private int timeout;
    private LightHandler lightHandler = LightHandler.getInstance();
    private ConfigHandler configHandler = ConfigHandler.getInstance();

    public TileEntitySmallFloodlight() {
        super();
        Random rand = new Random();
        timeout = rand.nextInt((500 - 360) + 1) + 360;
        this.mode = 3;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        storage.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.WAS_ACTIVE)) {
            this.wasActive = nbtTagCompound.getBoolean(Names.NBT.WAS_ACTIVE);
        }
        if (nbtTagCompound.hasKey(Names.NBT.TIMEOUT)) {
            this.timeout = nbtTagCompound.getInteger(Names.NBT.TIMEOUT);
        } else {
            Random rand = new Random();
            timeout = rand.nextInt((500 - 360) + 1) + 360;
        }
        if (nbtTagCompound.hasKey(Names.NBT.STATE)) {
            this.setActive(nbtTagCompound.getInteger(Names.NBT.STATE) == 0 ? false : true);
        }
        if (nbtTagCompound.hasKey(Names.NBT.STORAGE_EU)) {
            this.storageEU = nbtTagCompound.getInteger(Names.NBT.STORAGE_EU);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        storage.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.WAS_ACTIVE, wasActive);
        nbtTagCompound.setInteger(Names.NBT.TIMEOUT, timeout);
        nbtTagCompound.setInteger(Names.NBT.STORAGE_EU, storageEU);
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        World world = this.getWorldObj();
        if (ModCompatibility.IC2Loaded && !wasAddedToEnergyNet && !world.isRemote) {
            addToIc2EnergyNetwork();
            wasAddedToEnergyNet = true;
        }
        if (!world.isRemote) {
            ForgeDirection direction = this.getOrientation();
            int realEnergyUsage = configHandler.energyUsageSmallFloodlight;
            if ((active ^ inverted) && (storage.getEnergyStored() >= realEnergyUsage || storageEU >= realEnergyUsage * 4)) {
                if (!wasActive || world.getTotalWorldTime() % timeout == 0) {
                    if (world.getTotalWorldTime() % timeout == 0) {
                        lightHandler.removeSource(world, this.xCoord, this.yCoord, this.zCoord, direction, this.mode);
                        lightHandler.addSource(world, this.xCoord, this.yCoord, this.zCoord, direction, this.mode);
                        world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.getOrientation().ordinal() + 6, 2);
                    } else {
                        lightHandler.addSource(world, this.xCoord, this.yCoord, this.zCoord, direction, this.mode);
                        world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) + 6, 2);
                    }
                }
                if (storageEU >= realEnergyUsage * 4) {
                    storageEU -= realEnergyUsage * 4;
                } else {
                    storage.modifyEnergyStored(-realEnergyUsage);
                }
                wasActive = true;
            } else {
                if (wasActive) {
                    lightHandler.removeSource(world, this.xCoord, this.yCoord, this.zCoord, direction, this.mode);
                    world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) - 6, 2);
                }
                wasActive = false;
            }
        }
    }

    public void setActive(boolean b) {
        active = b;
        this.setState((byte) (this.active ? 1 : 0));
    }

    public void toggleInverted() {
        inverted = !inverted;
    }
}