package de.keridos.floodlights.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import de.keridos.floodlights.core.network.message.MessageTileEntityFL;
import de.keridos.floodlights.reference.Reference;

/**
 * Created by Nico on 05/10/2014.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID.toLowerCase());

    public static void init() {
        INSTANCE.registerMessage(MessageTileEntityFL.class, MessageTileEntityFL.class, 0, Side.CLIENT);
    }
}
