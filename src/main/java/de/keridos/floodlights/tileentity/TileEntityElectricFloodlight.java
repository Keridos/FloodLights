package de.keridos.floodlights.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static de.keridos.floodlights.util.GeneralUtil.isItemStackValidElectrical;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;
import static de.keridos.floodlights.util.MathUtil.rotate;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the electric floodlight TileEntity.
 */

public class TileEntityElectricFloodlight extends TileEntityFLElectric implements ISidedInventory {
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return isItemStackValidElectrical(itemstack);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return isItemStackValidElectrical(itemstack);
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    public void straightSource(boolean remove) {
        for (int i = 1; i <= ConfigHandler.rangeStraightFloodlight; i++) {
            int x = this.xCoord + this.orientation.offsetX * i;
            int y = this.yCoord + this.orientation.offsetY * i;
            int z = this.zCoord + this.orientation.offsetZ * i;
            if (remove && worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                light.removeSource(this.xCoord, this.yCoord, this.zCoord);
            } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                worldObj.setBlock(x, y, z, ModBlocks.blockFLLight);
                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                light.addSource(this.xCoord, this.yCoord, this.zCoord);
            } else if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                light.addSource(this.xCoord, this.yCoord, this.zCoord);
            } else if (worldObj.getBlock(x, y, z).isOpaqueCube()) {
                break;
            }
        }
    }

    public void wideConeSource(boolean remove) {
        boolean[] failedBeams = new boolean[9];
        for (int j = 0; j <= 16; j++) {
            if (j <= 8) {
                for (int i = 1; i <= ConfigHandler.rangeConeFloodlight / 2; i++) {
                    int b = 0;
                    int c = 0;
                    switch (j) {
                        case 0:
                            b += i;
                            break;
                        case 1:
                            b -= i;
                            break;
                        case 2:
                            c += i;
                            break;
                        case 3:
                            c -= i;
                            break;
                        case 4:
                            b += i;
                            c += i;
                            break;
                        case 5:
                            b += i;
                            c -= i;
                            break;
                        case 6:
                            b -= i;
                            c += i;
                            break;
                        case 7:
                            b -= i;
                            c -= i;
                            break;
                    }
                    int[] rotatedCoords = rotate(i, b, c, this.orientation);
                    int x = this.xCoord + rotatedCoords[0];
                    int y = this.yCoord + rotatedCoords[1];
                    int z = this.zCoord + rotatedCoords[2];
                    if (remove && worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                        worldObj.setBlock(x, y, z, ModBlocks.blockFLLight);
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.addSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.addSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z).isOpaqueCube()) {
                        if (i < ConfigHandler.rangeConeFloodlight / 4) {   //This is for canceling the long rangs beams
                            failedBeams[j] = true;
                        }
                        break;
                    }
                }
            } else if (!failedBeams[j - 9] || remove) { // This is for the inner beams at longer range
                for (int i = ConfigHandler.rangeConeFloodlight / 4; i <= ConfigHandler.rangeConeFloodlight / 2; i++) {
                    int b = 0;
                    int c = 0;
                    switch (j) {
                        case 9:
                            b += i / 2;
                            break;
                        case 10:
                            b -= i / 2;
                            break;
                        case 11:
                            c += i / 2;
                            break;
                        case 12:
                            c -= i / 2;
                            break;
                        case 13:
                            b += i / 2;
                            c += i / 2;
                            break;
                        case 14:
                            b += i / 2;
                            c -= i / 2;
                            break;
                        case 15:
                            b -= i / 2;
                            c += i / 2;
                            break;
                        case 16:
                            b -= i / 2;
                            c -= i / 2;
                            break;
                    }
                    int[] rotatedCoords = rotate(i, b, c, this.orientation);
                    int x = this.xCoord + rotatedCoords[0];
                    int y = this.yCoord + rotatedCoords[1];
                    int z = this.zCoord + rotatedCoords[2];
                    if (remove && worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                        worldObj.setBlock(x, y, z, ModBlocks.blockFLLight);
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.addSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.addSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z).isOpaqueCube()) {
                        break;
                    }
                }
            }
        }
    }

    public void narrowConeSource(boolean remove) {
        boolean[] failedBeams = new boolean[9];    // for the additional beam to cancel when the main beams fail.
        for (int j = 0; j <= 16; j++) {
            if (j <= 8) {     // This is the main beams
                for (int i = 1; i <= ConfigHandler.rangeConeFloodlight / 2; i++) {
                    // for 1st light:
                    if (i == 1) {
                        int x = this.xCoord + this.orientation.offsetX;
                        int y = this.xCoord + this.orientation.offsetY;
                        int z = this.xCoord + this.orientation.offsetZ;
                        if (remove && worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                            light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                        } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                            worldObj.setBlock(x, y, z, ModBlocks.blockFLLight);
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                            light.addSource(this.xCoord, this.yCoord, this.zCoord);
                        } else if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                            light.addSource(this.xCoord, this.yCoord, this.zCoord);
                        } else if (worldObj.getBlock(x, y, z).isOpaqueCube()) {
                            return;
                        }
                    }
                    int a = 2 * i;
                    int b = 0;
                    int c = 0;
                    switch (j) {
                        case 0:
                            b += i;
                            break;
                        case 1:
                            b -= i;
                            break;
                        case 2:
                            c += i;
                            break;
                        case 3:
                            c -= i;
                            break;
                        case 4:
                            b += i;
                            c += i;
                            break;
                        case 5:
                            b += i;
                            c -= i;
                            break;
                        case 6:
                            b -= i;
                            c += i;
                            break;
                        case 7:
                            b -= i;
                            c -= i;
                            break;
                    }
                    int[] rotatedCoords = rotate(a, b, c, this.orientation); // rotate the coordinate to the correct spot in the real world :)
                    int x = this.xCoord + rotatedCoords[0];
                    int y = this.yCoord + rotatedCoords[1];
                    int z = this.zCoord + rotatedCoords[2];
                    if (remove && worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                        worldObj.setBlock(x, y, z, ModBlocks.blockFLLight);
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.addSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.addSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z).isOpaqueCube()) {
                        if (i < ConfigHandler.rangeConeFloodlight / 4) {   //This is for canceling the long rangs beams
                            failedBeams[j] = true;
                        }
                        break;
                    }
                }
            } else if (!failedBeams[j - 9] || remove) { // This is for the inner beams at longer range
                for (int i = ConfigHandler.rangeConeFloodlight / 4; i <= ConfigHandler.rangeConeFloodlight / 2; i++) {
                    int a = 2 * i;
                    int b = 0;
                    int c = 0;
                    switch (j) {
                        case 9:
                            b += i / 2;
                            break;
                        case 10:
                            b -= i / 2;
                            break;
                        case 11:
                            c += i / 2;
                            break;
                        case 12:
                            c -= i / 2;
                            break;
                        case 13:
                            b += i / 2;
                            c += i / 2;
                            break;
                        case 14:
                            b += i / 2;
                            c -= i / 2;
                            break;
                        case 15:
                            b -= i / 2;
                            c += i / 2;
                            break;
                        case 16:
                            b -= i / 2;
                            c -= i / 2;
                            break;
                    }
                    int[] rotatedCoords = rotate(a, b, c, this.orientation);
                    int x = this.xCoord + rotatedCoords[0];
                    int y = this.yCoord + rotatedCoords[1];
                    int z = this.zCoord + rotatedCoords[2];
                    if (remove && worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                        worldObj.setBlock(x, y, z, ModBlocks.blockFLLight);
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.addSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                        light.addSource(this.xCoord, this.yCoord, this.zCoord);
                    } else if (worldObj.getBlock(x, y, z).isOpaqueCube()) {
                        break;
                    }
                }
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
            int realEnergyUsage = ConfigHandler.energyUsage / (mode == 0 ? 1 : 2);
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
                if (!wasActive || update) {
                    if (update) {
                        removeSource(this.mode);
                        addSource(this.mode);
                        world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.getOrientation().ordinal() + 6, 2);
                    } else {
                        addSource(this.mode);
                        world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) + 6, 2);
                    }
                }
                if (storageEU >= (double) realEnergyUsage / 8.0D) {
                    storageEU -= (double) realEnergyUsage / 8.0D;
                } else {
                    storage.modifyEnergyStored(-realEnergyUsage);
                }
                wasActive = true;
            } else {
                if (wasActive) {
                    removeSource(this.mode);
                    world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) - 6, 2);
                }
                wasActive = false;
            }
        }
    }

    public void addSource(int mode) {
        if (mode == -1) {
            mode = this.mode;
        }
        if (mode == 0) {
            straightSource(false);
        } else if (mode == 1) {
            narrowConeSource(false);
        } else if (mode == 2) {
            wideConeSource(false);
        }
    }

    public void removeSource(int mode) {
        if (mode == -1) {
            mode = this.mode;
        }
        if (mode == 0) {
            straightSource(true);
        } else if (mode == 1) {
            narrowConeSource(true);
        } else if (mode == 2) {
            wideConeSource(true);
        }
    }

    public void changeMode(EntityPlayer player) {
        World world = this.getWorldObj();
        if (!world.isRemote) {
            ForgeDirection direction = this.getOrientation();
            int realEnergyUsage = ConfigHandler.energyUsage / (mode == 0 ? 1 : 4);
            removeSource(this.mode);
            mode = (mode == 2 ? 0 : mode + 1);
            if (active && (storage.getEnergyStored() >= realEnergyUsage || storageEU >= realEnergyUsage / 8.0D)) {
                addSource(this.mode);
            }
            String modeString = (mode == 0 ? Names.Localizations.STRAIGHT : mode == 1 ? Names.Localizations.NARROW_CONE : Names.Localizations.WIDE_CONE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.MODE) + ": " + safeLocalize(modeString)));
        }
    }
}
