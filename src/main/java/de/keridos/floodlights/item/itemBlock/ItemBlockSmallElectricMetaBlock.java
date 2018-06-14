package de.keridos.floodlights.item.itemBlock;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * Created by Keridos on 14.07.2015.
 * This Class
 */
public class ItemBlockSmallElectricMetaBlock extends ItemBlock {
    private final static String[] subNames = {
            "smallFluorescent", "squareFluorescent"
    };

    public ItemBlockSmallElectricMetaBlock(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return this.getUnlocalizedName() + "_" + subNames[itemStack.getItemDamage()];
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
