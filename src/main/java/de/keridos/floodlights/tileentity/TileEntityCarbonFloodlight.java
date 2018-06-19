package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.core.NetworkDataList;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
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
    public NetworkDataList getSyncData(@Nonnull NetworkDataList data) {
        super.getSyncData(data);
        data.add(timeRemaining);
        return data;
    }

    @Override
    public void applySyncData(ByteBuf buffer) {
        super.applySyncData(buffer);
        timeRemaining = buffer.readInt();
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            if (timeRemaining == 0 && !inventory.getStackInSlot(0).isEmpty()) {
                int factor = _mode == LIGHT_MODE_STRAIGHT ? 20 : 10;
                timeRemaining = ConfigHandler.carbonTime * getBurnTime(inventory.getStackInSlot(0)) / 1600 * factor;
                inventory.extractItem(0, 1, false);
                syncWithAccessors();
            }
            if (timeout > 0) {
                timeout--;
                return;
            }
            if (active && timeRemaining > 0) {
                if (update) {
                    removeSource(_mode);
                    addSource(_mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    update = false;
                } else if (!wasActive) {
                    addSource(_mode);
                    Logger.getGlobal().info("rotation: " + this.orientation.toString());
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                }
                timeRemaining--;
                wasActive = true;
            } else {
                if (wasActive) {
                    removeSource(_mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, false), 2);
                    wasActive = false;
                    timeout = ConfigHandler.timeoutFloodlights;
                    update = false;
                }
            }
        } else {
            if (active && timeRemaining > 0)
                timeRemaining--;
        }
    }

    private void addSource(int mode) {
        switch (mode) {
            case LIGHT_MODE_STRAIGHT:
                straightSource(false);
                break;
            case LIGHT_MODE_NARROW_CONE:
                narrowConeSource(false);
                break;
            case LIGHT_MODE_WIDE_CONE:
                wideConeSource(false);
                break;
        }
    }

    private void removeSource(int mode) {
        switch (mode) {
            case LIGHT_MODE_STRAIGHT:
                straightSource(true);
                break;
            case LIGHT_MODE_NARROW_CONE:
                narrowConeSource(true);
                break;
            case LIGHT_MODE_WIDE_CONE:
                wideConeSource(true);
                break;
        }
    }

    public void changeMode(EntityPlayer player) {
        World world = this.getWorld();
        if (!world.isRemote) {
            removeSource(_mode);
            _mode = (_mode == LIGHT_MODE_WIDE_CONE ? LIGHT_MODE_STRAIGHT : _mode + 1);
            if (_mode == LIGHT_MODE_NARROW_CONE)
                timeRemaining /= 4;
            else if (_mode == LIGHT_MODE_STRAIGHT)
                timeRemaining *= 4;

            if (active && timeRemaining > 0)
                addSource(_mode);

            String modeString = "";
            switch (_mode) {
                case LIGHT_MODE_STRAIGHT:
                    modeString = Names.Localizations.STRAIGHT;
                    break;
                case LIGHT_MODE_NARROW_CONE:
                    modeString = Names.Localizations.NARROW_CONE;
                    break;
                case LIGHT_MODE_WIDE_CONE:
                    modeString = Names.Localizations.WIDE_CONE;
                    break;
            }
            player.sendMessage(new TextComponentString(safeLocalize(Names.Localizations.MODE) + ": " + safeLocalize(modeString)));
        }
    }
}
