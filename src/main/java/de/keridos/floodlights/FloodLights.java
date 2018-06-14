package de.keridos.floodlights;

import de.keridos.floodlights.compatability.ModCompatibility;
import de.keridos.floodlights.core.proxy.CommonProxy;
import de.keridos.floodlights.handler.*;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.init.ModItems;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.util.RandomUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

/**
 * Created by Keridos on 28.02.14.
 * This Class is the Main Class of the Mod.
 */

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class FloodLights {
    @Mod.Instance(Reference.MOD_ID)
    public static FloodLights instance;

    @SidedProxy(clientSide = Reference.PROXY_LOCATION + ".ClientProxy", serverSide = Reference.PROXY_LOCATION + ".CommonProxy")
    public static CommonProxy proxy;

    private static ConfigHandler configHandler = null;
    private static RecipeHandler recipeHandler = null;
    private static ModCompatibility modCompatibility = null;
    private static GuiHandler Gui;

    @NetworkCheckHandler()
    public boolean matchModVersions(Map<String, String> remoteVersions, Side side) {
        return remoteVersions.containsKey(Reference.MOD_ID)
                && Reference.VERSION.equals(remoteVersions.get(Reference.MOD_ID));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configHandler = ConfigHandler.getInstance();
        configHandler.initConfig(new Configuration(event.getSuggestedConfigurationFile()));
        ModBlocks.registerTileEntities();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        recipeHandler = RecipeHandler.getInstance();
        modCompatibility = ModCompatibility.getInstance();
        PacketHandler.init();
        recipeHandler.initRecipes();
        modCompatibility.performModCompat();
        registerEventListeners();
        RandomUtil.init();
        proxy.initRenderers();
        proxy.initSounds();
        proxy.initHandlers();
        Gui = GuiHandler.getInstance();
    }

    @Mod.EventHandler
    public static void postInit(FMLServerStartingEvent event) {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void regsiterItems(RegistryEvent.Register<Item> event) {
        ModItems.registerItems(event.getRegistry());
    }

    private void registerEventListeners() {
        // MinecraftForge.EVENT_BUS.register(this);
        // some events, especially tick, are handled on FML bus
        //FMLCommonHandler.instance().bus().register(FMLEventListener.getInstance());
    }
}
