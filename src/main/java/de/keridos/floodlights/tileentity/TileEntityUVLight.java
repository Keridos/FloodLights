package de.keridos.floodlights.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.util.MathUtil;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.world.World;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class TileEntityUVLight extends TileEntityFLElectric {

    public TileEntityUVLight() {
        super();
    }

    public void setLightUV(int x, int y, int z) {
        if (worldObj.setBlock(x, y, z, ModBlocks.blockUVLightBlock)) {
            TileEntityUVLightBlock light = (TileEntityUVLightBlock) worldObj.getTileEntity(x, y, z);
            light.addSource(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void UVSource(boolean remove) {
        for (int i = 1; i <= ConfigHandler.rangeUVFloodlight; i++) {
            int x = this.xCoord + this.orientation.offsetX * i;
            int y = this.yCoord + this.orientation.offsetY * i;
            int z = this.zCoord + this.orientation.offsetZ * i;
            if (remove) {
                if (worldObj.getBlock(x, y, z) == ModBlocks.blockUVLightBlock) {
                    TileEntityUVLightBlock light = (TileEntityUVLightBlock) worldObj.getTileEntity(x, y, z);
                    light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                }
            } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z) || worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                worldObj.setBlockToAir(x, y, z);
                worldObj.removeTileEntity(x, y, z);
                setLightUV(x, y, z);
            } else if (worldObj.getBlock(x, y, z) == ModBlocks.blockUVLightBlock) {
                TileEntityUVLightBlock light = (TileEntityUVLightBlock) worldObj.getTileEntity(x, y, z);
                light.addSource(this.xCoord, this.yCoord, this.zCoord);
            } else if (worldObj.getBlock(x, y, z).isOpaqueCube()) {
                break;
            }
        }
    }

    @Override
    public void updateEntity() {
        World world = this.getWorldObj();
        if (ModCompatibility.IC2Loaded && !wasAddedToEnergyNet && !world.isRemote) {
            addToIc2EnergyNetwork();
            wasAddedToEnergyNet = true;
        }
        if (!world.isRemote) {
            int realEnergyUsage = ConfigHandler.energyUsageUVFloodlight;
            if (inventory[0] != null) {
                if (ModCompatibility.IC2Loaded) {
                    if (inventory[0].getItem() instanceof IElectricItem) {
                        double dischargeValue = (storage.getMaxEnergyStored() - (double) storage.getEnergyStored()) / 8.0D;
                        storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(8.0D * ElectricItem.manager.discharge(inventory[0], dischargeValue, 4, false, true, false)));
                    }
                }
                if (inventory[0].getItem() instanceof IEnergyContainerItem) {
                    IEnergyContainerItem item = (IEnergyContainerItem) inventory[0].getItem();
                    int dischargeValue = Math.min(item.getEnergyStored(inventory[0]), (storage.getMaxEnergyStored() - storage.getEnergyStored()));
                    storage.modifyEnergyStored(item.extractEnergy(inventory[0], dischargeValue, false));
                }
            }
            if (active && (storage.getEnergyStored() >= realEnergyUsage || storageEU >= (double) realEnergyUsage / 8.0D)) {
                if (update) {
                    UVSource(true);
                    UVSource(false);
                    world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.getOrientation().ordinal() + 6, 2);
                    update = false;
                } else if (!wasActive) {
                    UVSource(false);
                    world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) + 6, 2);

                }
                if (storageEU >= (double) realEnergyUsage / 8.0D) {
                    storageEU -= (double) realEnergyUsage / 8.0D;
                } else {
                    storage.modifyEnergyStored(-realEnergyUsage);
                }
                wasActive = true;
            } else {
                if (wasActive) {
                    UVSource(true);
                    world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) - 6, 2);
                }
                wasActive = false;
            }
        }
    }
}
