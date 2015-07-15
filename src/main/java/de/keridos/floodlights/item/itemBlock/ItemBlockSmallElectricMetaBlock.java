package de.keridos.floodlights.item.itemBlock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

/**
 * Created by Keridos on 14.07.2015.
 * This Class
 */
public class ItemBlockSmallElectricMetaBlock extends ItemBlockWithMetadata {
    private final static String[] subNames = {
            "smallFluorescent", "squareFluorescent"
    };

    public ItemBlockSmallElectricMetaBlock(Block block) {
        super(block, block);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return this.getUnlocalizedName() + "_" + subNames[itemStack.getItemDamage()];
    }
}
