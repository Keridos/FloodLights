package de.keridos.floodlights.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;

import static de.keridos.floodlights.util.GeneralUtil.isItemStackValidElectrical;

/**
 * Created by Keridos on 04.05.2015.
 * This Class
 */

@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Optional.Interface(iface = "gregtech.api.interfaces.tileentity.IEnergyConnected", modid = "GregTech")})

public class TileEntityFLElectric extends TileEntityMetaFloodlight implements IEnergyHandler, IEnergyReceiver, IEnergySink {
    protected boolean wasAddedToEnergyNet = false;
    protected double storageEU;
    protected EnergyStorage storage = new EnergyStorage(50000);

    public TileEntityFLElectric() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        storage.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.STORAGE_EU)) {
            this.storageEU = nbtTagCompound.getDouble(Names.NBT.STORAGE_EU);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        storage.writeToNBT(nbtTagCompound);
        nbtTagCompound.setDouble(Names.NBT.STORAGE_EU, storageEU);
    }

    @Override
    public boolean canConnectEnergy(EnumFacing facing) {
        return true;
    }

    @Override
    public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(EnumFacing facing) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing facing) {
        return storage.getMaxEnergyStored();
    }

    public void setEnergyStored(int energyStored) {
        storage.setEnergyStored(energyStored);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergy(EnumFacing forgeDirection, double v, double v1) {
        if ((double) (storage.getMaxEnergyStored() - storage.getEnergyStored()) >= (v * 8.0D)) {
            storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(v * 8.0D));
        } else {
            storageEU += v - ((double) (storage.getMaxEnergyStored() - storage.getEnergyStored()) / 8.0D);
            storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(v * 8.0D));
        }
        return 0.0D;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSinkTier() {
        return 4;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double getDemandedEnergy() {
        return Math.max(4000D - storageEU, 0.0D);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing forgeDirection) {
        return true;
    }

    @Optional.Method(modid = "IC2")
    public void addToIc2EnergyNetwork() {
        if (!worldObj.isRemote) {
            EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    @Optional.Method(modid = "IC2")
    @Override
    public void invalidate() {
        super.invalidate();
        if (!worldObj.isRemote) {
            EnergyTileUnloadEvent event = new EnergyTileUnloadEvent(this);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return isItemStackValidElectrical(itemstack);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing facing) {
        return isItemStackValidElectrical(itemstack);
    }

    protected void tryDischargeItem(ItemStack itemStack) {
        if (itemStack != null) {
            if (ModCompatibility.IC2Loaded) {
                    if (inventory[0].getItem() instanceof IElectricItem) {
                        double dischargeValue = (storage.getMaxEnergyStored() - (double) storage.getEnergyStored()) / 8.0D;
                        storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(8 * ElectricItem.manager.discharge(inventory[0], dischargeValue, 4, false, true, false)));
                    }
            }
            if (itemStack.getItem() instanceof IEnergyContainerItem) {
                IEnergyContainerItem item = (IEnergyContainerItem) itemStack.getItem();
                int dischargeValue = Math.min(item.getEnergyStored(itemStack), (storage.getMaxEnergyStored() - storage.getEnergyStored()));
                storage.modifyEnergyStored(item.extractEnergy(itemStack, dischargeValue, false));
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (ModCompatibility.IC2Loaded && !wasAddedToEnergyNet && !worldObj.isRemote) {
            addToIc2EnergyNetwork();
            wasAddedToEnergyNet = true;
        }
    }
}
