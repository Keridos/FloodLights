package de.keridos.floodlights.compatability;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Reference;
import mezz.jei.api.IItemBlacklist;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * Created by Keridos on 28.02.14.
 * This Class will be used for Mod Compatibility functions.
 */

@Optional.Interface(iface = "mezz.jei.api.IItemBlacklist", modid = "JEI")
public class ModCompatibility implements IItemBlacklist {
    private static ModCompatibility instance = null;

    public IGWHandler igwHandler = null;

    public static boolean IC2Loaded = false;
    public static boolean IGWModLoaded = false;
    public static boolean BCLoaded = false;
    public static boolean CofhCoreLoaded = false;
    public static boolean NEILoaded = false;
    public static boolean JEILoaded = false;
    public static boolean EnderIOLoaded = false;
    public static boolean WrenchAvailable = false;
    public static boolean ColoredLightCoreLoaded = false;

    private static ArrayList<ItemStack> JEIBlacklist = new ArrayList<>();

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
        JEILoaded = Loader.isModLoaded("JEI");
        EnderIOLoaded = Loader.isModLoaded("EnderIO");
        ColoredLightCoreLoaded = Loader.isModLoaded("coloredlightcore");
    }

    @Optional.Method(modid = "NotEnoughItems")
    private void hideNEIItems() {
        //hideItem(new ItemStack(ModBlocks.blockPhantomLight));
        //hideItem(new ItemStack(ModBlocks.blockUVLightBlock));
    }

    @Optional.Method(modid = "JEI")
    private void hideJEIItems() {
        addItemToBlacklist(new ItemStack(ModBlocks.blockPhantomLight));
        addItemToBlacklist(new ItemStack(ModBlocks.blockUVLightBlock));
    }

    @Optional.Method(modid = "JEI")
    @Override
    public void addItemToBlacklist(@Nonnull ItemStack itemStack) {
         JEIBlacklist.add(itemStack);
    }

    @Optional.Method(modid = "JEI")
    @Override
    public void removeItemFromBlacklist(@Nonnull ItemStack itemStack) {
         JEIBlacklist.remove(itemStack);
    }

    @Optional.Method(modid = "JEI")
    @Override
    public boolean isItemBlacklisted(@Nonnull ItemStack itemStack) {
        if (JEIBlacklist.size() > 0) {
            for (ItemStack itemStackTemp: JEIBlacklist) {
                if (itemStack.getItem().equals(itemStackTemp.getItem())) {
                   return true;
                }
            }
        }
        return false;
    }

    private void addVersionCheckerInfo() {
        NBTTagCompound versionchecker = new NBTTagCompound();
        versionchecker.setString("curseProjectName", "224728-floodlights");
        versionchecker.setString("curseFilenameParser", "FloodLights-@MCVERSION@-[]");
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
        if (JEILoaded) {
            hideJEIItems();
        }

        addVersionCheckerInfo();
        FMLInterModComms.sendMessage("Waila", "register", "de.keridos.floodlights.compatability.WailaTileHandler.callbackRegister");
        WrenchAvailable = (BCLoaded || EnderIOLoaded || IC2Loaded || CofhCoreLoaded);
    }
}
