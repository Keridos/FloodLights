package de.keridos.floodlights.core;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * Created by Nico on 28.02.14.
 */
public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager,
                             Packet250CustomPayload packet, Player playerEntity) {

    }

}
