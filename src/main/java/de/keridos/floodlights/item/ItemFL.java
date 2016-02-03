package de.keridos.floodlights.item;

import de.keridos.floodlights.client.gui.CreativeTabFloodlight;
import de.keridos.floodlights.reference.Textures;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Keridos on 06.10.14.
 * This Class is the generic Item class of this Mod.
 */

public class ItemFL extends Item {
    public ItemFL() {
        super();
        this.maxStackSize = 64;
        this.setNoRepair();
        this.setCreativeTab(CreativeTabFloodlight.FL_TAB);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
