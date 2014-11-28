package de.keridos.floodlights.util;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;

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
}
