package de.keridos.floodlights.client.gui;

import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Keridos on 09.05.2015.
 * This Class
 */
public class CreativeTabFloodlight {
    public static final CreativeTabs FL_TAB = new CreativeTabs(Reference.MOD_ID.toLowerCase()) {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.blockElectricLight);
        }
    };
}




