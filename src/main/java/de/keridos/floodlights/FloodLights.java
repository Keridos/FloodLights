package de.keridos.floodlights;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import de.keridos.floodlights.core.EventListener;
import de.keridos.floodlights.core.FMLEventListener;
import de.keridos.floodlights.core.proxy.CommonProxy;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Reference;
import net.minecraftforge.common.MinecraftForge;

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

    public void registerEventListeners() {
        MinecraftForge.EVENT_BUS.register(EventListener.getInstance());
        // some events, especially tick, are handled on FML bus
        FMLCommonHandler.instance().bus().register(FMLEventListener.getInstance());
    }

    @NetworkCheckHandler()
    public boolean matchModVersions(Map<String, String> remoteVersions, Side side) {
        return remoteVersions.containsKey("FloodLights") && Reference.VERSION.equals(remoteVersions.get("FloodLights"));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {


    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModBlocks.setupBlocks();
        ModBlocks.registerBlocks();
        ModBlocks.registerTileEntities();
        registerEventListeners();
        proxy.initRenderers();
    }
}
