package de.keridos.floodlights.compatability;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 * Created by Keridos on 28.02.14.
 * This Class will be used for Mod Compatibility functions.
 */

@SuppressWarnings("WeakerAccess")
public class ModCompatibility {
    private static ModCompatibility instance = null;

    public static boolean IC2Loaded = false;
    public static boolean BCLoaded = false;
    public static boolean CofhCoreLoaded = false;
    public static boolean NEILoaded = false;
    public static boolean JEILoaded = false;
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
        IC2Loaded = Loader.isModLoaded("ic2");
        BCLoaded = ModAPIManager.INSTANCE.hasAPI("BuildCraftAPI|tools");
        CofhCoreLoaded = ModAPIManager.INSTANCE.hasAPI("cofhapi");
        NEILoaded = Loader.isModLoaded("notenoughitems");
        JEILoaded = Loader.isModLoaded("jei");
        EnderIOLoaded = Loader.isModLoaded("enderio");
        ColoredLightCoreLoaded = Loader.isModLoaded("coloredlightcore");
        ACLoaded = Loader.isModLoaded("agricraft");
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
        addVersionCheckerInfo();
        //FMLInterModComms.sendMessage("Waila", "register", "de.keridos.floodlights.compatability.WailaTileHandler.callbackRegister");
        WrenchAvailable = (BCLoaded || EnderIOLoaded || IC2Loaded || CofhCoreLoaded);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isItemValidWrench(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().equals("ic2:wrench")
                || stack.getItem().getRegistryName().toString().equals("ic2:wrench_new")
                || stack.getItem().getRegistryName().toString().equals("enderio:item_yeta_wrench")
                || stack.getItem().getRegistryName().toString().equals("thermalfoundation:wrench")
                || stack.getItem().getRegistryName().toString().equals("buildcraftcore:wrench");
    }

    /*@Optional.Method(modid = "AgriCraft")
    public boolean isBlockValidAgriCraftSeed(Block block, World world, BlockPos blockPos) {
        if (block instanceof ICrop && ((APIv2) API.getAPI(2)).getCrop(world, blockPos.posX, blockPos.posY, blockPos.posZ).hasPlant() && ((APIv2) API.getAPI(2)).getCrop(world, blockPos.posX, blockPos.posY, blockPos.posZ).getPlant().getGrowthRequirement().canGrow(world, blockPos.posX, blockPos.posY, blockPos.posZ)) {
            return true;
        }
        return false;
    }        */
}
