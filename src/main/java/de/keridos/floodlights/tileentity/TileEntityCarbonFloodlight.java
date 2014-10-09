package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.handler.LightHandler;
import de.keridos.floodlights.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by Keridos on 09/10/2014.
 * This Class
 */
public class TileEntityCarbonFloodlight extends TileEntityFL implements ISidedInventory {
    private boolean inverted = false;
    private boolean active = false;
    private boolean wasActive = false;
    private int timeout;
    public int timeRemaining;
    private LightHandler lightHandler = LightHandler.getInstance();
    private ConfigHandler configHandler = ConfigHandler.getInstance();
    private ItemStack[] inventory;

    public TileEntityCarbonFloodlight() {
        Random rand = new Random();
        timeout = rand.nextInt((500 - 360) + 1) + 360;
        inventory = new ItemStack[1];
        timeRemaining = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.INVERT)) {
            this.inverted = nbtTagCompound.getBoolean(Names.NBT.INVERT);
        }
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
        if (nbtTagCompound.hasKey(Names.NBT.TIME_REMAINING)) {
            this.timeRemaining = nbtTagCompound.getInteger(Names.NBT.TIME_REMAINING);
        }
        NBTTagList list = nbtTagCompound.getTagList("ItemsCarbonFloodlight", 10);
        NBTTagCompound item = list.getCompoundTagAt(0);
        int slot = item.getByte("ItemsCarbonFloodlight");

        if (slot >= 0 && slot < getSizeInventory()) {
            setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.INVERT, inverted);
        nbtTagCompound.setBoolean(Names.NBT.WAS_ACTIVE, wasActive);
        nbtTagCompound.setInteger(Names.NBT.TIMEOUT, timeout);
        nbtTagCompound.setInteger(Names.NBT.TIME_REMAINING, timeRemaining);
        NBTTagList list = new NBTTagList();
        ItemStack itemstack = getStackInSlot(0);
        if (itemstack != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.setByte("ItemsCarbonFloodlight", (byte) 0);
            itemstack.writeToNBT(item);
            list.appendTag(item);
        }
        nbtTagCompound.setTag("ItemsCarbonFloodlight", list);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int par1) {
        int[] slots = {0};
        return slots;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return "carbonFloodlight";
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        if (i == 0) {
            return true;
        }
        return false;
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
        if (itemstack.getItem() == Items.coal) {
            return true;
        }
        return false;
    }

    @Override
    public void updateEntity() {
        World world = this.getWorldObj();
        world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.getOrientation().ordinal(), 2);
        if (!world.isRemote && world.getTotalWorldTime() % 20 == 0) {
            //world.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        if (!world.isRemote) {
            ForgeDirection direction = this.getOrientation();
            if (timeRemaining == 0 && inventory[0] != null) {
                decrStackSize(0, 1);
                timeRemaining = configHandler.carbonTime * 20;
            }
            if (((active ^ inverted) && timeRemaining > 0)) {
                if (!wasActive || world.getTotalWorldTime() % timeout == 0) {
                    if (world.getTotalWorldTime() % timeout == 0) {
                        lightHandler.removeSource(world, this.xCoord, this.yCoord, this.zCoord, direction, 0);
                        lightHandler.addSource(world, this.xCoord, this.yCoord, this.zCoord, direction, 0);
                    } else {
                        lightHandler.addSource(world, this.xCoord, this.yCoord, this.zCoord, direction, 0);
                    }
                }
                timeRemaining--;
                wasActive = true;
            } else {
                if (wasActive) {
                    lightHandler.removeSource(world, this.xCoord, this.yCoord, this.zCoord, direction, 0);
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
}
