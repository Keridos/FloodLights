package de.keridos.floodlights.compatability;

import net.minecraft.item.ItemStack;

/**
 * Created by Nico on 28.02.14.
 */
public class ModCompatability {
    private static ModCompatability instance = null;

    private static Class Ic2Items;

    private ModCompatability() {
    }

    public static ModCompatability getInstance() {
        if (instance == null) {
            instance = new ModCompatability();
        }
        return instance;
    }

    public static ItemStack getIC2Item(String name) {
        try {
            if (Ic2Items == null) Ic2Items = Class.forName("ic2.core.Ic2Items");

            Object ret = Ic2Items.getField(name).get(null);

            if (ret instanceof ItemStack) {
                return ((ItemStack) ret).copy();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
