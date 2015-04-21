package de.keridos.floodlights.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Optional;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.handler.lighting.LightHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the electric floodlight TileEntity.
 */
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2")
public class TileEntityElectricFloodlight extends TileEntityFL implements IEnergyHandler, IEnergySink {

    private boolean active = false;
    private boolean wasActive = false;
    private boolean wasAddedToEnergyNet = false;
    private int timeout;
    private int storageEU;
    protected EnergyStorage storage = new EnergyStorage(50000);
    private LightHandler lightHandler = LightHandler.getInstance();
    private ConfigHandler configHandler = ConfigHandler.getInstance();

    public TileEntityElectricFloodlight() {
        super();
        Random rand = new Random();
        timeout = rand.nextInt((500 - 360) + 1) + 360;
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
        if (storage.getMaxEnergyStored() - storage.getEnergyStored() >= MathHelper.truncateDoubleToInt(v * 4)) {
            storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(v * 4));
        } else {
            storageEU += MathHelper.truncateDoubleToInt(v * 4) - (storage.getMaxEnergyStored() - storage.getEnergyStored());
            storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(v * 4));
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
        if (storageEU < 4000) {
            return 8192.0D;
        }
        return 0.0D;
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
            int realEnergyUsage = configHandler.energyUsage / (mode == 0 ? 1 : 2);
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

    public boolean getInverted() {
        return inverted;
    }

    public void changeMode(EntityPlayer player) {
        World world = this.getWorldObj();
        if (!world.isRemote) {
            ForgeDirection direction = this.getOrientation();
            int realEnergyUsage = configHandler.energyUsage / (mode == 0 ? 1 : 4);
            lightHandler.removeSource(world, this.xCoord, this.yCoord, this.zCoord, direction, this.mode);
            mode = (mode == 2 ? 0 : mode + 1);
            if ((active ^ inverted) && (storage.getEnergyStored() >= realEnergyUsage || storageEU >= realEnergyUsage * 4)) {
                lightHandler.addSource(world, this.xCoord, this.yCoord, this.zCoord, direction, this.mode);
            }
            String modeString = (mode == 0 ? Names.Localizations.STRAIGHT : mode == 1 ? Names.Localizations.NARROW_CONE : Names.Localizations.WIDE_CONE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.MODE) + ": " + safeLocalize(modeString)));
        }
    }
}
