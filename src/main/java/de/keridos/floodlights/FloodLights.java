package de.keridos.floodlights;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import de.keridos.floodlights.core.proxy.CommonProxy;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Reference;

import java.util.Map;


/**
 * Created by Nico on 28.02.14.
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class FloodLights {
    @Mod.Instance(Reference.MOD_ID)
    public static FloodLights instance;

    @SidedProxy(clientSide = Reference.PROXY_LOCATION + ".ClientProxy", serverSide = Reference.PROXY_LOCATION + ".CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {

    }

    //@NetworkCheckHandler()
    public boolean matchModVersions(Map<String, String> remoteVersions, Side side) {
        return remoteVersions.containsKey("floodlights") && Reference.VERSION.equals(remoteVersions.get("floodlights"));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {


    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModBlocks.setupBlocks();
        ModBlocks.registerBlocks();
        ModBlocks.registerTileEntities();
    }
}
