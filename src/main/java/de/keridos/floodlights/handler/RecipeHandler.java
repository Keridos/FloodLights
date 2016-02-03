package de.keridos.floodlights.handler;

import net.minecraftforge.fml.common.registry.GameRegistry;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static de.keridos.floodlights.util.GeneralUtil.getMinecraftItem;

/**
 * Created by Keridos on 28.02.14.
 * This Class adds all Recipes for this mod.
 */
public class RecipeHandler {
    private static RecipeHandler instance = null;

    private RecipeHandler() {
    }

    public static RecipeHandler getInstance() {
        if (instance == null) {
            instance = new RecipeHandler();
        }
        return instance;
    }

    public void initRecipes() {
        if (ConfigHandler.electricFloodlight || ConfigHandler.smallElectricFloodlight || ConfigHandler.uvFloodlight) {
            GameRegistry.addRecipe(new ItemStack(ModItems.rawFilament, 1), " L ", "RGR", " L ", 'R', new ItemStack(getMinecraftItem("redstone"), 1), 'G', new ItemStack(getMinecraftItem("gold_ingot"), 1), 'L', new ItemStack(getMinecraftItem("glowstone_dust"), 1));
            FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(ModItems.rawFilament, 1), new ItemStack(ModItems.glowingFilament, 1), 0.1F);
            GameRegistry.addRecipe(new ItemStack(ModItems.lightBulb, 1), " G ", "GFG", " I ", 'G', new ItemStack(getMinecraftItem("glass_pane"), 1), 'F', new ItemStack(ModItems.glowingFilament, 1), 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1));
        }
        if (ConfigHandler.electricFloodlight) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockElectricLight, 1), "CIC", "IBG", "CIC", 'C', new ItemStack(getMinecraftItem("cobblestone"), 1), 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1), 'B', new ItemStack(ModItems.lightBulb, 1), 'G', new ItemStack(getMinecraftItem("glass"), 1)));
        }
        if (ConfigHandler.smallElectricFloodlight) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSmallElectricLight, 2, 0), " G ", "GBG", "III", 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1), 'B', new ItemStack(ModItems.lightBulb, 1), 'G', new ItemStack(getMinecraftItem("glass"), 1)));
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.blockSmallElectricLight, 1, 1), new ItemStack(ModBlocks.blockSmallElectricLight, 1, 0));
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.blockSmallElectricLight, 1, 0), new ItemStack(ModBlocks.blockSmallElectricLight, 1, 1));
        }
        if (ConfigHandler.carbonFloodlight) {
            GameRegistry.addRecipe(new ItemStack(ModItems.carbonDissolver, 1), "GRG", "IGI", 'R', new ItemStack(getMinecraftItem("redstone"), 1), 'G', new ItemStack(getMinecraftItem("glass_pane"), 1), 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1));
            GameRegistry.addRecipe(new ItemStack(ModItems.mantle, 1), "SSS", "SRS", "SSS", 'R', new ItemStack(getMinecraftItem("redstone"), 1), 'S', new ItemStack(getMinecraftItem("string"), 1));
            GameRegistry.addRecipe(new ItemStack(ModItems.carbonLantern, 1), " G ", "GMG", "DFI", 'G', new ItemStack(getMinecraftItem("glass_pane"), 1), 'M', new ItemStack(ModItems.mantle, 1), 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1), 'D', new ItemStack(ModItems.carbonDissolver, 1), 'F', new ItemStack(getMinecraftItem("flint_and_steel"), 1));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockCarbonLight, 1), "CIC", "IBG", "CIC", 'C', new ItemStack(getMinecraftItem("cobblestone"), 1), 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1), 'B', new ItemStack(ModItems.carbonLantern, 1), 'G', new ItemStack(getMinecraftItem("glass"), 1)));
        }
        if (ConfigHandler.uvFloodlight) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockUVLight, 1), "CIC", "IBG", "CIC", 'C', new ItemStack(getMinecraftItem("cobblestone"), 1), 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1), 'B', new ItemStack(ModItems.lightBulb, 1), 'G', new ItemStack(getMinecraftItem("stained_glass"), 1, 15)));
        }
    }
}
