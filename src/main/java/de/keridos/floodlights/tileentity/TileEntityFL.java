package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.core.NetworkDataList;
import de.keridos.floodlights.core.network.message.TileEntitySyncMessage;
import de.keridos.floodlights.handler.PacketHandler;
import de.keridos.floodlights.reference.Names;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashSet;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the base for all TileEntities within this mod.
 */
@SuppressWarnings({"WeakerAccess", "NullableProblems"})
public class TileEntityFL extends TileEntity {

    protected static final int LIGHT_MODE_STRAIGHT = 0;
    protected static final int LIGHT_MODE_NARROW_CONE = 1;
    protected static final int LIGHT_MODE_WIDE_CONE = 2;

    protected HashSet<EntityPlayer> inventoryAccessors = new HashSet<>();

    protected EnumFacing orientation;
    protected byte state;
    protected String customName;
    protected String owner;
    protected int mode;
    protected boolean inverted;
    protected int color;

    public TileEntityFL() {
        orientation = EnumFacing.SOUTH;
        state = 0;
        customName = "";
        owner = "";
        color = 0;
    }

    public EnumFacing getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = EnumFacing.getFront(orientation);
    }

    public void setOrientation(EnumFacing orientation) {
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
        if (mode >= 0 && mode <= 2)
            this.mode = mode;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.COLOR, color), 2);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.DIRECTION)) {
            this.orientation = EnumFacing.getFront(nbtTagCompound.getByte(Names.NBT.DIRECTION));
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
        if (nbtTagCompound.hasKey(Names.NBT.COLOR)) {
            this.color = nbtTagCompound.getInteger(Names.NBT.COLOR);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setByte(Names.NBT.DIRECTION, (byte) orientation.ordinal());
        nbtTagCompound.setBoolean(Names.NBT.INVERT, inverted);
        nbtTagCompound.setByte(Names.NBT.STATE, state);
        nbtTagCompound.setInteger(Names.NBT.MODE, mode);
        nbtTagCompound.setInteger(Names.NBT.COLOR, color);
        if (this.hasCustomName()) {
            nbtTagCompound.setString(Names.NBT.CUSTOM_NAME, customName);
        }
        if (this.hasOwner()) {
            nbtTagCompound.setString(Names.NBT.OWNER, owner);
        }
        return nbtTagCompound;
    }

    /**
     * This should be called when inventory that belongs to this tile entity is open. Must be used in conjunction
     * with {@link #onInventoryClose(EntityPlayer)}.
     */
    public void onInventoryOpen(EntityPlayer player) {
        inventoryAccessors.add(player);
        syncData(player);
    }

    /**
     * This should be called when inventory that belongs to this tile entity is closed. Must be used in conjunction
     * with {@link #onInventoryOpen(EntityPlayer)}.
     */
    public void onInventoryClose(EntityPlayer player) {
        inventoryAccessors.remove(player);
    }

    /**
     * Triggers data synchronization between serwer and client.
     */
    public void syncData(EntityPlayer player) {
        if (world.isRemote)
            return;

        NetworkDataList data = getSyncData(new NetworkDataList());
        PacketHandler.INSTANCE.sendTo(
                new TileEntitySyncMessage(getPos(), getWorld(), data),
                (EntityPlayerMP) player
        );
    }

    /**
     * Returns the tile entity data that will be sent to client when synchronization is required. Return null if
     * no synchronization is required.
     */
    public NetworkDataList getSyncData(@Nonnull NetworkDataList data) {
        data.add(state);
        data.add(mode);
        data.add(inverted);
        data.add(color);
        return data;
    }

    /**
     * Called when tile entity sync packet arrives. Read data in exactly the same order as in {@link #getSyncData(NetworkDataList)}.
     */
    public void applySyncData(ByteBuf buffer) {
        state = buffer.readByte();
        mode = buffer.readInt();
        inverted = buffer.readBoolean();
        color = buffer.readInt();
    }

    /**
     * Triggers synchronization of this tile entity with all players that are currently using it (open inventory).
     */
    protected void syncWithAccessors() {
        if (world.isRemote)
            return;

        NetworkDataList data = getSyncData(new NetworkDataList());
        for (EntityPlayer player : inventoryAccessors) {
            PacketHandler.INSTANCE.sendTo(
                    new TileEntitySyncMessage(getPos(), getWorld(), data),
                    (EntityPlayerMP) player
            );
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }
}
