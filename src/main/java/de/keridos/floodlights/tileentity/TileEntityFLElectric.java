package de.keridos.floodlights.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.BaseFLTeslaContainer;
import de.keridos.floodlights.util.MathUtil;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;

import static de.keridos.floodlights.compatability.ModCompatibility.TeslaLoaded;
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
    private Object container;

    protected TileEntityFLElectric() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        storage.readFromNBT(nbtTagCompound);
        if (TeslaLoaded) {
            getTeslaContainer(this.container).deserializeNBT(nbtTagCompound);
        }
        if (nbtTagCompound.hasKey(Names.NBT.STORAGE_EU)) {
            this.storageEU = nbtTagCompound.getDouble(Names.NBT.STORAGE_EU);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        storage.writeToNBT(nbtTagCompound);
        if (TeslaLoaded){
            getTeslaContainer(this.container).serializeNBT(nbtTagCompound);
        }
        nbtTagCompound.setDouble(Names.NBT.STORAGE_EU, storageEU);
        return nbtTagCompound;
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

    @Optional.Method(modid = "tesla")
    public BaseFLTeslaContainer getTeslaContainer(Object container){
        if (container instanceof BaseFLTeslaContainer) {
            return (BaseFLTeslaContainer) container;
        } else {
            container = new BaseFLTeslaContainer();
            return (BaseFLTeslaContainer) container;
        }

    }

    @Optional.Method(modid = "tesla")
    public void moveEnergyFromTeslaToRF(){
        if (container instanceof BaseFLTeslaContainer) {
            int energyNeeded = storage.getEnergyStored() -storage.getMaxEnergyStored();
            if (energyNeeded> 0){
                 storage.modifyEnergyStored((int)((BaseFLTeslaContainer) container).takePower(energyNeeded, false));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        // This method is where other things will try to access your TileEntity's Tesla
        // capability. In the case of the analyzer, is a consumer, producer and holder so we
        // can allow requests that are looking for any of those things. This example also does
        // not care about which side is being accessed, however if you wanted to restrict which
        // side can be used, for example only allow power input through the back, that could be
        // done here.
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER) {
            if (TeslaLoaded) {
                moveEnergyFromTeslaToRF();
                return (T) getTeslaContainer(this.container);
            }
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

        // This method replaces the instanceof checks that would be used in an interface based
        // system. It can be used by other things to see if the TileEntity uses a capability or
        // not. This example is a Consumer, Producer and Holder, so we return true for all
        // three. This can also be used to restrict access on certain sides, for example if you
        // only accept power input from the bottom of the block, you would only return true for
        // Consumer if the facing parameter was down.
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return true;

        return super.hasCapability(capability, facing);
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
