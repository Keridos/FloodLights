package de.keridos.floodlights.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import de.keridos.floodlights.core.network.message.MessageTileEntityFL;
import de.keridos.floodlights.reference.Reference;

/**
 * Created by Keridos on 05.10.14.
 * This Class manages all Messages that can be sent from the server to the client.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL);

    public static void init() {
        INSTANCE.registerMessage(MessageTileEntityFL.class, MessageTileEntityFL.class, 0, Side.CLIENT);
    }
}
