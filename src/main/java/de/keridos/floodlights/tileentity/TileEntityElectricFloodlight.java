package de.keridos.floodlights.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.handler.LightHandler;
import de.keridos.floodlights.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Nico on 01/10/2014.
 */
public class TileEntityElectricFloodlight extends TileEntityFL implements IEnergyHandler {
    private boolean inverted = false;
    private boolean active = false;
    private boolean wasActive = false;
    protected EnergyStorage storage = new EnergyStorage(32000);
    private LightHandler lightHandler = LightHandler.getInstance();
    private ConfigHandler configHandler = ConfigHandler.getInstance();


    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

        super.readFromNBT(nbtTagCompound);
        storage.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.INVERT)) {
            this.inverted = nbtTagCompound.getBoolean(Names.NBT.INVERT);
        }
        if (nbtTagCompound.hasKey(Names.NBT.WASACTIVE)) {
            this.wasActive = nbtTagCompound.getBoolean(Names.NBT.WASACTIVE);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {

        super.writeToNBT(nbtTagCompound);
        storage.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.INVERT, inverted);
        nbtTagCompound.setBoolean(Names.NBT.WASACTIVE, wasActive);
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {

        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {

        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {

        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {

        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        World world = this.getWorldObj();
        ForgeDirection direction = this.getOrientation();
        if (((active ^ inverted) && getEnergyStored(ForgeDirection.DOWN) >= configHandler.energyUsage)) {
            if (!wasActive) {
                lightHandler.addSource(world, this.xCoord, this.yCoord, this.zCoord, direction, 0);
            }
            storage.extractEnergy(configHandler.energyUsage, false);
            wasActive = true;

        } else {
            if (wasActive) {
                lightHandler.removeSource(world, this.xCoord, this.yCoord, this.zCoord, direction, 0);
            }
            wasActive = false;
        }

    }

    public void setActive(boolean b) {
        active = b;
    }

    public void toggleInverted() {
        inverted = !inverted;
    }

    public boolean getInverted() {
        return inverted;
    }
}
