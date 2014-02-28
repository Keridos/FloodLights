package de.keridos.floodlights;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import de.keridos.floodlights.core.PacketHandler;
import de.keridos.floodlights.core.proxy.CommonProxy;
import de.keridos.floodlights.lib.Reference;


/**
 * Created by Nico on 28.02.14.
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = {Reference.CHANNEL}, packetHandler = PacketHandler.class)
public class FloodLights {
    @Mod.Instance(Reference.MOD_ID)
    public static FloodLights instance;

    @SidedProxy(clientSide = Reference.PROXY_LOCATION + ".ClientProxy", serverSide = Reference.PROXY_LOCATION + ".CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {

    }
}
