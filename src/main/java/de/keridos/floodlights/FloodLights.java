package de.keridos.floodlights;

import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.core.proxy.CommonProxy;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.handler.GuiHandler;
import de.keridos.floodlights.handler.PacketHandler;
import de.keridos.floodlights.handler.RecipeHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.init.ModItems;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.util.RenderUtil;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

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
    private static GuiHandler Gui;

    @NetworkCheckHandler()
    public boolean matchModVersions(Map<String, String> remoteVersions, Side side) {
        return remoteVersions.containsKey("FloodLights") && Reference.VERSION.equals(remoteVersions.get("FloodLights"));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configHandler.initConfig(new Configuration(event.getSuggestedConfigurationFile()));
        ModBlocks.setupBlocks();
        ModBlocks.registerBlocks();
        ModBlocks.registerTileEntities();
        proxy.initItemModels();
        ModItems.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PacketHandler.init();
        recipeHandler.initRecipes();
        modCompatibility.performModCompat();
        registerEventListeners();
        proxy.initRenderers();
        proxy.initSounds();
        proxy.initHandlers();
        RenderUtil.setupColors();
        Gui = GuiHandler.getInstance();
    }

    @Mod.EventHandler
    public static void postInit(FMLServerStartingEvent event) {

    }

    public void registerEventListeners() {
        //MinecraftForge.EVENT_BUS.register(EventListener.getInstance());
        // some events, especially tick, are handled on FML bus
        //FMLCommonHandler.instance().bus().register(FMLEventListener.getInstance());
    }
}
