package de.keridos.floodlights.core.network.message;

import de.keridos.floodlights.handler.PacketHandler;
import de.keridos.floodlights.tileentity.TileEntityFL;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A message type used to synchronize tile entities' states between client and server.
 */
public class TileEntitySyncMessage implements IMessage {

    private NonNullList<Object> data;
    private ByteBuf buffer;

    private int x;
    private int y;
    private int z;
    private int dim;

    @SuppressWarnings("unused")
    public TileEntitySyncMessage() {
    }

    public TileEntitySyncMessage(BlockPos pos, World world, NonNullList<Object> data) {
        this.data = data;
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        dim = world.provider.getDimension();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        dim = buf.readInt();
        buffer = buf.copy();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(dim);
        PacketHandler.encode(data.toArray(), buf);
    }

    public static class Handler implements IMessageHandler<TileEntitySyncMessage, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(TileEntitySyncMessage message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            if (!player.world.isBlockLoaded(pos))
                return null;

            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntity tileEntity = player.world.getTileEntity(pos);
                if (tileEntity instanceof TileEntityFL) {
                    ((TileEntityFL) tileEntity).applySyncData(message.buffer);
                    ((TileEntityFL) tileEntity).rerenderBlock();
                }
            });

            return null;
        }
    }
}
