package de.keridos.floodlights;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import de.keridos.floodlights.client.gui.GuiHandler;
import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.core.EventListener;
import de.keridos.floodlights.core.FMLEventListener;
import de.keridos.floodlights.core.network.PacketHandler;
import de.keridos.floodlights.core.proxy.CommonProxy;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.handler.RecipeHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.init.ModItems;
import de.keridos.floodlights.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.util.Map;

/**
 * Created by Keridos on 28.02.14.
 * This Class is the Main Class of the Mod.
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class FloodLights {
    @Mod.Instance(Reference.MOD_ID)
    public static FloodLights instance;

    @SidedProxy(clientSide = Reference.PROXY_LOCATION + ".ClientProxy", serverSide = Reference.PROXY_LOCATION + ".CommonProxy")
    public static CommonProxy proxy;

    private ConfigHandler configHandler = ConfigHandler.getInstance();
    private static RecipeHandler recipeHandler = RecipeHandler.getInstance();
    private static ModCompatibility modCompatibility = ModCompatibility.getInstance();
    private static GuiHandler Gui = null;

    @NetworkCheckHandler()
    public boolean matchModVersions(Map<String, String> remoteVersions, Side side) {
        return remoteVersions.containsKey("FloodLights") && Reference.VERSION.equals(remoteVersions.get("FloodLights"));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configHandler.initConfig(new Configuration(event.getSuggestedConfigurationFile()));
        ModBlocks.setupBlocks();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModBlocks.registerBlocks();
        ModBlocks.registerTileEntities();
        ModItems.init();
        registerEventListeners();
        PacketHandler.init();
        recipeHandler.initRecipes();
        modCompatibility.checkForMods();
        proxy.initRenderers();
        proxy.initSounds();
        proxy.initHandlers();
        Gui = GuiHandler.getInstance();
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {

    }

    public void registerEventListeners() {
        MinecraftForge.EVENT_BUS.register(EventListener.getInstance());
        // some events, especially tick, are handled on FML bus
        FMLCommonHandler.instance().bus().register(FMLEventListener.getInstance());
    }
}
