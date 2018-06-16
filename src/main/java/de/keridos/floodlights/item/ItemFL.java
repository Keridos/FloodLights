package de.keridos.floodlights.item;

import de.keridos.floodlights.client.gui.CreativeTabFloodlight;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Textures;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Keridos on 06.10.14.
 * This Class is the generic Item class of this Mod.
 */

@SuppressWarnings("NullableProblems")
public class ItemFL extends Item {

    public ItemFL() {
        super();
        maxStackSize = 64;
        setNoRepair();
        setCreativeTab(CreativeTabFloodlight.FL_TAB);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName();
    }

    private String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    /**
     * Sets unlocalized name {@link Item#setUnlocalizedName(String)} and registryName {@link Item#setRegistryName(String)}.
     * <br>
     * Naming convention:
     * <li><b>camelCase</b> - unlocalized name</li>
     * <li><b>unserscore_based</b> - registry name (automatic conversion)</li>
     */
    protected void setNames(String unlocalizedName) {
        setUnlocalizedName(unlocalizedName);
        setRegistryName(Names.MOD_ID, Names.convertToUnderscore(unlocalizedName));
    }
}
