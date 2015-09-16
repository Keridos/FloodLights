package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import static de.keridos.floodlights.util.MathUtil.rotate;

/**
 * Created by Keridos on 06.05.2015.
 * This Class
 */
public class TileEntityMetaFloodlight extends TileEntityFL implements ISidedInventory {
    protected boolean active;
    protected boolean wasActive;
    protected boolean update = true;
    protected ItemStack[] inventory;

    public TileEntityMetaFloodlight() {
        super();
        this.wasActive = false;
        inventory = new ItemStack[1];
    }

    public void setRedstone(boolean b) {
        active = b ^ inverted;
        this.setState((byte) (this.active ? 1 : 0));
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void toggleInverted() {
        inverted = !inverted;
        active = !active;
        this.setState((byte) (this.active ? 1 : 0));
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean getWasActive() {
        return wasActive;
    }

    public void setWasActive(boolean wasActive) {
        this.wasActive = wasActive;
    }

    public void toggleUpdateRun() {
        update = true;
    }

    public void setLight(int x, int y, int z) {
        if (worldObj.getBlock(x, y, z) == ModBlocks.blockUVLightBlock) {
            return;
        }
        if (worldObj.setBlock(x, y, z, ModBlocks.blockFLLight)) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
            light.addSource(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.STATE)) {
            this.active = (nbtTagCompound.getInteger(Names.NBT.STATE) != 0);
        }
        if (nbtTagCompound.hasKey(Names.NBT.WAS_ACTIVE)) {
            this.wasActive = nbtTagCompound.getBoolean(Names.NBT.WAS_ACTIVE);
        }
        if (nbtTagCompound.hasKey(Names.NBT.ITEMS)) {
            NBTTagList list = nbtTagCompound.getTagList(Names.NBT.ITEMS, 10);
            NBTTagCompound item = list.getCompoundTagAt(0);
            int slot = item.getByte(Names.NBT.ITEMS);
            if (slot >= 0 && slot < getSizeInventory()) {
                setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.WAS_ACTIVE, wasActive);
        nbtTagCompound.setByte(Names.NBT.STATE, state);
        NBTTagList list = new NBTTagList();
        ItemStack itemstack = getStackInSlot(0);
        if (itemstack != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.setByte(Names.NBT.ITEMS, (byte) 0);
            itemstack.writeToNBT(item);
            list.appendTag(item);
        }
        nbtTagCompound.setTag(Names.NBT.ITEMS, list);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int par1) {
        return new int[]{0};
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return "metaFloodlight";
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return true;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = getStackInSlot(slot);

        if (itemstack != null) {
            if (itemstack.stackSize <= count) {
                setInventorySlotContents(slot, null);
            } else {
                itemstack = itemstack.splitStack(count);
                markDirty();
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack itemstack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        inventory[slot] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public void straightSource(boolean remove) {
        for (int i = 1; i <= ConfigHandler.rangeStraightFloodlight; i++) {
            int x = this.xCoord + this.orientation.offsetX * i;
            int y = this.yCoord + this.orientation.offsetY * i;
            int z = this.zCoord + this.orientation.offsetZ * i;
            if (remove) {
                if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                    TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                    light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                }
            } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                setLight(x, y, z);
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
                for (int i = 1; i <= ConfigHandler.rangeConeFloodlight / 4; i++) {
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
                    if (remove) {
                        if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                            light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                        }
                    } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                        setLight(x, y, z);
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
                for (int i = ConfigHandler.rangeConeFloodlight / 9; i <= ConfigHandler.rangeConeFloodlight / 4; i++) {
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
                    if (remove) {
                        if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                            light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                        }
                    } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                        setLight(x, y, z);
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
                for (int i = 1; i <= ConfigHandler.rangeConeFloodlight; i++) {
                    // for 1st light:
                    if (i == 1) {
                        int x = this.xCoord + this.orientation.offsetX;
                        int y = this.xCoord + this.orientation.offsetY;
                        int z = this.xCoord + this.orientation.offsetZ;
                        if (remove) {
                            if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                                light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                            }
                        } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                            setLight(x, y, z);
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
                    if (remove) {
                        if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                            light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                        }
                    } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                        setLight(x, y, z);
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
                for (int i = ConfigHandler.rangeConeFloodlight / 2; i <= ConfigHandler.rangeConeFloodlight; i++) {
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
                    if (remove) {
                        if (worldObj.getBlock(x, y, z) == ModBlocks.blockFLLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(x, y, z);
                            light.removeSource(this.xCoord, this.yCoord, this.zCoord);
                        }
                    } else if (worldObj.getBlock(x, y, z).isAir(worldObj, x, y, z)) {
                        setLight(x, y, z);
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

}
