package de.keridos.floodlights.handler;


import de.keridos.floodlights.core.network.message.MessageTileEntityFL;
import de.keridos.floodlights.reference.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Keridos on 05.10.14.
 * This Class manages all Messages that can be sent from the server to the client.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL);

    public static void init() {
        //INSTANCE.registerMessage(MessageTileEntityFL.MessageTileEntityFLHandler.class, MessageTileEntityFL.class, 0, Side.CLIENT);
    }
}
