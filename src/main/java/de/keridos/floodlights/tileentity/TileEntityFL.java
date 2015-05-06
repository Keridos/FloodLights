package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.core.network.PacketHandler;
import de.keridos.floodlights.core.network.message.MessageTileEntityFL;
import de.keridos.floodlights.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the base for all TileEntities within this mod.
 */
public class TileEntityFL extends TileEntity {
    protected ForgeDirection orientation;
    protected byte state;
    protected String customName;
    protected String owner;
    protected int mode;
    protected boolean inverted;

    public TileEntityFL() {
        orientation = ForgeDirection.SOUTH;
        state = 0;
        customName = "";
        owner = "";
    }

    public ForgeDirection getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = ForgeDirection.getOrientation(orientation);
    }

    public void setOrientation(ForgeDirection orientation) {
        this.orientation = orientation;
    }

    public short getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.DIRECTION)) {
            this.orientation = ForgeDirection.getOrientation(nbtTagCompound.getByte(Names.NBT.DIRECTION));
        }
        if (nbtTagCompound.hasKey(Names.NBT.INVERT)) {
            this.inverted = nbtTagCompound.getBoolean(Names.NBT.INVERT);
        }
        if (nbtTagCompound.hasKey(Names.NBT.STATE)) {
            this.state = nbtTagCompound.getByte(Names.NBT.STATE);
        }
        if (nbtTagCompound.hasKey(Names.NBT.CUSTOM_NAME)) {
            this.customName = nbtTagCompound.getString(Names.NBT.CUSTOM_NAME);
        }
        if (nbtTagCompound.hasKey(Names.NBT.OWNER)) {
            this.owner = nbtTagCompound.getString(Names.NBT.OWNER);
        }
        if (nbtTagCompound.hasKey(Names.NBT.MODE)) {
            this.mode = nbtTagCompound.getInteger(Names.NBT.MODE);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setByte(Names.NBT.DIRECTION, (byte) orientation.ordinal());
        nbtTagCompound.setBoolean(Names.NBT.INVERT, inverted);
        nbtTagCompound.setByte(Names.NBT.STATE, state);
        nbtTagCompound.setInteger(Names.NBT.MODE, mode);
        if (this.hasCustomName()) {
            nbtTagCompound.setString(Names.NBT.CUSTOM_NAME, customName);
        }
        if (this.hasOwner()) {
            nbtTagCompound.setString(Names.NBT.OWNER, owner);
        }
    }

    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    public boolean hasOwner() {
        return owner != null && owner.length() > 0;
    }

    public boolean getInverted() {
        return this.inverted;
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityFL(this));
    }
}
