package de.keridos.floodlights.compatability;

import de.keridos.floodlights.init.ModBlocks;
import mezz.jei.api.*;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Keridos on 14/06/2016.
 * This Class
 */

@JEIPlugin
public class JEICompatability implements IModPlugin {
    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {

    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {

    }

    @Override
    public void onRecipeRegistryAvailable(@Nonnull IRecipeRegistry recipeRegistry) {

    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        IItemBlacklist blacklist = registry.getJeiHelpers().getItemBlacklist();
        blacklist.addItemToBlacklist(new ItemStack(ModBlocks.blockPhantomLight));
        blacklist.addItemToBlacklist(new ItemStack(ModBlocks.blockUVLightBlock));
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

    }
}
