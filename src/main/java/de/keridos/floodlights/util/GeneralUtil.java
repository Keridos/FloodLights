package de.keridos.floodlights.util;

import cofh.redstoneflux.api.IEnergyContainerItem;
import de.keridos.floodlights.compatability.ModCompatibility;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.logging.Logger;

/**
 * Created by Keridos on 28/11/2014.
 * This Class
 */
public class GeneralUtil {

    public static Item getMinecraftItem(String name) {
        return Item.REGISTRY.getObject(new ResourceLocation("minecraft", name));
    }

    @SuppressWarnings("deprecation")
    public static String safeLocalize(String key) {
        String translation = I18n.translateToLocal(key);
        if (translation.equals(key))
            translation = I18n.translateToFallback(key);

        return translation;
    }

    public static int getBurnTime(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
            Block block = Block.getBlockFromItem(item);

            if (block == Blocks.WOODEN_SLAB) {
                return 150;
            }

            //noinspection deprecation
            if (block.getMaterial(block.getStateFromMeta(itemStack.getItemDamage())) == Material.WOOD) {
                return 300;
            }

            if (block == Blocks.COAL_BLOCK) {
                return 14400;
            }
        }

        if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
        if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
        if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD")) return 200;
        if (item == Items.STICK) return 100;
        if (item == Items.COAL) return 1600;
        if (item == Items.LAVA_BUCKET) return 20000;
        if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
        if (item == Items.BLAZE_ROD) return 2400;
        return ForgeEventFactory.getItemBurnTime(itemStack);
    }

    public static boolean isItemStackValidElectrical(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (ModCompatibility.IC2Loaded) {
            if (item instanceof IElectricItem) {
                return ((IElectricItem) item).canProvideEnergy(itemStack);
            }
        }

        return ModCompatibility.CofhCoreLoaded && item instanceof IEnergyContainerItem;
    }

    public static BlockPos getPosFromPosFacing(BlockPos pos, EnumFacing facing) {
        return new BlockPos(pos.getX() + facing.getFrontOffsetX(),
                pos.getY() + facing.getFrontOffsetY(),
                pos.getZ() + facing.getFrontOffsetZ());
    }

    public static boolean isBlockValidGrowable(Block block, World world, BlockPos blockPos) {
        boolean result = false;
        if ((block instanceof IGrowable && ((IGrowable) block).canGrow(world, blockPos, world.getBlockState(blockPos), false))
            /*|| (ModCompatibility.ACLoaded  && ModCompatibility.getInstance().isBlockValidAgriCraftSeed(block, world, blockPos)*/) {
            Logger.getGlobal().info("blockcangrow: " + (((IGrowable) block).canGrow(world, blockPos, world.getBlockState(blockPos), false)));
            result = true;
        }
        return result;
    }
}
