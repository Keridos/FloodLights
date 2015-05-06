package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.reference.Names;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

/**
 * Created by Keridos on 06.05.2015.
 * This Class
 */
public class TileEntityMetaFloodlight extends TileEntityFL {
    protected boolean active;
    protected int timeout;

    public TileEntityMetaFloodlight() {
        super();
        Random rand = new Random();
        timeout = rand.nextInt((500 - 360) + 1) + 360;
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

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.TIMEOUT)) {
            this.timeout = nbtTagCompound.getInteger(Names.NBT.TIMEOUT);
        } else {
            Random rand = new Random();
            timeout = rand.nextInt((500 - 360) + 1) + 360;
        }
        if (nbtTagCompound.hasKey(Names.NBT.STATE)) {
            this.active = (nbtTagCompound.getInteger(Names.NBT.STATE) != 0);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger(Names.NBT.TIMEOUT, timeout);
    }
}
