package de.keridos.floodlights.handler;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by Nico on 28.02.14.
 */
public class RecipeHandler {
    private static RecipeHandler instance = null;
    private ConfigHandler configHandler = ConfigHandler.getInstance();

    private RecipeHandler() {
    }

    public static RecipeHandler getInstance() {
        if (instance == null) {
            instance = new RecipeHandler();
        }
        return instance;
    }

    public Item getMinecraftItem(String name) {
        Item item;
        item = GameData.getItemRegistry().getRaw("minecraft:" + name);
        return item;
    }

    public void initRecipes() {
        if (configHandler.electricFloodlight) {
            GameRegistry.addRecipe(new ItemStack(ModItems.rawFilament, 1), "RGR", 'R', new ItemStack(getMinecraftItem("redstone"), 1, 0), 'G', new ItemStack(getMinecraftItem("gold_ingot"), 1, 0));
            FurnaceRecipes.smelting().func_151394_a(new ItemStack(ModItems.rawFilament, 1), new ItemStack(ModItems.glowingFilament, 1), 0.1F);
            GameRegistry.addRecipe(new ItemStack(ModItems.lightbulb, 1), " G ", "GFG", " I ", 'G', new ItemStack(getMinecraftItem("glass_pane"), 1), 'F', new ItemStack(ModItems.glowingFilament, 1), 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockElectricLight, 1), "CIC", "IBI", "CGC", 'C', new ItemStack(getMinecraftItem("cobblestone"), 1), 'I', new ItemStack(getMinecraftItem("iron_ingot"), 1), 'B', new ItemStack(ModItems.lightbulb, 1), 'G', new ItemStack(getMinecraftItem("glass"), 1)));
        }
    }
}
