package de.keridos.floodlights.capability;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

    // 1 EU = 4 RF
    public static final int EU_TO_RF_RATE = 4;

    private ItemStack delegate;
    private int prevStorageRatio;

    public CustomEnergyStorage(ItemStack delegate) {
        this();
        this.delegate = delegate;
        if (delegate.getTagCompound() == null ) {
            delegate.setTagCompound(new NBTTagCompound());
        }
        readFromNBT(delegate.getTagCompound());
    }

    public CustomEnergyStorage() {
        super(ConfigHandler.energyBufferSize);
    }

    public void setEnergyStored(int energy) {
        this.energy = Math.max(0, Math.min(energy, capacity));
    }

    public double receiveEU(double amount) {
        int received = receiveEnergy((int) Math.round(amount * EU_TO_RF_RATE), false);
        return amount - (double) received / EU_TO_RF_RATE;
    }

    public boolean isFull() {
        return getEnergyStored() == getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int prev = getEnergyStored();
        int result = super.receiveEnergy(maxReceive, simulate);
        if (delegate != null && getEnergyStored() != prev)
            //noinspection ConstantConditions
            writeToNBT(delegate.getTagCompound());

        return result;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int prev = getEnergyStored();
        int result = super.extractEnergy(maxExtract, simulate);
        if (delegate != null && getEnergyStored() != prev)
            //noinspection ConstantConditions
            writeToNBT(delegate.getTagCompound());
        return result;
    }

    /**
     * Returns whether amount of stored energy has changed significantly since last call.
     */
    public boolean storageChanged() {
        int ratio = Math.round(energy * 1000f / capacity);
        if (Math.abs(ratio - prevStorageRatio) > 10 || (ratio != prevStorageRatio && (energy == 0 || energy == capacity))) {
            prevStorageRatio = ratio;
            return true;
        }
        return false;
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound.hasKey(Names.NBT.STORAGE_FE))
            setEnergyStored(nbtTagCompound.getInteger(Names.NBT.STORAGE_FE));
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger(Names.NBT.STORAGE_FE, energy);
    }
}