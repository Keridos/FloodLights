package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.logging.Logger;

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
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger(Names.NBT.TIME_REMAINING, timeRemaining);
        return nbtTagCompound;
    }

    @Override
    protected boolean canInsertItem(ItemStack itemStack) {
        return getBurnTime(itemStack) > 0;
    }

    @Override
    public void update() {
        super.update();
        World world = this.getWorld();
        if (!world.isRemote) {
            if (timeRemaining == 0 && !inventory.getStackInSlot(0).isEmpty()) {
                timeRemaining = ConfigHandler.carbonTime * getBurnTime(inventory.getStackInSlot(0)) / 1600 * (mode == 0 ? 20 : 10);
                inventory.extractItem(0, 1, false);
            }
            if (timeout > 0) {
                timeout--;
                return;
            }
            if (active && timeRemaining > 0) {
                if (update) {
                    removeSource(this.mode);
                    addSource(this.mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    update = false;
                } else if (!wasActive) {
                    addSource(this.mode);
                    Logger.getGlobal().info("rotation: "+this.orientation.toString());
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                }
                timeRemaining--;
                wasActive = true;
            } else {
                if (wasActive) {
                    removeSource(this.mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, false), 2);
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
            player.sendMessage(new TextComponentString(safeLocalize(Names.Localizations.MODE) + ": " + safeLocalize(modeString)));
        }
    }
}
