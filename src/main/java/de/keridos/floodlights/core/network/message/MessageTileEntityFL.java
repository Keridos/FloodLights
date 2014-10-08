package de.keridos.floodlights.core.network.message;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.keridos.floodlights.tileentity.TileEntityFL;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Keridos on 05.10.14.
 * This Class is the Message that the electric floodlights TileEntity uses.
 */
public class MessageTileEntityFL implements IMessage, IMessageHandler<MessageTileEntityFL, IMessage> {
    public int x, y, z;
    public byte orientation, state;
    public String customName, owner;

    public MessageTileEntityFL() {
    }

    public MessageTileEntityFL(TileEntityFL tileEntityFL) {
        this.x = tileEntityFL.xCoord;
        this.y = tileEntityFL.yCoord;
        this.z = tileEntityFL.zCoord;
        this.orientation = (byte) tileEntityFL.getOrientation().ordinal();
        this.state = (byte) tileEntityFL.getState();
        this.customName = tileEntityFL.getCustomName();
        this.owner = tileEntityFL.getOwner();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.orientation = buf.readByte();
        this.state = buf.readByte();
        int customNameLength = buf.readInt();
        this.customName = new String(buf.readBytes(customNameLength).array());
        int ownerLength = buf.readInt();
        this.owner = new String(buf.readBytes(ownerLength).array());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(orientation);
        buf.writeByte(state);
        buf.writeInt(customName.length());
        buf.writeBytes(customName.getBytes());
        buf.writeInt(owner.length());
        buf.writeBytes(owner.getBytes());
    }

    @Override
    public IMessage onMessage(MessageTileEntityFL message, MessageContext ctx) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
        if (tileEntity instanceof TileEntityFL) {
            ((TileEntityFL) tileEntity).setOrientation(message.orientation);
            ((TileEntityFL) tileEntity).setState(message.state);
            ((TileEntityFL) tileEntity).setCustomName(message.customName);
            ((TileEntityFL) tileEntity).setOwner(message.owner);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("MessageTileEntityFL - x:%s, y:%s, z:%s, orientation:%s, state:%s, customName:%s, owner:%s", x, y, z, orientation, state, customName, owner);
    }
}
