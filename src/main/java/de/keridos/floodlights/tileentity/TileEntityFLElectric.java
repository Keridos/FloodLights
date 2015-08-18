package de.keridos.floodlights.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Optional;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Keridos on 04.05.2015.
 * This Class
 */

@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Optional.Interface(iface = "gregtech.api.interfaces.tileentity.IEnergyConnected", modid = "GregTech")})

public class TileEntityFLElectric extends TileEntityMetaFloodlight implements IEnergyHandler, IEnergySink {
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

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double v, double v1) {
        if ((double) (storage.getMaxEnergyStored() - storage.getEnergyStored()) >= (v * 8.0D)) {
            storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(v * 8.0D));
        } else if (storageEU < 4000D) {
            storageEU += v - ((double) (storage.getMaxEnergyStored() - storage.getEnergyStored()) / 8.0D);
            storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(v * 8.0D));
        } else {
            return v;
        }
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSinkTier() {
        return 4;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double getDemandedEnergy() {
        return 8192.0D;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
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


}
