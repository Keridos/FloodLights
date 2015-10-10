package de.keridos.floodlights.compatability;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInterModComms;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static codechicken.nei.api.API.hideItem;

/**
 * Created by Keridos on 28.02.14.
 * This Class will be used for Mod Compatibility functions.
 */
public class ModCompatibility {
    private static ModCompatibility instance = null;

    public IGWHandler igwHandler = null;

    public static boolean IC2Loaded = false;
    public static boolean IGWModLoaded = false;
    public static boolean BCLoaded = false;
    public static boolean CofhCoreLoaded = false;
    public static boolean NEILoaded = false;
    public static boolean EnderIOLoaded = false;
    public static boolean WrenchAvailable = false;
    public static boolean ColoredLightCoreLoaded = false;

    private ModCompatibility() {
    }

    public static ModCompatibility getInstance() {
        if (instance == null) {
            instance = new ModCompatibility();
        }
        return instance;
    }

    private void checkForMods() {
        IC2Loaded = Loader.isModLoaded("IC2");
        IGWModLoaded = Loader.isModLoaded("IGWMod");
        BCLoaded = ModAPIManager.INSTANCE.hasAPI("BuildCraftAPI|tools");
        CofhCoreLoaded = ModAPIManager.INSTANCE.hasAPI("CoFHAPI|item");
        NEILoaded = Loader.isModLoaded("NotEnoughItems");
        NEILoaded = Loader.isModLoaded("EnderIO");
        ColoredLightCoreLoaded = Loader.isModLoaded("coloredlightcore");
    }

    @Optional.Method(modid = "NotEnoughItems")
    private void hideNEIItems() {
        hideItem(new ItemStack(ModBlocks.blockPhantomLight));
        hideItem(new ItemStack(ModBlocks.blockUVLightBlock));
    }

    private void addVersionCheckerInfo() {
        NBTTagCompound versionchecker = new NBTTagCompound();
        versionchecker.setString("curseProjectName", "224728-floodlights");
        versionchecker.setString("curseFilenameParser", "FloodLights-1.7.10-[]");
        versionchecker.setString("modDisplayName", "FloodLights");
        versionchecker.setString("oldVersion", Reference.VERSION);
        FMLInterModComms.sendRuntimeMessage("floodlights", "VersionChecker", "addCurseCheck", versionchecker);
    }

    public void performModCompat() {
        checkForMods();
        if (ConfigHandler.IGWNotifierEnabled) {
            new IGWSupportNotifier();
        }
        if (NEILoaded) {
            hideNEIItems();
        }
        addVersionCheckerInfo();
        FMLInterModComms.sendMessage("Waila", "register", "de.keridos.floodlights.compatability.WailaTileHandler.callbackRegister");
        WrenchAvailable = (BCLoaded || EnderIOLoaded || IC2Loaded || CofhCoreLoaded);
    }
}
