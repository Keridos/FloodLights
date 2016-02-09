package de.keridos.floodlights.util;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import de.keridos.floodlights.compatability.ModCompatibility;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.logging.Logger;

/**
 * Created by Keridos on 28/11/2014.
 * This Class
 */
public class GeneralUtil {

    public static Item getMinecraftItem(String name) {
        Item item;
        item = GameData.getItemRegistry().getRaw("minecraft:" + name);
        return item;
    }

    public static String safeLocalize(String text) {
        if (StatCollector.translateToLocal(text) != null) {
            return StatCollector.translateToLocal(text);
        } else {
            return StatCollector.translateToFallback(text);
        }
    }

    public static int getBurnTime(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
            Block block = Block.getBlockFromItem(item);

            if (block == Blocks.wooden_slab) {
                return 150;
            }

            if (block.getMaterial() == Material.wood) {
                return 300;
            }

            if (block == Blocks.coal_block) {
                return 14400;
            }
        }

        if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
        if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
        if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) return 200;
        if (item == Items.stick) return 100;
        if (item == Items.coal) return 1600;
        if (item == Items.lava_bucket) return 20000;
        if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
        if (item == Items.blaze_rod) return 2400;
        return GameRegistry.getFuelValue(itemStack);
    }

    public static boolean isItemStackValidElectrical(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (ModCompatibility.IC2Loaded) {
            if (item instanceof IElectricItem) {
                return ((IElectricItem) item).canProvideEnergy(itemStack);
            }
        }
        return item instanceof IEnergyContainerItem;
    }

    public static boolean isBlockValidGrowable(Block block, World world, BlockPos blockPos) {
        boolean result = false;
        if ((block instanceof IGrowable && ((IGrowable) block).func_149851_a(world, blockPos.posX, blockPos.posY, blockPos.posZ, false))
                || (ModCompatibility.ACLoaded && ModCompatibility.getInstance().isBlockValidAgriCraftSeed(block, world, blockPos))) {
            Logger.getGlobal().info("blockcangrow: " + (block instanceof IGrowable && ((IGrowable) block).func_149851_a(world, blockPos.posX, blockPos.posY, blockPos.posZ, false)));
            result = true;
        }
        return result;
    }

    public static Block getBlockFromDirection(World world, int x, int y, int z, ForgeDirection direction, int distance) {
        return world.getBlock(x + direction.offsetX * distance, y + direction.offsetY * distance, z + direction.offsetZ * distance);
    }
}
