package de.keridos.floodlights.core.network.message;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.tileentity.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Keridos on 05.10.14.
 * This Class is the Message that the electric floodlights TileEntity uses.
 */
public class MessageTileEntityFL implements IMessage {
    public int x, y, z, timeRemaining, color, rfStorage;
    public byte orientation, state;
    public boolean rotationState, wasActive;
    public String customName, owner;

    public static class MessageTileEntityFLHandler implements IMessageHandler<MessageTileEntityFL, IMessage> {
        public MessageTileEntityFLHandler() {
        }

        @Override
        public IMessage onMessage(MessageTileEntityFL messagein, MessageContext ctx) {
            final MessageTileEntityFL message = messagein;
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileEntity tileEntity = FloodLights.proxy.getWorld().getTileEntity(new BlockPos(message.x, message.y, message.z));
                    if (tileEntity instanceof TileEntityFL) {
                        ((TileEntityFL) tileEntity).setOrientation(message.orientation);
                        ((TileEntityFL) tileEntity).setState(message.state);
                        ((TileEntityFL) tileEntity).setCustomName(message.customName);
                        ((TileEntityFL) tileEntity).setOwner(message.owner);
                        ((TileEntityFL) tileEntity).setColor(message.color);
                    }
                    if (tileEntity instanceof TileEntityCarbonFloodlight) {
                        ((TileEntityCarbonFloodlight) tileEntity).timeRemaining = message.timeRemaining;
                    }
                    if (tileEntity instanceof TileEntitySmallFloodlight) {
                        ((TileEntitySmallFloodlight) tileEntity).setRotationState(message.rotationState);
                    }
                    if (tileEntity instanceof TileEntityFLElectric) {
                        ((TileEntityFLElectric) tileEntity).setEnergyStored(message.rfStorage);
                    }
                    if (tileEntity instanceof TileEntityMetaFloodlight) {
                        ((TileEntityMetaFloodlight) tileEntity).setWasActive(message.wasActive);
                    }

                }
            });
            return null;
        }
    }

    public MessageTileEntityFL() {
    }

    public MessageTileEntityFL(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityFL) {
            TileEntityFL tileEntityFL = (TileEntityFL) tileEntity;
            this.x = tileEntityFL.getPos().getX();
            this.y = tileEntityFL.getPos().getY();
            this.z = tileEntityFL.getPos().getZ();
            this.orientation = (byte) tileEntityFL.getOrientation().ordinal();
            this.state = (byte) tileEntityFL.getState();
            this.customName = tileEntityFL.getCustomName();
            this.owner = tileEntityFL.getOwner();
            this.color = tileEntityFL.getColor();
            if (tileEntity instanceof TileEntityCarbonFloodlight) {
                this.timeRemaining = ((TileEntityCarbonFloodlight) tileEntity).timeRemaining;
            } else {
                this.timeRemaining = 0;
            }
            if (tileEntity instanceof TileEntitySmallFloodlight) {
                this.rotationState = ((TileEntitySmallFloodlight) tileEntity).getRotationState();
            } else {
                this.rotationState = false;
            }
            if (tileEntity instanceof TileEntityFLElectric) {
                this.rfStorage = ((TileEntityFLElectric) tileEntity).getEnergyStored(EnumFacing.UP);
            } else {
                this.rfStorage = 0;
            }
            if (tileEntity instanceof TileEntityMetaFloodlight) {
                this.wasActive = ((TileEntityMetaFloodlight) tileEntity).getWasActive();
            } else {
                this.wasActive = false;
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.timeRemaining = buf.readInt();
        this.orientation = buf.readByte();
        this.state = buf.readByte();
        int customNameLength = buf.readInt();
        this.customName = new String(buf.readBytes(customNameLength).array());
        int ownerLength = buf.readInt();
        this.owner = new String(buf.readBytes(ownerLength).array());
        this.rotationState = buf.readBoolean();
        this.color = buf.readInt();
        this.rfStorage = buf.readInt();
        this.wasActive = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(timeRemaining);
        buf.writeByte(orientation);
        buf.writeByte(state);
        buf.writeInt(customName.length());
        buf.writeBytes(customName.getBytes());
        buf.writeInt(owner.length());
        buf.writeBytes(owner.getBytes());
        buf.writeBoolean(rotationState);
        buf.writeInt(color);
        buf.writeInt(rfStorage);
        buf.writeBoolean(wasActive);
    }

    @Override
    public String toString() {
        return String.format("MessageTileEntityFL - x:%s, y:%s, z:%s, timeRemaining:%s, orientation:%s, state:%s, customName:%s, owner:%s", x, y, z, timeRemaining, orientation, state, customName, owner);
    }
}
