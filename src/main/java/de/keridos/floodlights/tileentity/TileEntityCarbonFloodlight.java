package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.GeneralUtil.getBurnTime;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 09/10/2014.
 * This Class describes the carbon floodlight TileEntity.
 */
public class TileEntityCarbonFloodlight extends TileEntityMetaFloodlight {
    public int timeRemaining;

    public TileEntityCarbonFloodlight() {
        super();
        timeRemaining = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.TIME_REMAINING)) {
            this.timeRemaining = nbtTagCompound.getInteger(Names.NBT.TIME_REMAINING);
        }
        NBTTagList list = nbtTagCompound.getTagList(Names.NBT.ITEMS, 10);
        NBTTagCompound item = list.getCompoundTagAt(0);
        int slot = item.getByte(Names.NBT.ITEMS);
        if (slot >= 0 && slot < getSizeInventory()) {
            setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger(Names.NBT.TIME_REMAINING, timeRemaining);
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
    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing facing) {
        return (i == 0 && getBurnTime(itemstack) > 0);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return (getBurnTime(itemstack) > 0);
    }

    @Override
    public void update() {
        World world = this.getWorld();
        if (!world.isRemote) {
            if (timeRemaining == 0 && inventory[0] != null) {
                timeRemaining = ConfigHandler.carbonTime * getBurnTime(inventory[0]) / 1600 * (mode == 0 ? 20 : 10);
                decrStackSize(0, 1);
            }
            if (timeout > 0) {
                timeout--;
                return;
            }
            if (active && timeRemaining > 0) {
                if (update) {
                    removeSource(this.mode);
                    addSource(this.mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).getBlock().getStateFromMeta(this.getOrientation().ordinal() + 6), 2);
                    update = false;
                } else if (!wasActive) {
                    addSource(this.mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).getBlock().getStateFromMeta(this.getOrientation().ordinal() + 6), 2);

                }
                timeRemaining--;
                wasActive = true;
            } else {
                if (wasActive) {
                    removeSource(this.mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).getBlock().getStateFromMeta(this.getOrientation().ordinal()), 2);
                    wasActive = false;
                    timeout = ConfigHandler.timeoutFloodlights;
                    update = false;
                }
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
        World world = this.getWorld();
        if (!world.isRemote) {
            removeSource(this.mode);
            mode = (mode == 2 ? 0 : mode + 1);
            if (mode == 1) {
                timeRemaining /= 4;
            } else if (mode == 0) {
                timeRemaining *= 4;
            }
            if (active && timeRemaining > 0) {
                addSource(this.mode);
            }
            String modeString = (mode == 0 ? Names.Localizations.STRAIGHT : mode == 1 ? Names.Localizations.NARROW_CONE : Names.Localizations.WIDE_CONE);
            player.addChatMessage(new ChatComponentText(safeLocalize(Names.Localizations.MODE) + ": " + safeLocalize(modeString)));
        }
    }
}
