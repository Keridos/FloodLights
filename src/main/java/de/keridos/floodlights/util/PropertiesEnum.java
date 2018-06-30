package de.keridos.floodlights.util;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Keridos on 04/06/2016.
 * This Class
 */
public class PropertiesEnum {
    public enum EnumModelSmallLight implements IStringSerializable {
        SMALL_ELECTRIC_FLOODLIGHTS_STRIP(0, "small_electric_floodlight_strip"),
        SMALL_ELECTRIC_FLOODLIGHT_SQUARE(1, "small_electric_floodlight_square");

        private int ID;
        private String name;

        EnumModelSmallLight(int ID, String name) {
            this.ID = ID;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public int getID() {
            return ID;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
