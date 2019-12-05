package de.keridos.floodlights.tileentity;

import cofh.redstoneflux.api.IEnergyContainerItem;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.core.NetworkDataList;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Keridos on 04.05.2015.
 * This Class
 */

@SuppressWarnings("WeakerAccess")
@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2")})
public class TileEntityFLElectric extends TileEntityMetaFloodlight implements IEnergySink {

    // 1 EU = 4 RF
    private static final int EU_TO_RF_RATE = 4;

    /**
     * Base energy usage.
     */
    protected int energyUsage = ConfigHandler.energyUsage;
    /**
     * Real energy usage - with light mode taken into acocunt.
     */
    protected int realEnergyUsage;

    protected boolean wasAddedToEnergyNet = false;
    public CustomEnergyStorage energy = new CustomEnergyStorage(ConfigHandler.energyBufferSize);

    public TileEntityFLElectric() {
        super();
        updateEnergyUsage();
    }

    @Override
    public void update() {
        super.update();

        if (ModCompatibility.IC2Loaded && !wasAddedToEnergyNet && !world.isRemote) {
            addToIc2EnergyNetwork();
            wasAddedToEnergyNet = true;
        }

        if (world.isRemote || !isReady())
            return;

        tryDischargeItem(inventory.getStackInSlot(0));

        // Update client GUI when amount of stored energy has changed
        if (energy.storageChanged())
            syncWithAccessors();

        // Note: machine will shut down automatically if there is not enough energy
        if (active && hasEnergy())
            energy.extractEnergy(realEnergyUsage, false);
    }

    @Override
    protected boolean hasEnergy() {
        return energy.getEnergyStored() >= realEnergyUsage;
    }

    private void tryDischargeItem(ItemStack itemStack) {
        if (itemStack != ItemStack.EMPTY) {
            if (ModCompatibility.IC2Loaded && inventory.getStackInSlot(0).getItem() instanceof IElectricItem) {
                double dischargeValue = (energy.getMaxEnergyStored() - (double) energy.getEnergyStored()) / 8.0D;
                double discharged = ElectricItem.manager.discharge(inventory.getStackInSlot(0), dischargeValue, 4, false, true, false);
                energy.receiveEnergy(MathUtil.truncateDoubleToInt(8 * discharged), false);
            }
            if (itemStack.getItem() instanceof IEnergyContainerItem) {
                IEnergyContainerItem item = (IEnergyContainerItem) itemStack.getItem();
                int dischargeValue = Math.min(item.getEnergyStored(itemStack), (energy.getMaxEnergyStored() - energy.getEnergyStored()));
                energy.receiveEnergy(item.extractEnergy(itemStack, dischargeValue, false), false);
            }
        }
    }

    @Override
    public void setMode(int mode) {
        super.setMode(mode);
        updateEnergyUsage();
    }

    @Override
    public void changeMode(EntityPlayer player) {
        super.changeMode(player);
        updateEnergyUsage();
    }

    protected void updateEnergyUsage() {
        realEnergyUsage = energyUsage * (mode == LIGHT_MODE_STRAIGHT ? 1 : 4);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        energy.readFromNBT(nbtTagCompound);
        updateEnergyUsage();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        energy.writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public NetworkDataList getSyncData(@Nonnull NetworkDataList data) {
        super.getSyncData(data);
        data.add(energy.getEnergyStored());
        return data;
    }

    @Override
    public void applySyncData(ByteBuf buffer) {
        super.applySyncData(buffer);
        energy.setEnergyStored(buffer.readInt());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return (T) energy;
        return super.getCapability(capability, facing);
    }

    @Optional.Method(modid = "ic2")
    @Override
    public double injectEnergy(EnumFacing forgeDirection, double amount, double voltage) {
        return energy.receiveEU(amount);
    }

    @Optional.Method(modid = "ic2")
    @Override
    public int getSinkTier() {
        return 4;
    }

    @Optional.Method(modid = "ic2")
    @Override
    public double getDemandedEnergy() {
        return (double) energy.getMaxEnergyStored() / EU_TO_RF_RATE;
    }

    @Optional.Method(modid = "ic2")
    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing forgeDirection) {
        return true;
    }

    @Optional.Method(modid = "ic2")
    public void addToIc2EnergyNetwork() {
        if (!world.isRemote) {
            EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    @Optional.Method(modid = "ic2")
    @Override
    public void invalidate() {
        super.invalidate();
        if (!world.isRemote) {
            EnergyTileUnloadEvent event = new EnergyTileUnloadEvent(this);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    public static class CustomEnergyStorage extends EnergyStorage {

        private int prevStorageRatio;

        public CustomEnergyStorage(int capacity) {
            super(capacity);
        }

        public void setEnergyStored(int energy) {
            this.energy = Math.max(0, Math.min(energy, capacity));
        }

        public double receiveEU(double amount) {
            int received = receiveEnergy((int) Math.round(amount * EU_TO_RF_RATE), false);
            return amount - (double) received / EU_TO_RF_RATE;
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
}
