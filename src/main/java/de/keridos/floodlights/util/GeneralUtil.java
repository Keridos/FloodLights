package de.keridos.floodlights.util;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

//import ic2.api.item.IElectricItem;

/**
 * Created by Keridos on 28/11/2014.
 * This Class
 */
public class GeneralUtil {

    public static Item getMinecraftItem(String name) {
        Item item;
        item = GameData.getItemRegistry().getRaw(GameData.getItemRegistry().getId(new ResourceLocation("minecraft", name)));
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
        if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD")) return 200;
        if (item == Items.stick) return 100;
        if (item == Items.coal) return 1600;
        if (item == Items.lava_bucket) return 20000;
        if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
        if (item == Items.blaze_rod) return 2400;
        return GameRegistry.getFuelValue(itemStack);
    }

    public static boolean isItemStackValidElectrical(ItemStack itemStack) {
        Item item = itemStack.getItem();
        /*if (ModCompatibility.IC2Loaded) {
            if (item instanceof IElectricItem) {
                return ((IElectricItem) item).canProvideEnergy(itemStack);
            }
        } */
        return item instanceof IEnergyContainerItem;
    }

    public static EnumFacing getFacingFromEntity(Entity entity) {
        return null; //TODO: implement!
    }

    public static BlockPos getPosFromPosFacing(BlockPos pos, EnumFacing facing) {
        return new BlockPos(pos.getX() + facing.getFrontOffsetX(),
                pos.getY() + facing.getFrontOffsetY(),
                pos.getZ() + facing.getFrontOffsetZ());
    }

    public static BlockPos getPosFromIntArray(int[] array) {
        return (new BlockPos(array[0], array[1], array[2]));
    }

    public static int[] getIntArrayFromPos(BlockPos pos) {
        return new int[]{pos.getX(), pos.getY(), pos.getZ()};
    }

    public static boolean isBlockValidGrowable(Block block, World world, BlockPos blockPos) {
        boolean result = false;
        if ((block instanceof IGrowable && ((IGrowable) block).canGrow(world, blockPos,world.getBlockState(blockPos), false))
                /*|| (ModCompatibility.ACLoaded  && ModCompatibility.getInstance().isBlockValidAgriCraftSeed(block, world, blockPos)*/) {
            result = true;
        }
        return result;
    }
}
