package de.keridos.floodlights.compatability;

import de.keridos.floodlights.init.ModBlocks;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Keridos on 14/06/2016.
 * This Class
 */

@JEIPlugin
public class JEICompatability implements IModPlugin {
    @Override
    public void register(@Nonnull IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.blockPhantomLight));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.blockPhantomUVLight));
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

    }
}
