package de.keridos.floodlights.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class TileEntityUVLight extends TileEntityFLElectric {

    public TileEntityUVLight() {
        super();
    }

    public void setLightUV(BlockPos pos) {
        if (worldObj.setBlockState(pos, ModBlocks.blockUVLightBlock.getDefaultState())) {
            TileEntityUVLightBlock light = (TileEntityUVLightBlock) worldObj.getTileEntity(pos);
            light.addSource(this.pos);
        }
    }

    public void UVSource(boolean remove) {
        for (int i = 1; i <= ConfigHandler.rangeUVFloodlight; i++) {
            BlockPos tempPos = new BlockPos(this.pos.getX() + this.orientation.getFrontOffsetX() * i,
                    this.pos.getY() + this.orientation.getFrontOffsetY() * i,
                    this.pos.getZ() + this.orientation.getFrontOffsetZ() * i);
            if (remove) {
                if (worldObj.getBlockState(tempPos).getBlock() == ModBlocks.blockUVLightBlock) {
                    TileEntityUVLightBlock light = (TileEntityUVLightBlock) worldObj.getTileEntity(tempPos);
                    light.removeSource(this.pos);
                }
            } else if (worldObj.getBlockState(tempPos).getBlock().isAir(worldObj.getBlockState(tempPos),worldObj, tempPos) || worldObj.getBlockState(tempPos).getBlock() == ModBlocks.blockPhantomLight) {
                worldObj.setBlockToAir(tempPos);
                worldObj.removeTileEntity(tempPos);
                setLightUV(tempPos);
            } else if (worldObj.getBlockState(tempPos).getBlock() == ModBlocks.blockUVLightBlock) {
                TileEntityUVLightBlock light = (TileEntityUVLightBlock) worldObj.getTileEntity(tempPos);
                light.addSource(this.pos);
            } else if (worldObj.getBlockState(tempPos).getBlock().isOpaqueCube(worldObj.getBlockState(tempPos))) {
                break;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        World world = this.getWorld();
        /*if (ModCompatibility.IC2Loaded && !wasAddedToEnergyNet && !world.isRemote) {
            addToIc2EnergyNetwork();
            wasAddedToEnergyNet = true;
        }*/
        if (!world.isRemote) {
            int realEnergyUsage = ConfigHandler.energyUsageUVFloodlight;
            if (inventory[0] != null) {
                /*if (ModCompatibility.IC2Loaded) {
                    if (inventory[0].getItem() instanceof IElectricItem) {
                        double dischargeValue = (storage.getMaxEnergyStored() - (double) storage.getEnergyStored()) / 8.0D;
                        storage.modifyEnergyStored(MathUtil.truncateDoubleToInt(8.0D * ElectricItem.manager.discharge(inventory[0], dischargeValue, 4, false, true, false)));
                    }
                }*/
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
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    world.markBlocksDirtyVertical(this.pos.getX(),this.pos.getZ(),this.pos.getX(),this.pos.getZ());
                    update = false;
                } else if (!wasActive) {
                    UVSource(false);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    world.markBlocksDirtyVertical(this.pos.getX(),this.pos.getZ(),this.pos.getX(),this.pos.getZ());
                }
                if (storageEU >= (double) realEnergyUsage / 8.0D) {
                    storageEU -= (double) realEnergyUsage / 8.0D;
                } else {
                    storage.modifyEnergyStored(-realEnergyUsage);
                }
                wasActive = true;
            } else if ((!active || (storage.getEnergyStored() < realEnergyUsage && storageEU < (double) realEnergyUsage / 8.0D)) && wasActive) {
                UVSource(true);
                world.setBlockState(this.pos, world.getBlockState(this.pos).getBlock().getStateFromMeta(this.getOrientation().ordinal()), 2);
                world.markBlocksDirtyVertical(this.pos.getX(),this.pos.getZ(),this.pos.getX(),this.pos.getZ());
                wasActive = false;
            }
        }
    }
}

