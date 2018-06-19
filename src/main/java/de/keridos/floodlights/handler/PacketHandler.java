package de.keridos.floodlights.handler;


import de.keridos.floodlights.core.network.message.MessageTileEntityFL;
import de.keridos.floodlights.core.network.message.TileEntitySyncMessage;
import de.keridos.floodlights.reference.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Keridos on 05.10.14.
 * This Class manages all Messages that can be sent from the server to the client.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL);

    @SuppressWarnings("UnusedAssignment")
    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(MessageTileEntityFL.MessageTileEntityFLHandler.class, MessageTileEntityFL.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(TileEntitySyncMessage.Handler.class, TileEntitySyncMessage.class, id++, Side.CLIENT);
    }

    /**
     * Encodes list of objects into given byte buffer.
     * <br>
     * Copied from Mekanism
     */
    public static void encode(Object[] dataValues, ByteBuf output) {
        try {
            for (Object data : dataValues) {
                if (data instanceof Byte)
                    output.writeByte((Byte) data);
                else if (data instanceof Integer)
                    output.writeInt((Integer) data);
                else if (data instanceof Short)
                    output.writeShort((Short) data);
                else if (data instanceof Long)
                    output.writeLong((Long) data);
                else if (data instanceof Boolean)
                    output.writeBoolean((Boolean) data);
                else if (data instanceof Double)
                    output.writeDouble((Double) data);
                else if (data instanceof Float)
                    output.writeFloat((Float) data);
                else if (data instanceof String)
                    output.writeBytes(((String) data).getBytes(Charset.forName("UTF-8")));
                else if (data instanceof EnumFacing)
                    output.writeInt(((EnumFacing) data).ordinal());
                /*
                That part isn't necessary right now

                else if (data instanceof ItemStack)
                    writeStack(output, (ItemStack) data);
                else if (data instanceof NBTTagCompound)
                    writeNBT(output, (NBTTagCompound) data);*/
                else if (data instanceof int[]) {
                    for (int i : (int[]) data) {
                        output.writeInt(i);
                    }
                } else if (data instanceof byte[]) {
                    for (byte b : (byte[]) data) {
                        output.writeByte(b);
                    }
                } else if (data instanceof List)
                    encode(((List) data).toArray(), output);
                else
                    throw new RuntimeException("Un-encodable data passed to encode(): " + data + ", full data: " + Arrays.toString(dataValues));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
