package de.keridos.floodlights.compatability;

import com.InfinityRaider.AgriCraft.api.API;
import com.InfinityRaider.AgriCraft.api.v2.APIv2;
import com.InfinityRaider.AgriCraft.api.v2.ICrop;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInterModComms;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Reference;
import de.keridos.floodlights.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

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
    public static boolean ACLoaded = false;

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
        EnderIOLoaded = Loader.isModLoaded("EnderIO");
        ColoredLightCoreLoaded = Loader.isModLoaded("coloredlightcore");
        ACLoaded = Loader.isModLoaded("AgriCraft");
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

    @Optional.Method(modid = "AgriCraft")
    public boolean isBlockValidAgriCraftSeed(Block block, World world, BlockPos blockPos) {
        if (block instanceof ICrop && ((APIv2) API.getAPI(2)).getCrop(world, blockPos.posX, blockPos.posY, blockPos.posZ).hasPlant() && ((APIv2) API.getAPI(2)).getCrop(world, blockPos.posX, blockPos.posY, blockPos.posZ).getPlant().getGrowthRequirement().canGrow(world, blockPos.posX, blockPos.posY, blockPos.posZ)) {
            return true;
        }
        return false;
    }
}
