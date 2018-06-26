package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.core.NetworkDataList;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import static de.keridos.floodlights.util.GeneralUtil.getBurnTime;

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
        if (!isReady())
            return;

        // Update energy storage
        if (active && timeRemaining > 0)
            timeRemaining--;

        if (world.isRemote)
            return;

        // Refill the energy storage
        if (timeRemaining == 0 && !inventory.getStackInSlot(0).isEmpty()) {
            int factor = mode == LIGHT_MODE_STRAIGHT ? 20 : 10;
            timeRemaining = ConfigHandler.carbonTime * getBurnTime(inventory.getStackInSlot(0)) / 1600 * factor;
            inventory.extractItem(0, 1, false);
            syncWithAccessors();
        }
    }

    @Override
    protected boolean hasEnergy() {
        return timeRemaining > 0;
    }

    @Override
    public void changeMode(EntityPlayer player) {
        int newMode = (mode == LIGHT_MODE_WIDE_CONE ? LIGHT_MODE_STRAIGHT : mode + 1);
        if (newMode == LIGHT_MODE_NARROW_CONE)
            timeRemaining /= 4;
        else if (newMode == LIGHT_MODE_STRAIGHT)
            timeRemaining *= 4;

        super.changeMode(player);

    }
}
